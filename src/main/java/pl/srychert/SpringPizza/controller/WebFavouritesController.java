package pl.srychert.SpringPizza.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.srychert.SpringPizza.domain.Favourites;
import pl.srychert.SpringPizza.service.FavouritesService;
import pl.srychert.SpringPizza.service.PizzaService;
import pl.srychert.SpringPizza.service.UserService;

import java.util.ArrayList;

@Controller
@RequestMapping("favourites")
public class WebFavouritesController {

    private final FavouritesService favouritesService;
    private final UserService userService;
    private final PizzaService pizzaService;

    public WebFavouritesController(FavouritesService favouritesService,
                                   UserService userService, PizzaService pizzaService) {
        this.favouritesService = favouritesService;
        this.userService = userService;
        this.pizzaService = pizzaService;
    }

    @GetMapping("/list")
    @Secured("ROLE_ADMIN")
    String getAll(Model model) {
        model.addAttribute("favList", favouritesService.getAll());
        return "fav-list";
    }

//    @GetMapping("/{favouritesId}")
//    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
//    String get(@PathVariable Long favouritesId, Model model) {
//        model.addAttribute("fav", favouritesService.get(favouritesId));
//        return "fav-details";
//    }

    @GetMapping("/{userId}/add")
    @PreAuthorize("@authComponent.isAccountOwner(#userId) or hasRole('ROLE_ADMIN')")
    public String add(@PathVariable Long userId, Model model) {
        var user = userService.get(userId);
        model.addAttribute("user", user);

        var fav = new Favourites();
        fav.setPizzas(new ArrayList<>());
        fav.setClient(user);

        favouritesService.add(fav);
        return "redirect:/user/name/" + user.getUserName();
    }

    @GetMapping("/{userId}/delete/{favouritesId}")
    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable Long userId, @PathVariable Long favouritesId, Model model) {
        var user = userService.get(userId);
        model.addAttribute("user", user);
        favouritesService.delete(favouritesId);

        return "redirect:/user/name/" + user.getUserName();
    }

    @GetMapping("/{favouritesId}/pizza-add/{pizzaId}")
    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
    public String addPizza(@PathVariable Long favouritesId, @PathVariable Long pizzaId, Model model) {
        favouritesService.addPizza(favouritesId, pizzaId);
        model.addAttribute("pizzaList", pizzaService.getAllByPriceAsc());

        return "redirect:/pizza/list";
    }

    @GetMapping("/{favouritesId}/pizza-remove/{pizzaId}")
    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
    public String removePizza(@PathVariable Long favouritesId, @PathVariable Long pizzaId, Model model) {
        favouritesService.removePizza(favouritesId, pizzaId);
        model.addAttribute("pizzaList", pizzaService.getAllByPriceAsc());

        return "redirect:/pizza/list";
    }
}
