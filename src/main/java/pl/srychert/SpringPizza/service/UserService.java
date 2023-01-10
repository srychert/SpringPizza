package pl.srychert.SpringPizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.exception.ApiRequestException;
import pl.srychert.SpringPizza.repository.UserRepository;

import java.util.*;

@Service
@Transactional
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User get(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));
    }

    public User getByUserName(String userName) {
        return userRepository
                .findByUserName(userName)
                .orElseThrow(() -> new ApiRequestException("No such User userName in DB"));
    }

    public User add(User user) {
        checkForDuplicates(user);
        User newUser = new User(
                user.getUserName(),
                passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newUser);
    }

    public User addWithRoles(User user) {
        checkForDuplicates(user);
        User newUser = new User(
                user.getUserName(),
                passwordEncoder.encode(user.getPassword()),
                user.getRoles());

        return userRepository.save(newUser);
    }

    public User delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        }
        return user.orElseThrow(() -> new ApiRequestException("No such User id in DB"));
    }

    public User update(Long id, User user) {
        User updatedUser = userRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        String userName = user.getUserName();

        if (!userName.equals(updatedUser.getUserName())) {
            checkForDuplicateUserName(userName);
            updatedUser.setUserName(userName);
        }

        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(updatedUser);
    }

    public User updateRoles(Long id, List<String> roles) {
        // check necessary because user could send empty body through controller
        if (roles == null) {
            throw new ApiRequestException("roles can't be null");
        }
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("No such User id in DB"));

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void checkForDuplicates(User user) {
        checkForDuplicateUserName(user.getUserName());
    }

    public void checkForDuplicateUserName(String username) {
        if (userRepository.findByUserName(username).isPresent()) {
            throw new ApiRequestException("Duplicate userName field");
        }
    }

    public List<String> getAllRoles() {
        return List.of("ROLE_USER", "ROLE_ADMIN");
    }

    public HashMap<Long, Integer> countPizzaOccurrencesInOrders(User user) {
        var list = user.getOrders()
                .stream().map(order -> order.getPizzas().stream().map(Pizza::getId).toList())
                .flatMap(List::stream).toList();

        Set<Long> distinct = new HashSet<>(list);
        var occurrences = new HashMap<Long, Integer>();
        for (Long l : distinct) {
            occurrences.put(l, Collections.frequency(list, l));
        }

        return occurrences;
    }

}