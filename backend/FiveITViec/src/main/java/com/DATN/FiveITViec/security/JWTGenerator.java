package com.DATN.FiveITViec.security;

import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    private UserRepository userRepository;
    
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        String token = getToken(email);
        return token;
    }



    public String generateTokenForDifferentLogin(User user){
        String token = getToken(user.getEmail());
        return token;
    }


    private String getToken(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null || user.getUserId() <= 0) {
            throw new AuthenticationCredentialsNotFoundException("User not found");
        }

        String userRole = user.getRoles().getRoleName(); // Assuming roles are stored in the user object
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", userRole) // Add the role as a claim
                .claim("email", email) // Add the email as a claim
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println("New token :");
        System.out.println(token);
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
           return false;
        }
    }

}
