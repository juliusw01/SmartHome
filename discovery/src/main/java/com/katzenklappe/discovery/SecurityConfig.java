package com.katzenklappe.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("discUser").password("{noop}discPassword").roles("SYSTEM");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/eureka/**").hasAnyRole("SYSTEM")
                        .requestMatchers(HttpMethod.POST, "/eureka/**").hasAnyRole("SYSTEM")
                        .requestMatchers(HttpMethod.PUT, "/eureka/**").hasAnyRole("SYSTEM")
                        .requestMatchers(HttpMethod.DELETE, "/eureka/**").hasAnyRole("SYSTEM")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();

    }

    @Configuration
    public static class AdminSecurityConfig {

        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.inMemoryAuthentication();
        }

        protected void configure(HttpSecurity http) throws Exception{
            http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers(HttpMethod.GET, "/").hasRole("ADMIN")
                            .requestMatchers("/info", "/health").authenticated()
                            .anyRequest().denyAll())
                    .csrf(AbstractHttpConfigurer::disable);
        }
    }
}
