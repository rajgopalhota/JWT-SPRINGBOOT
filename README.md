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
## Conclusion

This project provides a simple implementation of JWT-based authentication in a Spring Boot application. The key components include a custom JWT authentication filter, security configuration, and the ability to access protected routes with a valid token. This architecture can be easily extended to handle additional security features such as role-based access control and token refresh mechanisms.

---