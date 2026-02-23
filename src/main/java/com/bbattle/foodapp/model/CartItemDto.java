package com.bbattle.foodapp.model;

public class CartItemDto {
    private Long productId;
    private int quantity;

    //getters and setters
    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
