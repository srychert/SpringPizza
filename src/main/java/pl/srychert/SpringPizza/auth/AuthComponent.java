package pl.srychert.SpringPizza.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.srychert.SpringPizza.domain.Favourites;
import pl.srychert.SpringPizza.domain.Order;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.repository.FavouritesRepository;
import pl.srychert.SpringPizza.repository.OrderRepository;
import pl.srychert.SpringPizza.repository.UserRepository;

import java.util.Optional;

@Component
public class AuthComponent {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final FavouritesRepository favouritesRepository;

    public AuthComponent(UserRepository userRepository,
                         OrderRepository orderRepository,
                         FavouritesRepository favouritesRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.favouritesRepository = favouritesRepository;
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        Optional<User> user = userRepository.findByUserName(name);
        return user.map(User::getId).orElse(null);
    }

    public boolean isAccountOwner(Long id) {
        Long userId = getUserId();

        return id.equals(userId);
    }

    public boolean isOrderOwner(Long orderId) {
        Long userId = getUserId();

        Optional<Order> order = orderRepository.findById(orderId);
        Optional<Long> clientId = order.map(o -> o.getClient().getId());

        return clientId.map(id -> id.equals(userId)).orElse(false);
    }

    public boolean isFavouritesOwner(Long favouritesId) {
        Long userId = getUserId();

        Optional<Favourites> favourites = favouritesRepository.findById(favouritesId);
        Optional<Long> clientId = favourites.map(o -> o.getClient().getId());

        return clientId.map(id -> id.equals(userId)).orElse(false);
    }
}
