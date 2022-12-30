package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.srychert.SpringPizza.domain.Pizza;

import java.util.Optional;

@Repository
public interface PizzaRepository extends CrudRepository<Pizza, Long> {
    Optional<Pizza> findByName(String name);
}
