package br.com.osa.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

  public JwtAuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String path = request.getServletPath();
    System.out.println("[JwtFilter] Iniciando filtro para path: " + path);

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    System.out.println("[JwtFilter] Header Authorization: " + header);

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      System.out.println("[JwtFilter] Token extraído: " + token);

      if (!token.isBlank()) {
        try {
          String userId = tokenService.parseSubject(token);
          System.out.println("[JwtFilter] Subject (userId) extraído do token: " + userId);

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
          System.out.println("[JwtFilter] Autenticação registrada no SecurityContext: " + auth);

        } catch (Exception e) {
          System.out.println("[JwtFilter] Erro ao validar token: " + e.getMessage());
          e.printStackTrace();
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }
      } else {
        System.out.println("[JwtFilter] Token está em branco.");
      }
    } else {
      System.out.println("[JwtFilter] Nenhum token Bearer encontrado no header.");
    }

    chain.doFilter(request, response);
    System.out.println("[JwtFilter] Filtro finalizado para path: " + path);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    boolean ignorar = path.startsWith("/auth/");
    System.out.println("[JwtFilter] shouldNotFilter? " + ignorar + " para path: " + path);
    return ignorar;
  }
}
