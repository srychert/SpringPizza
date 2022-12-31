package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.service.PizzaService;

@Controller
@RequestMapping("pizza")
public class WebPizzaController {
    private final PizzaService pizzaService;
    private final PizzaRepository pizzaRepository;

    public WebPizzaController(@Autowired PizzaService pizzaService, PizzaRepository pizzaRepository) {
        this.pizzaService = pizzaService;
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping("/list")
    String getPizzaList(Model model) {
        model.addAttribute("pizzaList", pizzaService.getAll());
        return "pizza-list";
    }

    @GetMapping("/add")
    public String showAddForm(Pizza pizza) {
        return "pizza-add";
    }

    @GetMapping("/{pizzaId}")
    String getPizza(@PathVariable Long pizzaId, Model model) {
        model.addAttribute("pizza", pizzaService.get(pizzaId));
        return "pizza-details";
    }

    @PostMapping(path = "/add")
    public String add(@Valid Pizza pizza, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "pizza-add";
        }

        var pizzaInDb = pizzaRepository.findByName(pizza.getName());
        if (pizzaInDb.isPresent()) {
            result.rejectValue("name", "error.Name", "Pizza with this name already exists");
            return "pizza-add";
        }

        pizzaService.add(pizza);
        return "redirect:/pizza/list";
    }

    @GetMapping("/delete/{pizzaId}")
    public String delete(@PathVariable Long pizzaId, Model model) {
        pizzaService.delete(pizzaId);
        return "redirect:/pizza/list";
    }

    @PostMapping("/update/{pizzaId}")
    public String update(@PathVariable Long pizzaId, @Valid @RequestBody Pizza pizza, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            return "pizza-list";
        }

        pizzaService.update(pizzaId, pizza);
        return "redirect:/pizza/list";
    }

}
