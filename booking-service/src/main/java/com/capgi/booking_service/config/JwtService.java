package com.capgi.booking_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.security.SignatureException;
import java.util.function.Function;

@Service
public class JwtService {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	private final String SECRET_KEY = "mySuperSecureSecretKeyThatIsAtLeast32Chars"; // should be at least 256-bit key for HS256

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
    	try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            logger.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    public boolean isTokenValid(String token) {
    	try {
            extractAllClaims(token);
            return true;
        } 
    	catch (ExpiredJwtException e) {
            logger.warn("Token expired: {}", e.getMessage());
        } 
    	catch (UnsupportedJwtException | MalformedJwtException e) {
            logger.warn("Invalid token: {}", e.getMessage());
        } 
    	catch (Exception e) {
            logger.error("Unexpected error during token validation: {}", e.getMessage());
        }
        return false;
    }


    private Claims extractAllClaims(String token) {
        try {
        	JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build();

            return parser
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (JwtException e) {
            logger.warn("JWT parsing error: {}", e.getMessage());
            throw e;
        }
    }
}


















