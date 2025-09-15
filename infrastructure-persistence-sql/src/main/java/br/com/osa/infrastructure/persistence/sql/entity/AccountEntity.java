package br.com.osa.infrastructure.persistence.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal negativeDebt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getNegativeDebt() {
    return negativeDebt;
  }

  public void setNegativeDebt(BigDecimal negativeDebt) {
    this.negativeDebt = negativeDebt;
  }
}
