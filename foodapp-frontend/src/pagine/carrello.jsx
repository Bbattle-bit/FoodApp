import React from "react";
import { useCart } from "./CartContext";

function CarrelloPage() {
  const { carrello, setCarrello } = useCart();

  // Rimuovi un item dal carrello
  const handleRemoveItem = (index) => {
    setCarrello((prev) => prev.filter((_, i) => i !== index));
  };

  // Procedi al checkout
  const handleCheckout = async () => {
    if (carrello.length === 0) return;

    const token = localStorage.getItem("token");
    if (!token) {
      alert("Devi essere loggato per procedere al pagamento");
      return;
    }

    try {
      const response = await fetch(
        "http://localhost:8080/api/payment/create-checkout-session",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token,
          },
          body: JSON.stringify(
            carrello.map((item) => ({
              productId: item.id,
              quantity: item.quantity || 1,
            }))
          ),
        }
      );

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text || "Errore durante il checkout");
      }

      const data = await response.json();

      // Redirect alla pagina Stripe
      window.location.href = data.url;
    } catch (error) {
      console.error("Errore durante il checkout:", error);
      alert("Errore durante il checkout. Controlla console per dettagli.");
    }
  };

  if (carrello.length === 0)
    return <div className="p-10 text-xl">Il carrello è vuoto</div>;

  return (
    <div className="p-10">
      <h1 className="text-2xl font-bold mb-4">Carrello</h1>

      {carrello.map((item, index) => (
        <div
          key={index}
          className="flex justify-between border p-2 rounded mb-2 items-center"
        >
          <p>
            {item.name} - €{item.price.toFixed(2)} x {item.quantity || 1}
          </p>
          <button
            onClick={() => handleRemoveItem(index)}
            className="bg-red-500 text-white px-2 rounded hover:bg-red-600 transition"
          >
            Rimuovi
          </button>
        </div>
      ))}

      <div className="flex justify-between mt-4 items-center">
        <p className="font-bold">
          Totale: €
          {carrello
            .reduce((sum, item) => sum + item.price * (item.quantity || 1), 0)
            .toFixed(2)}
        </p>
        <button
          onClick={handleCheckout}
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition cursor-pointer"
        >
          Procedi al pagamento
        </button>
      </div>
    </div>
  );
}

export default CarrelloPage;