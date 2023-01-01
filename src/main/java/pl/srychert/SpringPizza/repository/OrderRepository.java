package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import pl.srychert.SpringPizza.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
