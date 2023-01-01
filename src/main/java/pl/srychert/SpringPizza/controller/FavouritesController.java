package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.Favourites;
import pl.srychert.SpringPizza.service.FavouritesService;

@RestController
@RequestMapping("/api/favourites")
public class FavouritesController {
    @Autowired
    private final FavouritesService favouritesService;

    public FavouritesController(FavouritesService favouritesService) {
        this.favouritesService = favouritesService;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public Iterable<Favourites> getAll() {
        return favouritesService.getAll();
    }

    @GetMapping("/{favouritesId}")
    public Favourites get(@PathVariable Long favouritesId) {
        return favouritesService.get(favouritesId);
    }

    @PostMapping
    public Favourites add(@Valid @RequestBody Favourites favourites) {
        return favouritesService.add(favourites);
    }

    @DeleteMapping("/{favouritesId}")
    @Secured("ROLE_ADMIN")
    public Favourites delete(@PathVariable Long favouritesId) {
        return favouritesService.delete(favouritesId);
    }

    @PutMapping("/{favouritesId}")
    @Secured("ROLE_ADMIN")
    public Favourites update(@PathVariable Long favouritesId, @Valid @RequestBody Favourites favourites) {
        return favouritesService.update(favouritesId, favourites);
    }

    @PutMapping("/{favouritesId}/pizza-add/{pizzaId}")
    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
    public Favourites addPizza(@PathVariable Long favouritesId, @PathVariable Long pizzaId) {
        return favouritesService.addPizza(favouritesId, pizzaId);
    }

    @PutMapping("/{favouritesId}/pizza-remove/{pizzaId}")
    @PreAuthorize("@authComponent.isFavouritesOwner(#favouritesId) or hasRole('ROLE_ADMIN')")
    public Favourites removePizza(@PathVariable Long favouritesId, @PathVariable Long pizzaId) {
        return favouritesService.removePizza(favouritesId, pizzaId);
    }
}
