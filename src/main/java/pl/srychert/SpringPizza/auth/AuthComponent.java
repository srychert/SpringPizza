package pl.srychert.SpringPizza.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.repository.UserRepository;

import java.util.Optional;

@Component
public class AuthComponent {
    @Autowired
    private final UserRepository userRepository;

    public AuthComponent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAccountOwner(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        
        Optional<User> user = userRepository.findByUserName(name);
        Long userId = user.map(User::getId).orElse(null);

        return id.equals(userId);
    }
}
