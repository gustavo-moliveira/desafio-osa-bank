package br.com.osa.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

  private final UUID id;
  private final UUID accountId;
  private final TransactionType type;
  private final BigDecimal amount;
  private final LocalDateTime createdAt;

  public Transaction(UUID id, UUID accountId, TransactionType type, BigDecimal amount,
      LocalDateTime createdAt) {
    this.id = id;
    this.accountId = accountId;
    this.type = type;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getAccountId() {
    return accountId;
  }

  public TransactionType getType() {
    return type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
