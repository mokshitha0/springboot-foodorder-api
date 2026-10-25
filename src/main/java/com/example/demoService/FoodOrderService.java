
package com.example.demoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.demoModel.FoodOrder;
import com.example.demoException.FoodOrderNotFoundException;

@Service
public class FoodOrderService {

    private final List<FoodOrder> orders = new ArrayList<>();
    private long nextId = 1;

    // Add a new food order
    public FoodOrder addOrder(FoodOrder order) {
        validateOrder(order);

        order.setOrderId(nextId++);
        orders.add(order);

        return order;
    }

    // Get all orders
    public List<FoodOrder> getAllOrders() {
        return new ArrayList<>(orders);
    }

    // Get order by ID
    public FoodOrder getOrderById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                "Order ID must be positive"
            );
        }

        return orders.stream()
                .filter(order -> order.getOrderId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                    new FoodOrderNotFoundException(
                        "Food order not found with ID: " + id
                    )
                );
    }

    // Search orders by customer name
    public List<FoodOrder> searchByCustomerName(String name) {
        checkNotBlank(name, "Customer name must not be blank");

        String keyword = name.trim().toLowerCase(Locale.ROOT);

        return orders.stream()
                .filter(order -> order.getCustomerName()
                    .toLowerCase(Locale.ROOT)
                    .contains(keyword))
                .toList();
    }

    // Search orders by food item
    public List<FoodOrder> searchByFoodItem(String foodItem) {
        checkNotBlank(foodItem, "Food item must not be blank");

        String keyword = foodItem.trim().toLowerCase(Locale.ROOT);

        return orders.stream()
                .filter(order -> order.getFoodItem()
                    .toLowerCase(Locale.ROOT)
                    .contains(keyword))
                .toList();
    }

    // Find orders within a maximum unit price
    public List<FoodOrder> getOrdersWithinPrice(
            BigDecimal maxPrice) {

        if (maxPrice == null || maxPrice.signum() < 0) {
            throw new IllegalArgumentException(
                "Maximum price is required and cannot be negative"
            );
        }

        return orders.stream()
                .filter(order ->
                    order.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    // Update complete order
    public FoodOrder updateOrder(Long id, FoodOrder updatedOrder) {
        FoodOrder existing = getOrderById(id);

        validateOrder(updatedOrder);

        existing.setCustomerName(updatedOrder.getCustomerName());
        existing.setFoodItem(updatedOrder.getFoodItem());
        existing.setQuantity(updatedOrder.getQuantity());
        existing.setPrice(updatedOrder.getPrice());

        return existing;
    }

    // Update only quantity
    public FoodOrder updateQuantity(Long id, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(
                "Quantity must be at least 1"
            );
        }

        FoodOrder existing = getOrderById(id);
        existing.setQuantity(quantity);

        return existing;
    }

    // Delete an order
    public String deleteOrder(Long id) {
        FoodOrder existing = getOrderById(id);
        orders.remove(existing);

        return "Food order deleted successfully";
    }

    // Common validation method
    private void validateOrder(FoodOrder order) {
        if (order == null) {
            throw new IllegalArgumentException(
                "Food order cannot be null"
            );
        }

        checkNotBlank(order.getCustomerName(),
                "Customer name is required");

        checkNotBlank(order.getFoodItem(),
                "Food item cannot be empty");

        if (order.getQuantity() < 1) {
            throw new IllegalArgumentException(
                "Quantity must be at least 1"
            );
        }

        if (order.getPrice() == null
                || order.getPrice().signum() <= 0) {
            throw new IllegalArgumentException(
                "Price must be greater than zero"
            );
        }
    }

    private void checkNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
