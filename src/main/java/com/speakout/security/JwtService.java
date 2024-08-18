package com.speakout.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  
  @Value("${security.jwt.access-token-secret-key}")
  private String accessTokenSecretKey;
  
  @Value("${security.jwt.refresh-token-secret-key}")
  private String refreshTokenSecretKey;
  
  @Getter
  @Value("${security.jwt.access-token-expiration}")
  private long accessTokenExpiration;
  
  @Getter
  @Value("${security.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;
  
  public String generateAccessToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, accessTokenExpiration, accessTokenSecretKey);
  }
  
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration, refreshTokenSecretKey);
  }
  
  public <T> T extractClaims(String token, Function<Claims, T> resolverClaims, boolean isAccessToken) {
    Claims claims = extractAllClaimsFromToken(token, isAccessToken);
    return resolverClaims.apply(claims);
  }
  
  public String extractUsername(String token, boolean isAccessToken) {
    return extractClaims(token, Claims::getSubject, isAccessToken);
  }
  
  private String buildToken(Map<String, Object> extrasClaims, UserDetails userDetails, long expiration, String secretKey) {
    return Jwts.builder()
        .claims(extrasClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignKey(secretKey), Jwts.SIG.HS256)
        .compact();
  }
  
  private boolean isTokenExpired(String token, boolean isAccessToken) {
    return extractExpiration(token, isAccessToken).before(new Date());
  }
  
  public Date extractExpiration(String token, boolean isAccessToken) {
    return extractClaims(token, Claims::getExpiration, isAccessToken);
  }
  
  private Claims extractAllClaimsFromToken(String token, boolean isAccessToken) {
    return Jwts.parser()
        .verifyWith(getSignKey(isAccessToken ?  accessTokenSecretKey : refreshTokenSecretKey))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
  
  private SecretKey getSignKey(String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
  
}
