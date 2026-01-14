package com.podflix.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // IMPORTANT: In a real production app, store this in environment
    // variables/properties
    // The key needs to be at least 256 bits (32 bytes) for HS256
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private Key getSignKey() {
        byte[] keyBytes = hexStringToByteArray(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Helper to decode hex string if you use a hex secret, or just use Bytes.wrap
    // if raw string
    // ideally use Decoders.BASE64 if it's base64 encoded.
    // For simplicity here assume the secret above is a Hex string of a key or just
    // a long random string.
    // Let's rely on Keys.hmacShaKeyFor to handle bytes.
    // To match common examples, let's just use a strong literal string and get
    // bytes.

    private Key getSignInKey() {
        // Use a secure random key for demo or load from properties
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, int tokenVersion) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", tokenVersion);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        // Validate Token Version
        Integer tokenVersion = extractClaim(token, claims -> claims.get("tokenVersion", Integer.class));
        if (userDetails instanceof CustomUserDetails) {
            if (tokenVersion == null || tokenVersion != ((CustomUserDetails) userDetails).getTokenVersion()) {
                return false;
            }
        }

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Helper for Hex if needed, but getBytes() is simpler for basic string secrets
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
