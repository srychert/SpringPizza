package pl.srychert.SpringPizza.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pl.srychert.SpringPizza.validation.Capitalized;
import pl.srychert.SpringPizza.validation.TailLowerCase;

@Entity
@Getter
@Setter
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Capitalized
    @TailLowerCase
    @Column(unique = true)
    private String name;

    public Pizza(String name) {
        this.name = name;
    }

    public Pizza() {
    }
}
