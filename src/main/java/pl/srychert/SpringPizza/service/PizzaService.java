package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.PizzaRepository;

import java.util.Optional;

@Service
@Transactional
public class PizzaService {
    final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public Iterable<Pizza> getAll() {
        return pizzaRepository.findAll();
    }

    public Iterable<Pizza> getAllByPriceAsc() {
        return pizzaRepository.findAllByOrderByPriceAsc();
    }

    public Iterable<Pizza> getAllByPriceDesc() {
        return pizzaRepository.findAllByOrderByPriceDesc();
    }

    public Pizza get(Long id) {
        return pizzaRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Pizza id in DB"));
    }

    public Pizza add(Pizza pizza) {
        var pizzaToAdd = new Pizza(pizza.getName(), pizza.getPrice());
        return pizzaRepository.save(pizzaToAdd);
    }

    public Pizza delete(Long id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if (pizza.isPresent()) {
            pizzaRepository.deleteById(id);
        }
        return pizza.orElseThrow(() -> new ApiRequestException("No such Pizza id in DB"));
    }

    public Pizza update(Long id, Pizza updatedPizza) {
        var pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Pizza id in DB"));

        pizza.setName(updatedPizza.getName());
        pizza.setPrice(updatedPizza.getPrice());

        return pizzaRepository.save(pizza);
    }
}
