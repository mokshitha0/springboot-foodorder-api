package com.example.demoController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public FoodOrder addOrder(@Valid @RequestBody FoodOrder order) {
        return service.addOrder(order);
    }

    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @GetMapping("/search")
    public List<FoodOrder> searchByCustomerName(
            @RequestParam String customerName) {
        return service.searchByCustomerName(customerName);
    }

    @GetMapping("/foodItem")
    public List<FoodOrder> searchByFoodItem(@RequestParam String name) {
        return service.searchByFoodItem(name);
    }

    @GetMapping("/price")
    public List<FoodOrder> getOrdersWithinPrice(
            @RequestParam BigDecimal maxPrice) {
        return service.getOrdersWithinPrice(maxPrice);
    }

    // Custom endpoint: filter by Starters, Main Course, or Desserts
    @GetMapping("/category/{category}")
    public List<FoodOrder> getOrdersByCategory(
            @PathVariable String category) {
        return service.getOrdersByCategory(category);
    }

    // Custom endpoint: filter by Mild, Medium, or Spicy
    @GetMapping("/spice-level/{spiceLevel}")
    public List<FoodOrder> getOrdersBySpiceLevel(
            @PathVariable String spiceLevel) {
        return service.getOrdersBySpiceLevel(spiceLevel);
    }

    @PutMapping("/{id}")
    public FoodOrder updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody FoodOrder order) {
        return service.updateOrder(id, order);
    }

    @PatchMapping("/{id}/quantity")
    public FoodOrder updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        return service.updateQuantity(id, quantity);
    }

    // Custom endpoint: update only the spice preference
    @PatchMapping("/{id}/spice-level")
    public FoodOrder updateSpiceLevel(
            @PathVariable Long id,
            @RequestParam String spiceLevel) {
        return service.updateSpiceLevel(id, spiceLevel);
    }

    // Custom endpoint: return a bill summary
    @GetMapping("/{id}/bill")
    public Map<String, Object> getBill(@PathVariable Long id) {
        FoodOrder order = service.getOrderById(id);
        BigDecimal total = service.calculateBill(id);

        Map<String, Object> bill = new LinkedHashMap<>();
        bill.put("orderId", order.getOrderId());
        bill.put("customerName", order.getCustomerName());
        bill.put("foodItem", order.getFoodItem());
        bill.put("quantity", order.getQuantity());
        bill.put("unitPrice", order.getPrice());
        bill.put("totalBill", total);
        return bill;
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return service.deleteOrder(id);
    }
}
