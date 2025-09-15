package br.com.port.read;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceCachePort {

  void setBalance(UUID accountId, BigDecimal balance);

  BigDecimal getBalance(UUID accountId);
}
