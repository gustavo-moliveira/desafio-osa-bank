package br.com.osa.controller;

import static br.com.osa.controller.handler.MoneyFormatter.format;

import br.com.osa.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
public class TransactionCommandController {

  private final AccountService accountService;

  public TransactionCommandController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/deposit")
  public ResponseEntity<?> deposit(@AuthenticationPrincipal Principal principal,
      @Valid @RequestBody MoneyRequest request) {
    UUID userId = UUID.fromString(principal.getName());
    accountService.deposit(userId, format(request.amount));
    return ResponseEntity.ok(Map.of("message", "Depósito realizado com sucesso"));
  }

  @PostMapping("/withdraw")
  public ResponseEntity<?> withdraw(@AuthenticationPrincipal Principal principal,
      @Valid @RequestBody MoneyRequest request) {
    UUID userId = UUID.fromString(principal.getName());
    accountService.withdraw(userId, format(request.amount));
    return ResponseEntity.ok(Map.of("message", "Saque realizado com sucesso"));
  }

  public static class MoneyRequest {

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    public BigDecimal amount;
  }
}
