package com.katzenklappe.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails adminUser = User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, adminUser);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(form -> form
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
                )
                .httpBasic(httpBasic -> {})
                .authorizeExchange(auth -> auth
                        .pathMatchers("/", "/public", "/login").permitAll()
                        .pathMatchers("/eureka/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .logout(logout -> {}) // default logout
                .build();
    }
}
