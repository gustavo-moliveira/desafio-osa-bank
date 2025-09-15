package br.com.osa.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class OverdraftPolicy {

  private static final BigDecimal CHARGE_MULTIPLIER = new BigDecimal("1.02");
  private static final int SCALE = 2;

  private OverdraftPolicy() {
  }

  public static BigDecimal calculateCharge(BigDecimal negativeDebt) {
    if (negativeDebt == null) {
      return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
    }
    return negativeDebt.multiply(CHARGE_MULTIPLIER).setScale(SCALE, RoundingMode.HALF_UP);
  }
}

