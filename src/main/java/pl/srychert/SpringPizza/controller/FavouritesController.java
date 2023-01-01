package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Favourites delete(@PathVariable Long favouritesId) {
        return favouritesService.delete(favouritesId);
    }

    @PutMapping("/{favouritesId}")
    public Favourites update(@PathVariable Long favouritesId, @Valid @RequestBody Favourites favourites) {
        return favouritesService.update(favouritesId, favourites);
    }
}
