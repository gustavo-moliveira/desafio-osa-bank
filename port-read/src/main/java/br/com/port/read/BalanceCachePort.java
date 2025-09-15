package br.com.port.read;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface BalanceCachePort {

  void setBalance(UUID accountId, BigDecimal balance);

  Optional<BigDecimal> getBalance(UUID accountId);
}
