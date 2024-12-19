### `README.md`

# JWT Authentication in Spring Boot

## Overview

This repository demonstrates how to integrate JWT (JSON Web Token) authentication in a Spring Boot application. The JWT token is used to authenticate users and grant access to protected routes. A custom filter is used to extract the token from the `Authorization` header, validate it, and authenticate the user.

## Key Features

- **JWT Authentication**: Protect routes with JWT token authentication.
- **Custom JWT Filter**: Automatically extracts and validates the JWT token.
- **Spring Security**: Uses Spring Security for managing authentication and authorization.

---

## Key Dependencies

The project uses the following dependencies to handle JWT authentication and security:

1. **Spring Boot Starter Web**: Provides essential components for building RESTful web applications.
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```

2. **Spring Boot Starter Security**: Adds support for securing the application using Spring Security.
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```

3. **JSON Web Token (JWT)**: Used to create, parse, and validate JWT tokens.
    ```xml
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.11.5</version>
    </dependency>
    ```

4. **Spring Boot Starter Logging**: Provides logging support, which is helpful for debugging JWT token validation.
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
    </dependency>
    ```

5. **JAXB API (Optional)**: If you encounter errors related to XML binding or JAXB (e.g., `javax.xml.bind` class not found), you will need to add the following dependency:
    ```xml
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.1</version>
    </dependency>
    ```

---

## Key Concepts and Components

### 1. **JWT Authentication Filter**

The `JwtAuthenticationFilter` intercepts HTTP requests, extracts the JWT token from the `Authorization` header, validates it, and sets the authentication in the `SecurityContext`.

- The filter extracts the token from the `Authorization` header (e.g., `Bearer <token>`).
- It validates the token using the secret key and then retrieves the username (subject).
- The username is then set in the Spring Security context to authenticate the user.

### 2. **Security Configuration**

In the `SecurityConfig` class, we configure Spring Security to use the `JwtAuthenticationFilter` to authenticate users before accessing protected routes.

- Disable CSRF protection for the purpose of this demo (you can enable it for production apps).
- The `/protected-route` is secured and requires authentication.

### 3. **SecurityContextHolder**

Once the JWT token is validated, the username is set in the `SecurityContext`, and any controller can access the username using `SecurityContextHolder.getContext().getAuthentication().getPrincipal()`.

### 4. **Protected Route**

The `/protected-route` endpoint requires a valid JWT token to access. If the token is invalid or missing, the request will be rejected.

---

## Important Notes

### 1. **JWT Token Handling**
- **Token Format**: The JWT token must be passed in the `Authorization` header in the format: `Bearer <token>`.
- **Token Expiry**: If using an expiration date for your token, make sure to handle token expiration in your filter logic and return appropriate HTTP error codes (e.g., `401 Unauthorized`).
- **Secure Secret Key**: The secret key used to sign and validate the token must be kept secret and not hardcoded in the codebase. You can use environment variables or configuration files to store it securely.

### 2. **Spring Security Configuration**
- Ensure that you disable CSRF if you're using JWT tokens, as the token authentication is stateless and CSRF protection is typically used for session-based authentication.
- If you need more sophisticated security, like roles and permissions, you can enhance the filter to include role-based checks.

### 3. **Error Handling**
- Ensure proper error handling when the token is invalid, expired, or missing.
- The `JwtAuthenticationFilter` should clear the `SecurityContextHolder` if the token is invalid to prevent access.

### 4. **JAXB API Dependency**
- If you encounter errors related to missing XML binding classes (e.g., `javax.xml.bind`), such as the following error:
  ```
  ClassNotFoundException: javax.xml.bind.JAXBException
  ```
  Add the following dependency to your `pom.xml`:
  ```xml
  <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.1</version>
  </dependency>
  ```
- This is common when running the project on JDK 9+ because JAXB was removed from the standard library in JDK 9.

### 5. **Testing**
- Always test the JWT flow using tools like Postman or curl to ensure that token-based authentication works as expected.
- Add unit tests to ensure your filter logic and security configuration are correctly implemented.

### 6. **Logging**
- The `SecurityContext` stores the authenticated user's details. If you're troubleshooting authentication issues, logging the details in the filter will help trace issues like token validation or missing tokens.

---

## Day 2: Enhancements and API Design

### Enhancements Made

1. **Enhanced JWT Payload**:
    - Added `accountNumber` field along with the username in the JWT payload.

2. **Additional Routes**:
    - Designed multiple routes supporting different request types: GET (with query parameters), POST (with a request body), and PUT (with path variables).

3. **Unified User Data Access**:
    - Each route now extracts and returns the authenticated username, account number, and route-specific data.

### Updated JWT Utility Methods

| Method              | Description                                                 |
|---------------------|-------------------------------------------------------------|
| `generateToken()`   | Generates a token containing both `username` and `accountNumber`. |
| `extractUsername()` | Extracts the `username` from the token.                     |
| `extractAccountNum()` | Extracts the `accountNumber` from the token.               |

### Controller Methods

| HTTP Method | Endpoint                 | Request Type        | Parameters              | Description                                                |
|-------------|--------------------------|---------------------|-------------------------|------------------------------------------------------------|
| GET         | `/api/get-with-params`  | Query Parameters    | `param1`, `param2`      | Returns username, accountNumber, and provided query params.|
| POST        | `/api/post-with-body`   | JSON Request Body   | `key`, `value`          | Returns username, accountNumber, and body content.         |
| PUT         | `/api/put-with-path/{id}` | Path Variable + Body | `id`, `updateField`    | Returns username, accountNumber, and updated content.      |

### Example Controller Implementation

```java
@RestController
@RequestMapping("/api")
public class ApiController {

