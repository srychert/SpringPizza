package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.srychert.SpringPizza.domain.Pizza;

@Repository
public interface PizzaRepository extends CrudRepository<Pizza, Long> {
}
