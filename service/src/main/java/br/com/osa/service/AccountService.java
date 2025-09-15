package br.com.osa.service;

import br.com.osa.domain.exception.ResourceNotFoundException;
import br.com.osa.domain.exception.ValidationException;
import br.com.osa.domain.model.Account;
import br.com.osa.domain.model.Transaction;
import br.com.osa.domain.model.TransactionType;
import br.com.osa.port.repository.AccountRepositoryPort;
import br.com.osa.port.repository.TransactionRepositoryPort;
import br.com.port.read.BalanceCachePort;
import br.com.port.read.HistoryReadPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountService {

  private final AccountRepositoryPort accountRepositoryPort;
  private final TransactionRepositoryPort transactionRepositoryPort;
  private final HistoryReadPort historyReadPort;
  private final BalanceCachePort balanceCachePort;

  public AccountService(AccountRepositoryPort accountRepositoryPort,
      TransactionRepositoryPort transactionRepositoryPort,
      HistoryReadPort historyReadPort,
      BalanceCachePort balanceCachePort) {
    this.accountRepositoryPort = accountRepositoryPort;
    this.transactionRepositoryPort = transactionRepositoryPort;
    this.historyReadPort = historyReadPort;
    this.balanceCachePort = balanceCachePort;
  }

  public void deposit(UUID userId, BigDecimal amount) {
    validateAmount(amount);
    validateUserId(userId);

    Account account = accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    Account updated = account.deposit(amount);
    accountRepositoryPort.save(updated);

    Transaction tx = new Transaction(
        UUID.randomUUID(),
        updated.getId(),
        TransactionType.DEPOSIT,
        amount,
        LocalDateTime.now()
    );
    transactionRepositoryPort.save(tx);
    historyReadPort.append(tx);

    balanceCachePort.setBalance(updated.getId(), calcularSaldoAjustado(updated));
  }

  public void withdraw(UUID userId, BigDecimal amount) {
    validateAmount(amount);
    validateUserId(userId);

    Account account = accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    Account updated = account.withdraw(amount);
    accountRepositoryPort.save(updated);

    Transaction tx = new Transaction(
        UUID.randomUUID(),
        updated.getId(),
        TransactionType.WITHDRAW,
        amount,
        LocalDateTime.now()
    );
    transactionRepositoryPort.save(tx);
    historyReadPort.append(tx);

    balanceCachePort.setBalance(updated.getId(), calcularSaldoAjustado(updated));
  }

  private BigDecimal calcularSaldoAjustado(Account account) {
    BigDecimal saldo = account.getBalance();
    if (account.getNegativeDebt().compareTo(BigDecimal.ZERO) > 0) {
      saldo = saldo.subtract(account.getNegativeDebt());
    }
    return saldo;
  }

  private void validateUserId(UUID userId) {
    if (userId == null) {
      throw new ValidationException("Invalid user identifier");
    }
  }

  private void validateAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Transaction amount must be greater than zero");
    }
    if (amount.scale() > 2) {
      throw new ValidationException("Amount must have at most two decimal places");
    }
  }
}
