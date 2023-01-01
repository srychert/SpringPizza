package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import pl.srychert.SpringPizza.domain.Favourites;

import java.util.Optional;

public interface FavouritesRepository extends CrudRepository<Favourites, Long> {
    Optional<Favourites> findByClientId(Long id);
}
