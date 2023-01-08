package pl.srychert.SpringPizza;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.repository.UserRepository;
import pl.srychert.SpringPizza.service.PizzaService;
import pl.srychert.SpringPizza.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class SpringPizzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPizzaApplication.class, args);
    }

    @Bean
    public CommandLineRunner setupApp(PizzaService pizzaService, PizzaRepository pizzaRepository,
                                      UserRepository userRepository, UserService userService) {
        return (args) -> {
            Pizza pizza = new Pizza("Pepperoni", new BigDecimal(25));

            if (pizzaRepository.findByName("Pepperoni").isEmpty()) {
                pizzaService.add(pizza);
            }

            User user = new User("user",
                    "pass",
                    List.of("ROLE_USER"));

            if (userRepository.findByUserName("user").isEmpty()) {
                userService.addWithRoles(user);
            }

            User admin = new User("admin",
                    "pass",
                    List.of("ROLE_USER", "ROLE_ADMIN"));

            if (userRepository.findByUserName("admin").isEmpty()) {
                userService.addWithRoles(admin);
            }
        };
    }

}
