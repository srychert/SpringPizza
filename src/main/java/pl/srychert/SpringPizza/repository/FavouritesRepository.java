package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import pl.srychert.SpringPizza.domain.Favourites;

public interface FavouritesRepository extends CrudRepository<Favourites, Long> {
}
