package br.com.osa.infrastructure.security.jwt;

import br.com.osa.port.security.TokenProviderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService implements TokenProviderPort {

  private final Key key;
  private final long expirationSeconds;

  public TokenService(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-seconds}") long expirationSeconds) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationSeconds = expirationSeconds;
  }

  @Override
  public String generateToken(String userId, String subject) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(userId)
        .setIssuer("osa-bank")
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .claim("login", subject)
        .signWith(key)
        .compact();
  }

  public String parseSubject(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
