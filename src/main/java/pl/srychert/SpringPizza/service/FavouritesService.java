package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Favourites;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.FavouritesRepository;

import java.util.Optional;

@Service
@Transactional
public class FavouritesService {
    final FavouritesRepository favouritesRepository;

    public FavouritesService(FavouritesRepository favouritesRepository) {
        this.favouritesRepository = favouritesRepository;
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
        var favouritesToAdd = new Favourites(favourites.getPizzas(), favourites.getClient());
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

        favourites.setPizzas(updatedFavourites.getPizzas());
        favourites.setClient(updatedFavourites.getClient());

        return favouritesRepository.save(favourites);
    }
}
