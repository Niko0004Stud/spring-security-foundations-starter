package ek.osnb.demo.security;

import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/hello").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/notes").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    /*@Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("pw"))
                .roles("USER")
                .build();

        UserDetails user1 = User.builder()
                .username("user1")
                .password(encoder.encode("pw"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("pw"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, user1, admin);
    }*/
}