    private final JwtUtil jwtUtil;

    public ApiController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/get-with-params")
    public ResponseEntity<?> getWithParams(@RequestParam String param1, @RequestParam String param2, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token);
        String accountNum = jwtUtil.extractAccountNumber(token);
        return ResponseEntity.ok(Map.of(
            "username", username,
            "accountNumber", accountNum,
            "params", Map.of("param1", param1, "param2", param2)
        ));
    }

    @PostMapping("/post-with-body")
    public ResponseEntity<?> postWithBody(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token);
        String accountNum = jwtUtil.extractAccountNumber(token);
        return ResponseEntity.ok(Map.of(
            "username", username,
            "accountNumber", accountNum,
            "body", body
        ));
    }

    @PutMapping("/put-with-path/{id}")
    public ResponseEntity<?> putWithPath(@PathVariable String id, @RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token);
        String accountNum = jwtUtil.extractAccountNumber(token);
        return ResponseEntity.ok(Map.of(
            "username", username,
            "accountNumber", accountNum,
            "id", id,
            "body", body
        ));
    }
}
```

---

### Curl Commands

| HTTP Method | Command                                                                                                    |
|-------------|------------------------------------------------------------------------------------------------------------|
| GET         | `curl -X GET "http://localhost:8080/api/get-with-params?param1=value1&param2=value2" -H "Authorization: Bearer <token>"` |
| POST        | `curl -X POST "http://localhost:8080/api/post-with-body" -H "Content-Type: application/json" -H "Authorization: Bearer <token>" -d '{"key":"value"}'` |
| PUT         | `curl -X PUT "http://localhost:8080/api/put-with-path/101" -H "Content-Type: application/json" -H "Authorization: Bearer <token>" -d '{"updateField":"newValue"}'` |

---

### Day 03: Notes and Updates

#### Key Changes

1. **Dependency Updates**:
    - Removed unnecessary dependencies (`jaxb-api`) since they are no longer required for this project.
    - Updated key dependencies to the latest versions:
        - **Spring Boot Starter Security**: Provides enhanced security features.
        - **Spring Boot Starter Web**: For RESTful APIs.
        - **jjwt-api**: Updated to the latest version for better support and features.

   Updated `pom.xml` dependencies:
   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-security</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-api</artifactId>
           <version>0.11.5</version>
       </dependency>
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-impl</artifactId>
           <version>0.11.5</version>
           <scope>runtime</scope>
       </dependency>
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-jackson</artifactId>
           <version>0.11.5</version>
           <scope>runtime</scope>
       </dependency>
   </dependencies>
   ```

2. **Code Updates**:
    - Updated the `JwtUtil` class to address deprecated methods from the older `jjwt` library.
    - Replaced old method calls with newer ones that align with the updated `jjwt` library.

#### Highlights of `JwtUtil` Changes

1. **Key Enhancements**:
    - Introduced the use of `Jwts.builder()` and `Jwts.parser()` with the newer APIs.
    - Updated the signing and verification methods using `SecretKey` and `Keys` utility from `jjwt`.

2. **Updated Methods**:
    - **`generateToken`**: Generates JWT tokens with additional claims, ensuring compatibility with the latest library features.
    - **`validateToken`**: Handles validation for token expiration, malformed tokens, and signature verification.
    - **`extractUsername` and `extractAccountNumber`**: Simplified extraction using new claim resolution patterns.
    - **`getSignInKey`**: Generates the signing key securely using `Decoders.BASE64` and `Keys.hmacShaKeyFor`.

3. **Enhanced Validation**:
    - Added better error handling for scenarios such as expired tokens, malformed tokens, and unsupported formats.

#### Notes for Future Enhancements

1. **Environment Variables for Secrets**:
    - Move the `SECRET_KEY` to an environment variable or a secure vault for production use.

2. **Role-Based Authorization**:
    - Extend `JwtUtil` and the security filter to support role-based claims for user access control.

3. **Token Refresh Mechanism**:
    - Add an endpoint for refreshing tokens when nearing expiration.

4. **Logging**:
    - Integrate structured logging for debugging token-related issues in production.

---
## Conclusion

This project provides a simple implementation of JWT-based authentication in a Spring Boot application. The key components include a custom JWT authentication filter, security configuration, and the ability to access protected routes with a valid token. This architecture can be easily extended to handle additional security features such as role-based access control and token refresh mechanisms.

---