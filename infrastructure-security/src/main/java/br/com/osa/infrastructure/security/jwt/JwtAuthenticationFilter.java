package br.com.osa.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final TokenService tokenService;

  public JwtAuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String path = request.getServletPath();
    logger.debug("Starting JWT filter for path: {}", path);

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);

      if (!token.isBlank()) {
        try {
          String userId = tokenService.parseSubject(token);
          logger.debug("Extracted subject from token for path {}", path);

          Principal principal = () -> userId;

          Authentication auth = new AbstractAuthenticationToken(
              AuthorityUtils.createAuthorityList("ROLE_USER")) {
            @Override
            public Object getCredentials() {
              return token;
            }

            @Override
            public Object getPrincipal() {
              return principal;
            }

            @Override
            public String getName() {
              return principal.getName();
            }
          };

          ((AbstractAuthenticationToken) auth)
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          auth.setAuthenticated(true);

          org.springframework.security.core.context.SecurityContextHolder.getContext()
              .setAuthentication(auth);
          logger.debug("Authentication registered in SecurityContext for userId={}", userId);

        } catch (Exception e) {
          logger.warn("Failed to validate JWT for path {}: {}", path, e.getMessage());
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }
      } else {
        logger.debug("Bearer token is blank for path: {}", path);
      }
    } else {
      logger.debug("No Bearer token found in Authorization header for path: {}", path);
    }

    chain.doFilter(request, response);
    logger.debug("JWT filter finished for path: {}", path);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    boolean ignore = path.startsWith("/auth/");
    logger.debug("shouldNotFilter? {} for path: {}", ignore, path);
    return ignore;
  }
}
