package com.example.demoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.demoException.FoodOrderNotFoundException;
import com.example.demoModel.FoodOrder;

@Service
public class FoodOrderService {

    private static final List<String> CATEGORIES =
            List.of("Starters", "Main Course", "Desserts");
    private static final List<String> SPICE_LEVELS =
            List.of("Mild", "Medium", "Spicy");

    private final List<FoodOrder> orders = new ArrayList<>();
    private long nextId = 1;

    public FoodOrder addOrder(FoodOrder order) {
        validateOrder(order);
        order.setOrderId(nextId++);
        orders.add(order);
        return order;
    }

    public List<FoodOrder> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public FoodOrder getOrderById(Long id) {
        validateId(id);
        return orders.stream()
                .filter(order -> order.getOrderId().equals(id))
                .findFirst()
                .orElseThrow(() -> new FoodOrderNotFoundException(
                        "Food order not found with ID: " + id));
    }

    public List<FoodOrder> searchByCustomerName(String name) {
        checkNotBlank(name, "Customer name must not be blank");
        String keyword = name.trim().toLowerCase(Locale.ROOT);

        return orders.stream()
                .filter(order -> order.getCustomerName()
                        .toLowerCase(Locale.ROOT).contains(keyword))
                .toList();
    }

    public List<FoodOrder> searchByFoodItem(String foodItem) {
        checkNotBlank(foodItem, "Food item must not be blank");
        String keyword = foodItem.trim().toLowerCase(Locale.ROOT);

        return orders.stream()
                .filter(order -> order.getFoodItem()
                        .toLowerCase(Locale.ROOT).contains(keyword))
                .toList();
    }

    public List<FoodOrder> getOrdersWithinPrice(BigDecimal maxPrice) {
        if (maxPrice == null || maxPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "Maximum price is required and cannot be negative");
        }

        return orders.stream()
                .filter(order -> order.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    // Custom method: filter orders by Starters, Main Course, or Desserts
    public List<FoodOrder> getOrdersByCategory(String category) {
        String validCategory = normalizeChoice(
                category, CATEGORIES, "category");

        return orders.stream()
                .filter(order -> order.getCategory().equals(validCategory))
                .toList();
    }

    // Custom method: filter orders by Mild, Medium, or Spicy preference
    public List<FoodOrder> getOrdersBySpiceLevel(String spiceLevel) {
        String validSpiceLevel = normalizeChoice(
                spiceLevel, SPICE_LEVELS, "spice level");

        return orders.stream()
                .filter(order -> order.getSpiceLevel().equals(validSpiceLevel))
                .toList();
    }

    public FoodOrder updateOrder(Long id, FoodOrder updatedOrder) {
        FoodOrder existing = getOrderById(id);
        validateOrder(updatedOrder);

        existing.setCustomerName(updatedOrder.getCustomerName());
        existing.setFoodItem(updatedOrder.getFoodItem());
        existing.setCategory(updatedOrder.getCategory());
        existing.setSpiceLevel(updatedOrder.getSpiceLevel());
        existing.setQuantity(updatedOrder.getQuantity());
        existing.setPrice(updatedOrder.getPrice());

        return existing;
    }

    public FoodOrder updateQuantity(Long id, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        FoodOrder existing = getOrderById(id);
        existing.setQuantity(quantity);
        return existing;
    }

    // Custom method: change spice preference without replacing the full order
    public FoodOrder updateSpiceLevel(Long id, String spiceLevel) {
        String validSpiceLevel = normalizeChoice(
                spiceLevel, SPICE_LEVELS, "spice level");

        FoodOrder existing = getOrderById(id);
        existing.setSpiceLevel(validSpiceLevel);
        return existing;
    }

    // Custom method: calculate unit price multiplied by quantity
    public BigDecimal calculateBill(Long id) {
        FoodOrder order = getOrderById(id);
        return order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
    }

    public String deleteOrder(Long id) {
        FoodOrder existing = getOrderById(id);
        orders.remove(existing);
        return "Food order deleted successfully";
    }

    private void validateOrder(FoodOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Food order cannot be null");
        }

        checkNotBlank(order.getCustomerName(), "Customer name is required");
        checkNotBlank(order.getFoodItem(), "Food item cannot be empty");

        if (order.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        if (order.getPrice() == null || order.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        order.setCategory(normalizeChoice(
                order.getCategory(), CATEGORIES, "category"));
        order.setSpiceLevel(normalizeChoice(
                order.getSpiceLevel(), SPICE_LEVELS, "spice level"));
    }

    private String normalizeChoice(
            String value, List<String> allowedValues, String fieldName) {
        checkNotBlank(value, fieldName + " is required");

        String normalizedInput = value.trim().replace('_', ' ');
        return allowedValues.stream()
                .filter(option -> option.equalsIgnoreCase(normalizedInput))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid " + fieldName + ". Allowed values: "
                                + String.join(", ", allowedValues)));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
    }

    private void checkNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
