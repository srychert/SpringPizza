package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Order;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.OrderRepository;

import java.util.Optional;

@Service
@Transactional
public class OrderService {
    final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order get(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Order id in DB"));
    }

    public Order add(Order order) {
        var orderToAdd = new Order(order.getPizzas(), order.getClient());
        return orderRepository.save(orderToAdd);
    }

    public Order delete(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.deleteById(id);
        }
        return order.orElseThrow(() -> new ApiRequestException("No such Order id in DB"));
    }

    public Order update(Long id, Order updatedOrder) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Order id in DB"));

        order.setPizzas(updatedOrder.getPizzas());
        order.setClient(updatedOrder.getClient());

        return orderRepository.save(order);
    }
}
