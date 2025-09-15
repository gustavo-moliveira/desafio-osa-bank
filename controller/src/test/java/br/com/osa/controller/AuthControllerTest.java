package br.com.osa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.com.osa.domain.exception.ValidationException;
import br.com.osa.domain.model.User;
import br.com.osa.port.security.TokenProviderPort;
import br.com.osa.service.AuthService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock
  private AuthService authService;

  @Mock
  private TokenProviderPort tokenProvider;

  @InjectMocks
  private AuthController controller;

  @Test
  void whenRegister_withValidData_thenReturnUserId() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "Full Name", "12345678901", "login", "hash");
    when(authService.register(any(), any(), any(), any())).thenReturn(user);

    AuthController.RegisterRequest req = new AuthController.RegisterRequest(
        "Full Name", "12345678901", "login", "pass123");

    ResponseEntity<?> resp = controller.register(req);
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    Map<?, ?> map = (Map<?, ?>) resp.getBody();
    assertEquals(userId.toString(), map.get("userId"));
  }

  @Test
  void whenRegister_withInvalidData_thenThrowsValidationException() {
    when(authService.register(any(), any(), any(), any()))
        .thenThrow(new ValidationException("Invalid data"));

    AuthController.RegisterRequest req = new AuthController.RegisterRequest(
        "", "", "", "");

    assertThrows(ValidationException.class, () -> controller.register(req));
  }

  @Test
  void whenAuthenticate_withValidCredentials_thenReturnToken() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "Full", "123", "login", "hash");
    when(authService.authenticate(eq("login"), eq("pass"))).thenReturn(Optional.of(user));
    when(tokenProvider.generateToken(userId.toString(), user.getLogin())).thenReturn("token-xyz");

    AuthController.LoginRequest req = new AuthController.LoginRequest();
    req.login = "login";
    req.password = "pass";

    ResponseEntity<?> resp = controller.login(req);
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    Map<?, ?> map = (Map<?, ?>) resp.getBody();
    assertEquals("token-xyz", map.get("token"));
  }

  @Test
  void whenAuthenticate_withInvalidCredentials_thenReturn401() {
    when(authService.authenticate(eq("login"), eq("wrong"))).thenReturn(Optional.empty());

    AuthController.LoginRequest req = new AuthController.LoginRequest();
    req.login = "login";
    req.password = "wrong";

    ResponseEntity<?> resp = controller.login(req);
    assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
  }
}
