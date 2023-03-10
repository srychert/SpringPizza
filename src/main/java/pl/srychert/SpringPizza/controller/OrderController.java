package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.Order;
import pl.srychert.SpringPizza.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public Iterable<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@authComponent.isOrderOwner(#orderId) or hasRole('ROLE_ADMIN')")
    public Order get(@PathVariable Long orderId) {
        return orderService.get(orderId);
    }

    @PostMapping
    public Order add(@Valid @RequestBody Order order) {
        return orderService.add(order);
    }

    @DeleteMapping("/{orderId}")
    @Secured("ROLE_ADMIN")
    public Order delete(@PathVariable Long orderId) {
        return orderService.delete(orderId);
    }

    @PutMapping("/{orderId}")
    @Secured("ROLE_ADMIN")
    public Order update(@PathVariable Long orderId, @Valid @RequestBody Order order) {
        return orderService.update(orderId, order);
    }
}
