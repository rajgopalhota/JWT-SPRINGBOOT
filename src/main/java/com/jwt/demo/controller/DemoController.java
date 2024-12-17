package com.jwt.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    // Protected route, only accessible with a valid JWT token
    @GetMapping("/protected-route")
    public String protectedRoute(@RequestHeader("Authorization") String token) {
        // Extract and validate the token (in reality, you would use a service to validate the JWT)
        // You can also use @RequestHeader to capture the token for validation or logging
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "Access granted to protected route. Token received from user: " + username;
    }
}
