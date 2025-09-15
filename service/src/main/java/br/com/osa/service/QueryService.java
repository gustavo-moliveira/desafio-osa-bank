package br.com.osa.service;

import br.com.osa.domain.exception.ResourceNotFoundException;
import br.com.osa.domain.model.Account;
import br.com.osa.domain.model.Transaction;
import br.com.osa.port.repository.AccountRepositoryPort;
import br.com.port.read.BalanceCachePort;
import br.com.port.read.HistoryReadPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QueryService {

  private final AccountRepositoryPort accountRepositoryPort;
  private final BalanceCachePort balanceCachePort;
  private final HistoryReadPort historyReadPort;

  public QueryService(AccountRepositoryPort accountRepositoryPort,
      BalanceCachePort balanceCachePort,
      HistoryReadPort historyReadPort) {
    this.accountRepositoryPort = accountRepositoryPort;
    this.balanceCachePort = balanceCachePort;
    this.historyReadPort = historyReadPort;
  }

  public BigDecimal getBalance(UUID userId) {
    Account account = findAccountOrThrow(userId);
    UUID accountId = account.getId();

    Optional<BigDecimal> cachedOpt = balanceCachePort.getBalance(accountId);
    if (cachedOpt.isPresent()) {
      return cachedOpt.get();
    }

    BigDecimal adjusted = calculateAdjustedBalance(account);
    balanceCachePort.setBalance(accountId, adjusted);
    return adjusted;
  }

  public List<Transaction> getHistory(UUID userId) {
    Account account = findAccountOrThrow(userId);
    return historyReadPort.get(account.getId());
  }

  private Account findAccountOrThrow(UUID userId) {
    return accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
  }

  private BigDecimal calculateAdjustedBalance(Account account) {
    BigDecimal balance = account.getBalance();
    if (account.getNegativeDebt().compareTo(BigDecimal.ZERO) > 0) {
      balance = balance.subtract(account.getNegativeDebt());
    }
    return balance;
  }
}