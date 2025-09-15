package br.com.osa.controller.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyFormatter {

  public static BigDecimal format(BigDecimal value) {
    return value.setScale(2, RoundingMode.HALF_UP);
  }
}
