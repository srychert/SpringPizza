package pl.srychert.SpringPizza.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String userName;
    @NotBlank
    private String password;
    private List<String> roles;

    public User(String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.roles = List.of("USER");
    }

    public User() {
    }
}
