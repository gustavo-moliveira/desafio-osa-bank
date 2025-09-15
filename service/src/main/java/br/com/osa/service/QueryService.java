package br.com.osa.service;

import br.com.osa.domain.exception.ResourceNotFoundException;
import br.com.osa.domain.model.Account;
import br.com.osa.domain.model.Transaction;
import br.com.osa.port.repository.AccountRepositoryPort;
import br.com.port.read.BalanceCachePort;
import br.com.port.read.HistoryReadPort;
import java.math.BigDecimal;
import java.util.List;
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
    UUID accountId = accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"))
        .getId();

    BigDecimal cached = balanceCachePort.getBalance(accountId);
    if (cached != null) {
      return cached;
    }

    Account account = accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

    BigDecimal saldoAjustado = account.getBalance();
    if (account.getNegativeDebt().compareTo(BigDecimal.ZERO) > 0) {
      saldoAjustado = saldoAjustado.subtract(account.getNegativeDebt());
    }

    balanceCachePort.setBalance(accountId, saldoAjustado);

    return saldoAjustado;
  }


  public List<Transaction> getHistory(UUID userId) {
    UUID accountId = accountRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"))
        .getId();
    return historyReadPort.get(accountId);
  }
}
