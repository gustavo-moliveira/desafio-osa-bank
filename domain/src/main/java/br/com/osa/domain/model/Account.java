package br.com.osa.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

  private UUID id;
  private UUID userId;
  private BigDecimal balance;
  private BigDecimal negativeDebt;

  public Account(UUID id, UUID userId, BigDecimal balance, BigDecimal negativeDebt) {
    this.id = id;
    this.userId = userId;
    this.balance = balance;
    this.negativeDebt = negativeDebt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public BigDecimal getNegativeDebt() {
    return negativeDebt;
  }

  public Account deposit(BigDecimal amount) {
    BigDecimal updated = this.balance.add(amount);

    if (this.negativeDebt.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal charge = this.negativeDebt.multiply(new BigDecimal("1.02"));
      if (updated.compareTo(charge) >= 0) {
        return new Account(this.id, this.userId, updated.subtract(charge), BigDecimal.ZERO);
      } else {
        return new Account(this.id, this.userId, BigDecimal.ZERO, charge.subtract(updated));
      }
    }
    return new Account(this.id, this.userId, updated, this.negativeDebt);
  }

  public Account withdraw(BigDecimal amount) {
    if (this.balance.compareTo(amount) >= 0) {
      return new Account(this.id, this.userId, this.balance.subtract(amount), this.negativeDebt);
    } else {
      BigDecimal shortfall = amount.subtract(this.balance);
      return new Account(this.id, this.userId, BigDecimal.ZERO, this.negativeDebt.add(shortfall));
    }
  }
}
