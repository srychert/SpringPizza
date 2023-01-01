package pl.srychert.SpringPizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.srychert.SpringPizza.validation.Capitalized;
import pl.srychert.SpringPizza.validation.TailLowerCase;

import java.math.BigDecimal;
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

    @NotNull
    @Column
    @Min(0)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    @JsonIgnore
    @ManyToMany(mappedBy = "pizzas")
    private List<Order> orders;

    @JsonIgnore
    @ManyToMany(mappedBy = "pizzas")
    private List<Favourites> favourites;

    public Pizza(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Pizza() {
    }
}
