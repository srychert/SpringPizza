package pl.srychert.SpringPizza.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "order", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    @Size(min = 1)
    private List<Pizza> pizzas;

    @ManyToOne
    @NotNull
    private User client;

    public Order() {
    }

    public BigDecimal getTotal() {
        if (pizzas == null) {
            return new BigDecimal(0);
        }

        return pizzas.stream().map(Pizza::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
