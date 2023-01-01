package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Order;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.OrderRepository;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    final OrderRepository orderRepository;
    final PizzaRepository pizzaRepository;
    final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        PizzaRepository pizzaRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
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
        Long clientId = order.getClient().getId();
        User client = userRepository
                .findById(clientId)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        List<Long> pizzaIds = order.getPizzas().stream().map(Pizza::getId).toList();
        List<Pizza> pizzas = (List<Pizza>) pizzaRepository.findAllById(pizzaIds);

        Order orderToAdd = new Order();
        orderToAdd.setClient(client);
        orderToAdd.setPizzas(pizzas);

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

        Long clientId = updatedOrder.getClient().getId();
        User client = userRepository
                .findById(clientId)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        List<Long> pizzaIds = updatedOrder.getPizzas().stream().map(Pizza::getId).toList();
        List<Pizza> pizzas = (List<Pizza>) pizzaRepository.findAllById(pizzaIds);

        order.setClient(client);
        order.setPizzas(pizzas);

        return orderRepository.save(order);
    }
}
