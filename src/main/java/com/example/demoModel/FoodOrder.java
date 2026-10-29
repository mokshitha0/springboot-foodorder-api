package com.example.demoModel;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodOrder {

    private Long orderId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Food item cannot be empty")
    private String foodItem;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Spice level is required")
    private String spiceLevel;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    public FoodOrder() {
    }

    public FoodOrder(Long orderId, String customerName, String foodItem,
                     String category, String spiceLevel, int quantity,
                     BigDecimal price) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.foodItem = foodItem;
        this.category = category;
        this.spiceLevel = spiceLevel;
        this.quantity = quantity;
        this.price = price;
    }

    // Backward-compatible constructor for the original fields
    public FoodOrder(Long orderId, String customerName, String foodItem,
                     int quantity, BigDecimal price) {
        this(orderId, customerName, foodItem, "Main Course", "Medium",
                quantity, price);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
