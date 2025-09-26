package com.katzenklappe.smartHome.config;

import com.katzenklappe.smartHome.Services.AuthenticationFilter;
import com.katzenklappe.smartHome.Services.AuthenticationService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public void registerAuthProvider(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests((auth) -> auth.requestMatchers(HttpMethod.GET, "/light/*")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/light/*")
                .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }

    //@Bean
    //public AuthenticationFilter authenticationFilter(AuthenticationService authenticationService) {
    //    return new AuthenticationFilter(authenticationService);
    //}
//
    //@Bean
    //public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationService authenticationService) throws Exception {
    //    http.csrf(AbstractHttpConfigurer::disable)
    //            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/**").authenticated())
    //            .httpBasic(Customizer.withDefaults())
    //            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //            .addFilterBefore(new AuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);
//
    //    return http.build();
    //}

}