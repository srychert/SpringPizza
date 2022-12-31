package pl.srychert.SpringPizza.repository;

import org.springframework.data.repository.CrudRepository;
import pl.srychert.SpringPizza.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
