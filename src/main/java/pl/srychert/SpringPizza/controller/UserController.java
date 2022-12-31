package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> getAll() {
        return userService.getAll();
    }

    @GetMapping(path = "{userId}")
    public Optional<User> get(@PathVariable("userId") Long id) {
        return userService.get(id);
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @DeleteMapping(path = "{userId}")
    public User deleteUser(@PathVariable("userId") Long id) {
        return userService.delete(id);
    }

    @PutMapping(path = "{userId}")
    public User update(
            @PathVariable("userId") Long id, @Valid @RequestBody User user) {
        return userService.update(id, user);
    }

    @PatchMapping(path = "{userId}/roles")
    public User updateRoles(@PathVariable("userId") Long id, @RequestBody HashMap<String, List<String>> roles) {
        return userService.updateRoles(id, roles.get("roles"));
    }
}
