package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Favourites;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.FavouritesRepository;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FavouritesService {
    final FavouritesRepository favouritesRepository;
    final UserRepository userRepository;
    final PizzaRepository pizzaRepository;

    public FavouritesService(FavouritesRepository favouritesRepository,
                             UserRepository userRepository,
                             PizzaRepository pizzaRepository) {
        this.favouritesRepository = favouritesRepository;
        this.userRepository = userRepository;
        this.pizzaRepository = pizzaRepository;
    }


    public Iterable<Favourites> getAll() {
        return favouritesRepository.findAll();
    }

    public Favourites get(Long id) {
        return favouritesRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Favourites id in DB"));
    }

    public Favourites add(Favourites favourites) {

        Long clientId = favourites.getClient().getId();

        favouritesRepository.findByClientId(clientId).ifPresent(fav -> {
            throw new ApiRequestException("Favourites with this client id already exists");
        });

        User client = userRepository
                .findById(clientId)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        List<Long> pizzaIds = favourites.getPizzas().stream().map(Pizza::getId).toList();
        List<Pizza> pizzas = (List<Pizza>) pizzaRepository.findAllById(pizzaIds);

        Favourites favouritesToAdd = new Favourites();
        favouritesToAdd.setClient(client);
        favouritesToAdd.setPizzas(pizzas);

        return favouritesRepository.save(favouritesToAdd);
    }

    public Favourites delete(Long id) {
        Optional<Favourites> favourites = favouritesRepository.findById(id);
        if (favourites.isPresent()) {
            favouritesRepository.deleteById(id);
        }
        return favourites.orElseThrow(() -> new ApiRequestException("No such Favourites id in DB"));
    }

    public Favourites update(Long id, Favourites updatedFavourites) {
        Favourites favourites = favouritesRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such Favourites id in DB"));

        Long clientId = updatedFavourites.getClient().getId();

        favouritesRepository.findByClientId(clientId).ifPresent(fav -> {
            throw new ApiRequestException("Favourites with this client id already exists");
        });

        User client = userRepository
                .findById(clientId)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        List<Long> pizzaIds = updatedFavourites.getPizzas().stream().map(Pizza::getId).toList();
        List<Pizza> pizzas = (List<Pizza>) pizzaRepository.findAllById(pizzaIds);

        favourites.setClient(client);
        favourites.setPizzas(pizzas);

        return favouritesRepository.save(favourites);
    }

    public Favourites addPizza(Long favouritesId, Long pizzaId) {
        Favourites favourites = favouritesRepository
                .findById(favouritesId)
                .orElseThrow(() -> new ApiRequestException("No such Favourites id in DB"));

        Pizza pizza = pizzaRepository
                .findById(pizzaId)
                .orElseThrow(() -> new ApiRequestException("No such Pizza id in DB"));

        @NotNull List<Pizza> updatedPizzas = favourites.getPizzas();
        boolean pizzaInFav = updatedPizzas.stream().map(Pizza::getId).toList().contains(pizzaId);
        if (!pizzaInFav) {
            updatedPizzas.add(pizza);
            favourites.setPizzas(updatedPizzas);
        }
        return favourites;
    }

    public Favourites removePizza(Long favouritesId, Long pizzaId) {
        Favourites favourites = favouritesRepository
                .findById(favouritesId)
                .orElseThrow(() -> new ApiRequestException("No such Favourites id in DB"));

        Pizza pizza = pizzaRepository
                .findById(pizzaId)
                .orElseThrow(() -> new ApiRequestException("No such Pizza id in DB"));

        @NotNull List<Pizza> updatedPizzas = favourites.getPizzas()
                .stream().filter(p -> !p.getId().equals(pizzaId)).toList();
        
        favourites.setPizzas(updatedPizzas);
        return favourites;
    }
}
