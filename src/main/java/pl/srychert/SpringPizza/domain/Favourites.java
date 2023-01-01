package pl.srychert.SpringPizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "favourites", schema = "public")
public class Favourites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany(mappedBy = "favourites")
    @NotNull
    private List<Pizza> pizzas;

    @OneToOne(mappedBy = "favourites")
    @NotNull
    private User client;

    public Favourites(List<Pizza> pizzas, User client) {
        this.pizzas = pizzas;
        this.client = client;
    }

    public Favourites() {
    }
}
