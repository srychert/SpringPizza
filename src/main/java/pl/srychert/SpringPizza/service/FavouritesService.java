package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
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
        var favourites = favouritesRepository.findById(id)
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
}
