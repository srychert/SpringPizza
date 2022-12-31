package pl.srychert.SpringPizza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import pl.srychert.SpringPizza.auth.CustomAuthenticationProvider;

@Configuration
public class SecurityConfig {
    @Autowired
    private final CustomAuthenticationProvider authenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired
    public void setupAuthenticationProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                            auth
                                    .requestMatchers("/api/pizza/**").hasRole("ADMIN")
                                    .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "USER")
                                    .anyRequest().authenticated();
//                            .anyRequest().permitAll();
                        }
                );
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
