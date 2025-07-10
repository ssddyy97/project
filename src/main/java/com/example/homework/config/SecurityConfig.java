package com.example.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    

    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in this example, consider enabling in production
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Allow H2 console to be embedded in a frame
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // Allow access to common static resources
                .requestMatchers(new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/register"),
                        new AntPathRequestMatcher("/main"),
                        new AntPathRequestMatcher("/intro"),
                        new AntPathRequestMatcher("/schedule"),
                        new AntPathRequestMatcher("/clips"),
                        new AntPathRequestMatcher("/podcast"),
                        new AntPathRequestMatcher("/contact"),
                        new AntPathRequestMatcher("/sns"),
                        new AntPathRequestMatcher("/notices"),
                        new AntPathRequestMatcher("/api/videos"),
                        new AntPathRequestMatcher("/api/podcasts"),
                        new AntPathRequestMatcher("/static/**")).permitAll() // Allow public access to these paths and static resources
                .anyRequest().authenticated() // All other requests require authentication
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/", true) // Redirect to / after successful OAuth2 login
                .failureUrl("/login?error") // Redirect to login page on failure
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/", true) // Redirect to / after successful form login
                .failureHandler(customAuthenticationFailureHandler) // Use custom failure handler
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                .permitAll()
            );
        return http.build();
    }
}