//package com.jwt.auth.controller;
//
//import com.jwt.auth.utils.JwtUtil;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final JwtUtil jwtUtil;
//
//    public AuthController(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        // Replace with actual user authentication logic
//        if ("admin".equals(username) && "password".equals(password)) {
//            return jwtUtil.generateToken(username);
//        } else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
//}

// New controller to have multiple vals in JWT
package com.jwt.auth.controller;

import com.jwt.auth.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Replace with actual user authentication logic
        if ("admin".equals(username) && "password".equals(password)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("accountNum", "123456789"); // Replace with actual account number
            return jwtUtil.generateToken(claims);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}

