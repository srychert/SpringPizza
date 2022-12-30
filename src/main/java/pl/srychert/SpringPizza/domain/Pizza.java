package pl.srychert.SpringPizza.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String name;

    public Pizza(String name) {
        this.name = name;
    }

    public Pizza() {
    }
}
