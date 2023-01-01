package pl.srychert.SpringPizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "order", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany(mappedBy = "orders")
//    @NotNull
    private List<Pizza> pizzas;

    @ManyToOne(cascade = CascadeType.ALL)
//    @NotNull
    private User client;

    public Order(List<Pizza> pizzas, User client) {
        this.pizzas = pizzas;
        this.client = client;
    }

    public Order() {
    }
}
