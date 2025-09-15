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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
public class TransactionCommandController {

  private static final Logger logger = LoggerFactory.getLogger(TransactionCommandController.class);

  private final AccountService accountService;

  public TransactionCommandController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/deposit")
  public ResponseEntity<?> deposit(@AuthenticationPrincipal Principal principal,
      @Valid @RequestBody MoneyRequest request) {
    UUID userId = UUID.fromString(principal.getName());
    BigDecimal amount = format(request.amount);
    logger.info("Deposit request - userId={} amount={}", userId, amount);

    accountService.deposit(userId, amount);

    logger.info("Deposit successful - userId={} amount={}", userId, amount);
    return ResponseEntity.ok(Map.of("message", "Deposit completed successfully"));
  }

  @PostMapping("/withdraw")
  public ResponseEntity<?> withdraw(@AuthenticationPrincipal Principal principal,
      @Valid @RequestBody MoneyRequest request) {
    UUID userId = UUID.fromString(principal.getName());
    BigDecimal amount = format(request.amount);
    logger.info("Withdraw request - userId={} amount={}", userId, amount);

    accountService.withdraw(userId, amount);

    logger.info("Withdraw successful - userId={} amount={}", userId, amount);
    return ResponseEntity.ok(Map.of("message", "Withdraw completed successfully"));
  }

  public static class MoneyRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "The amount must be greater than zero")
    public BigDecimal amount;
  }
}
