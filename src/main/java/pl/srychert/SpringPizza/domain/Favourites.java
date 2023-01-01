package pl.srychert.SpringPizza.domain;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    private List<Pizza> pizzas;

    @OneToOne
    @NotNull
    private User client;

    public Favourites() {
    }
}
