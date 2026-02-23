package com.bbattle.foodapp.controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.bbattle.foodapp.model.CartItemDto;
import com.bbattle.foodapp.model.FoodItem;
import com.bbattle.foodapp.model.Order;
import com.bbattle.foodapp.model.OrderItem;
import com.bbattle.foodapp.model.OrderStatus;
import com.bbattle.foodapp.model.User;
import com.bbattle.foodapp.repository.FoodItemRepository;
import com.bbattle.foodapp.repository.OrderRepository;
import com.bbattle.foodapp.repository.OrderItemRepository;
import com.bbattle.foodapp.repository.UserRepository;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final FoodItemRepository foodItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public PaymentController(FoodItemRepository foodItemRepository,
                             OrderRepository orderRepository,
                             OrderItemRepository orderItemRepository,
                             UserRepository userRepository) {
        this.foodItemRepository = foodItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    // 🔹 Crea sessione Stripe per il checkout
    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody List<CartItemDto> cartItems) throws StripeException {

        // Prende l'utente loggato dal token
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Utente non autenticato");
        }
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato per email: " + email));

        Stripe.apiKey = stripeSecretKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (CartItemDto item : cartItems) {
            FoodItem foodItem = foodItemRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato: " + item.getProductId()));

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount((long) (foodItem.getPrice() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(foodItem.getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();
            lineItems.add(lineItem);
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:5173/success")
                        .setCancelUrl("http://localhost:5173/cancel")
                        .addAllLineItem(lineItems)
                        .putMetadata("items", mapper.writeValueAsString(cartItems))
                        .putMetadata("userEmail", email) // salviamo anche l'email dell'utente
                        .build();

        Session session = Session.create(params);
        return Map.of("url", session.getUrl());
    }

    // 🔹 Webhook Stripe
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> obj = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            if (!"checkout.session.completed".equals(obj.get("type"))) {
                return ResponseEntity.ok("evento ignorato");
            }

            Map<String, Object> dataObject = (Map<String, Object>) obj.get("data");
            Map<String, Object> session = (Map<String, Object>) dataObject.get("object");

            // Leggi email dal metadata (metodo sicuro)
            Map<String, Object> metadata = (Map<String, Object>) session.get("metadata");
            String email = (String) metadata.get("userEmail");
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato per email: " + email));

            // Crea ordine
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.IN_ATTESA_DI_CONFERMA);

            // Leggi items dal metadata
            String itemsJson = (String) metadata.get("items");
            List<CartItemDto> cartItems = mapper.readValue(itemsJson, new TypeReference<List<CartItemDto>>() {});

            List<OrderItem> orderItems = new ArrayList<>();
            double total = 0;

            for (CartItemDto ci : cartItems) {
                FoodItem food = foodItemRepository.findById(ci.getProductId())
                        .orElseThrow(() -> new RuntimeException("FoodItem non trovato: " + ci.getProductId()));

                OrderItem item = new OrderItem();
                item.setFoodItem(food);
                item.setQuantity(ci.getQuantity());
                item.setPrice(food.getPrice() * ci.getQuantity());
                item.setOrder(order);

                orderItems.add(item);
                total += item.getPrice();
            }

            order.setItems(orderItems);
            order.setTotalAmount(total);

            orderRepository.save(order);

            System.out.println("✅ Ordine salvato con successo per utente: " + user.getUsername());

            return ResponseEntity.ok("ordine salvato");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("errore webhook: " + e.getMessage());
        }
    }
}