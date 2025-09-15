package br.com.osa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.osa.service.AccountService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class TransactionCommandControllerTest {

  @Mock
  private AccountService accountService;

  private TransactionCommandController controller;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    controller = new TransactionCommandController(accountService);
  }

  @Test
  void whenDeposit_withValidAmount_thenCallsServiceAndReturnsOk() {
    UUID userId = UUID.randomUUID();
    Principal principal = () -> userId.toString();

    TransactionCommandController.MoneyRequest req = new TransactionCommandController.MoneyRequest();
    req.amount = new BigDecimal("10.50");

    doNothing().when(accountService).deposit(eq(userId), any(BigDecimal.class));

    ResponseEntity<?> resp = controller.deposit(principal, req);
    assertEquals(200, resp.getStatusCodeValue());

    verify(accountService, times(1)).deposit(eq(userId), any(BigDecimal.class));
  }

  @Test
  void whenWithdraw_withValidAmount_thenCallsServiceAndReturnsOk() {
    UUID userId = UUID.randomUUID();
    Principal principal = () -> userId.toString();

    TransactionCommandController.MoneyRequest req = new TransactionCommandController.MoneyRequest();
    req.amount = new BigDecimal("5.00");

    doNothing().when(accountService).withdraw(eq(userId), any(BigDecimal.class));

    ResponseEntity<?> resp = controller.withdraw(principal, req);
    assertEquals(200, resp.getStatusCodeValue());
  }

  @Test
  void whenDeposit_withInvalidAmount_thenThrowsValidationException() {
    UUID userId = UUID.randomUUID();
    Principal principal = () -> userId.toString();

    TransactionCommandController.MoneyRequest req = new TransactionCommandController.MoneyRequest();
    req.amount = new BigDecimal("0.00");

    doNothing().when(accountService).deposit(eq(userId), any(BigDecimal.class));
    org.mockito.Mockito.doThrow(
            new br.com.osa.domain.exception.ValidationException("Invalid amount"))
        .when(accountService).deposit(eq(userId), any(BigDecimal.class));

    org.junit.jupiter.api.Assertions.assertThrows(
        br.com.osa.domain.exception.ValidationException.class,
        () -> controller.deposit(principal, req));
  }

  @Test
  void whenWithdraw_withInvalidAmount_thenThrowsValidationException() {
    UUID userId = UUID.randomUUID();
    Principal principal = () -> userId.toString();

    TransactionCommandController.MoneyRequest req = new TransactionCommandController.MoneyRequest();
    req.amount = new BigDecimal("-5.00");

    org.mockito.Mockito.doThrow(
            new br.com.osa.domain.exception.ValidationException("Invalid amount"))
        .when(accountService).withdraw(eq(userId), any(BigDecimal.class));

    org.junit.jupiter.api.Assertions.assertThrows(
        br.com.osa.domain.exception.ValidationException.class,
        () -> controller.withdraw(principal, req));
  }
}