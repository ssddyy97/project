package com.example.homework.config;

import com.example.homework.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username"); // Assuming username is the parameter name for the ID field
        String errorMessage;

        if (exception instanceof BadCredentialsException) {
            // Check if the username exists
            if (userService.findUserByUsername(username).isEmpty()) {
                errorMessage = "usernameNotFound"; // User ID does not exist
            } else {
                errorMessage = "badCredentials"; // Password error
            }
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // This often happens if the UserDetailsService cannot find the user
            errorMessage = "usernameNotFound";
        } else {
            errorMessage = "unknownError"; // Other authentication errors
        }

        response.sendRedirect("/login?error=" + errorMessage);
    }
}