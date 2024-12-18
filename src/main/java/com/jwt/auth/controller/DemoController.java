//package com.jwt.auth.controller;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class DemoController {
//
//    // Protected route, only accessible with a valid JWT token
//    @GetMapping("/protected-route")
//    public String protectedRoute(@RequestHeader("Authorization") String token) {
//        // Extract and validate the token (in reality, you would use a service to validate the JWT)
//        // You can also use @RequestHeader to capture the token for validation or logging
//        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        return "Access granted to protected route. Token received from user: " + username;
//    }
//}

//For claims
package com.jwt.auth.controller;

import com.jwt.auth.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final JwtUtil jwtUtil;

    public DemoController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Extract username and accountNumber from token
    private Map<String, String> extractUserDetails(String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String username = jwtUtil.extractUsername(token);
        String accountNumber = jwtUtil.extractAccountNumber(token); // Assume this method exists in JwtUtil

        return Map.of("username", username, "accountNumber", accountNumber);
    }

    // GET Request with Query Parameters
    @GetMapping("/get-with-params")
    public Map<String, Object> getWithParams(
            @RequestHeader("Authorization") String token,
            @RequestParam String param1,
            @RequestParam(required = false) String param2) {

        Map<String, String> userDetails = extractUserDetails(token);
        return Map.of(
                "message", "GET request received",
                "userDetails", userDetails,
                "param1", param1,
                "param2", param2 != null ? param2 : "not provided"
        );
    }

    // POST Request with Request Body
    @PostMapping("/post-with-body")
    public Map<String, Object> postWithBody(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {

        Map<String, String> userDetails = extractUserDetails(token);
        return Map.of(
                "message", "POST request received",
                "userDetails", userDetails,
                "body", body
        );
    }

    // PUT Request with Path Variables and Request Body
    @PutMapping("/put-with-path/{id}")
    public Map<String, Object> putWithPath(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id,
            @RequestBody Map<String, String> body) {

        Map<String, String> userDetails = extractUserDetails(token);
        return Map.of(
                "message", "PUT request received",
                "userDetails", userDetails,
                "pathId", id,
                "body", body
        );
    }

    // DELETE Request with Query Parameters
    @DeleteMapping("/delete-with-params")
    public Map<String, Object> deleteWithParams(
            @RequestHeader("Authorization") String token,
            @RequestParam String id) {

        Map<String, String> userDetails = extractUserDetails(token);
        return Map.of(
                "message", "DELETE request received",
                "userDetails", userDetails,
                "deletedRecordId", id
        );
    }

}
