//package com.jwt.auth.utils;
//
//import io.jsonwebtoken.*;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    private static final String SECRET_KEY = "your_secret_key";
//    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
//
//    /**
//     * Generates a JWT token for the provided username.
//     */
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }
//
//    /**
//     * Extracts the username from the token.
//     */
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    /**
//     * Validates the token for expiration and signature correctness.
//     */
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException e) {
//            System.out.println("Token expired");
//        } catch (MalformedJwtException e) {
//            System.out.println("Invalid token");
//        } catch (UnsupportedJwtException e) {
//            System.out.println("Token not supported");
//        } catch (IllegalArgumentException e) {
//            System.out.println("Token claims string is empty");
//        }
//        return false;
//    }
//
//    /**
//     * Extracts a specific claim from the token.
//     */
//    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    /**
//     * Extracts all claims from the token.
//     */
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//    }
//}


package com.jwt.auth.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    /**
     * Generates a JWT token with additional claims.
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the username from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    /**
     * Extracts the account number from the token.
     */
    public String extractAccountNumber(String token) {
        return extractClaim(token, claims -> claims.get("accountNum", String.class));
    }

    /**
     * Validates the token for expiration and signature correctness.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token not supported");
        } catch (IllegalArgumentException e) {
            System.out.println("Token claims string is empty");
        }
        return false;
    }

    /**
     * Extracts a specific claim from the token.
     */
    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
