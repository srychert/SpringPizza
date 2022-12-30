package pl.srychert.SpringPizza;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.srychert.SpringPizza.domain.Pizza;
import pl.srychert.SpringPizza.repository.PizzaRepository;
import pl.srychert.SpringPizza.service.PizzaService;

@SpringBootApplication
public class SpringPizzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPizzaApplication.class, args);
    }

    @Bean
    public CommandLineRunner setupApp(PizzaService pizzaService, PizzaRepository pizzaRepository) {
        return (args) -> {
            var pizza = new Pizza("Farmerska");
            pizzaService.add(pizza);
            var pizzaInDb = pizzaRepository.findByName(pizza.getName()).get();
            System.out.printf("Id: %s\nName: %s", pizzaInDb.getId(), pizzaInDb.getName());
        };
    }

}
