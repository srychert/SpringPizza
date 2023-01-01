package pl.srychert.SpringPizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.srychert.SpringPizza.domain.Pizza;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findByName(String name);

    List<Pizza> findAllByOrderByPriceAsc();

    List<Pizza> findAllByOrderByPriceDesc();
}
