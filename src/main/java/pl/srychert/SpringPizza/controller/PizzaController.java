package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.service.PizzaService;

@RestController
@RequestMapping("/api/pizza")
public class PizzaController {

    @Autowired
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public Iterable<Pizza> getAll() {
        return pizzaService.getAll();
    }

    @GetMapping("/{pizzaId}")
    public Pizza get(@PathVariable Long pizzaId) {
        return pizzaService.get(pizzaId);
    }

    @PostMapping
    public Pizza add(@Valid @RequestBody Pizza pizza) {
        return pizzaService.add(pizza);
    }

    @DeleteMapping("/{pizzaId}")
    public Pizza delete(@PathVariable Long pizzaId) {
        return pizzaService.delete(pizzaId);
    }

    @PutMapping("/{pizzaId}")
    public Pizza update(@PathVariable Long pizzaId, @Valid @RequestBody Pizza pizza) {
        return pizzaService.update(pizzaId, pizza);
    }
}
