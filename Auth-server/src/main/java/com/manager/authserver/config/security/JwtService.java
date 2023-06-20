package com.manager.authserver.config.security;

import com.manager.authserver.models.User.UserModel;
import com.manager.authserver.repositories.TokenRepository;
import com.manager.authserver.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@Service
public class JwtService {

    private final String SECRET_KEY;

    private final TokenRepository tokenRepository;

    public JwtService(@Value("${jwt.secret.key}") String secretKey, TokenRepository tokenRepository) {
        this.SECRET_KEY = secretKey;
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(UserModel user) {
        Long userId = user.getId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userCpf", user.getCpf());
        return generateToken(claims, user.getEmail());
    }


    public String generateToken(Map<String, Object> extraClaims, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token) {

        return !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    public String extractUserCpf(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userCpf").toString();
    }

    private boolean isTokenExpired(String token) {
        Date expiryDate = extractExpiration(token);
        return expiryDate.before(new Date());
    }

    private Key getSigningKey() {
        byte[] secretBytes = Base64.getDecoder().decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}

