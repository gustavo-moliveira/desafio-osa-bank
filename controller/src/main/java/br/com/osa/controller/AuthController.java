package br.com.osa.controller;

import br.com.osa.domain.model.User;
import br.com.osa.port.security.TokenProviderPort;
import br.com.osa.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final TokenProviderPort tokenProvider;

  public AuthController(AuthService authService, TokenProviderPort tokenProvider) {
    this.authService = authService;
    this.tokenProvider = tokenProvider;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    User user = authService.register(request.fullName, request.cpf, request.login,
        request.password);
    return ResponseEntity.ok(Map.of("userId", user.getId().toString()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    return authService.authenticate(request.login, request.password)
        .map(u -> ResponseEntity.ok(
            Map.of("token", tokenProvider.generateToken(u.getId().toString(), u.getLogin()))))
        .orElse(ResponseEntity.status(401).build());
  }

  public record RegisterRequest(
      @NotBlank(message = "Full name is required")
      String fullName,

      @NotBlank(message = "CPF is required")
      String cpf,

      @NotBlank(message = "Login is required")
      String login,

      @NotBlank(message = "Password is required")
      String password
  ) {

  }

  public static class LoginRequest {

    @NotBlank
    public String login;
    @NotBlank
    public String password;
  }
}
