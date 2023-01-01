package pl.srychert.SpringPizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pl.srychert.SpringPizza.validation.Capitalized;
import pl.srychert.SpringPizza.validation.TailLowerCase;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pizza", schema = "public")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Capitalized
    @TailLowerCase
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "pizzas")
    private List<Order> orders;

    @JsonIgnore
    @ManyToMany(mappedBy = "pizzas")
    private List<Favourites> favourites;

    public Pizza(String name) {
        this.name = name;
    }

    public Pizza() {
    }
}
