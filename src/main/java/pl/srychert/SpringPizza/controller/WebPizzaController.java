package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.enums.Sorts;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.repository.UserRepository;
import pl.srychert.SpringPizza.service.PizzaService;

@Controller
@RequestMapping("pizza")
public class WebPizzaController {
    private final PizzaService pizzaService;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;

    public WebPizzaController(@Autowired PizzaService pizzaService,
                              PizzaRepository pizzaRepository, UserRepository userRepository) {
        this.pizzaService = pizzaService;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    String getPizzaList(@RequestParam(required = false, defaultValue = "ASC") Sorts sort, Model model) {

        model.addAttribute("pizzaList", pizzaService.getAll(sort));
        model.addAttribute("sort", sort.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        userRepository.findByUserName(name).ifPresent(user -> {
            model.addAttribute("user", user);
        });

        return "pizza-list";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String showAddForm(Pizza pizza) {
        return "pizza-add";
    }

    @GetMapping("/{pizzaId}")
    String getPizza(@PathVariable Long pizzaId, Model model) {
        model.addAttribute("pizza", pizzaService.get(pizzaId));
        return "pizza-details";
    }

    @PostMapping(path = "/add")
    @Secured("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable Long pizzaId, Model model) {
        pizzaService.delete(pizzaId);
        return "redirect:/pizza/list";
    }

    @GetMapping("/edit/{pizzaId}")
    @Secured("ROLE_ADMIN")
    public String showEditForm(@PathVariable Long pizzaId, Model model) {
        var pizza = pizzaService.get(pizzaId);

        model.addAttribute("pizza", pizza);
        return "pizza-edit";
    }

    @PostMapping("/update/{pizzaId}")
    @Secured("ROLE_ADMIN")
    public String update(@PathVariable Long pizzaId, @Valid Pizza pizza, BindingResult result, Model model) {
        if (result.hasErrors()) {
            pizza.setId(pizzaId);
            model.addAttribute("pizza", pizza);
            return "pizza-edit";
        }

        pizzaService.update(pizzaId, pizza);
        return "redirect:/pizza/list";
    }

}
