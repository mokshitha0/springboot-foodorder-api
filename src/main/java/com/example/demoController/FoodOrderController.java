
package com.example.demoController;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoModel.FoodOrder;
import com.example.demoService.FoodOrderService;

@RestController
@RequestMapping("/orders")
public class FoodOrderController {

    private final FoodOrderService service;

    public FoodOrderController(FoodOrderService service) {
        this.service = service;
    }

    // Add a food order
    @PostMapping
    public FoodOrder addOrder(@Valid @RequestBody FoodOrder order) {
        return service.addOrder(order);
    }

    // Get all orders
    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return service.getAllOrders();
    }

    // Get order by ID
    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    // Search by customer name
    @GetMapping("/search")
    public List<FoodOrder> searchByCustomerName(
            @RequestParam String customerName) {
        return service.searchByCustomerName(customerName);
    }

    // Search by food item
    @GetMapping("/foodItem")
    public List<FoodOrder> searchByFoodItem(
            @RequestParam String name) {
        return service.searchByFoodItem(name);
    }

    // Find orders within a maximum price
    @GetMapping("/price")
    public List<FoodOrder> getOrdersWithinPrice(
            @RequestParam BigDecimal maxPrice) {
        return service.getOrdersWithinPrice(maxPrice);
    }

    // Replace order details
    @PutMapping("/{id}")
    public FoodOrder updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody FoodOrder order) {
        return service.updateOrder(id, order);
    }

    // Update only the quantity
    @PatchMapping("/{id}/quantity")
    public FoodOrder updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        return service.updateQuantity(id, quantity);
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return service.deleteOrder(id);
    }
}
