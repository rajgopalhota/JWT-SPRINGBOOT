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
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "zeinekhnQP5icBDJNHjMb+sGrAUc5ZoNhs5WNl91zF4="; // Replace with your secure key
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    /**
     * Generates a JWT token with additional claims.
     *
     * @param claims The claims to be included in the token.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getSignInKey(), Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            System.err.println("Error while generating token: " + e.getMessage());
            e.printStackTrace(System.out);
            return null; // Handle or log this error as needed
        }
    }

    /**
     * Extracts the username from the token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    /**
     * Extracts the account number from the token.
     *
     * @param token The JWT token.
     * @return The account number.
     */
    public String extractAccountNumber(String token) {
        return extractClaim(token, claims -> claims.get("accountNum", String.class));
    }

    /**
     * Validates the token for expiration and signature correctness.
     *
     * @param token The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
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
     *
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the claim.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token The JWT token.
     * @return All claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Returns the secret key for signing JWT tokens.
     *
     * @return The secret key.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
