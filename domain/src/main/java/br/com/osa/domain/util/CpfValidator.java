package br.com.osa.domain.util;

public class CpfValidator {

  public static boolean isValid(String cpf) {
    String digits = cpf.replaceAll("\\D", "");
    if (digits.length() != 11) {
      return false;
    }
    if (digits.chars().distinct().count() == 1) {
      return false;
    }
    int d1 = calcDigit(digits.substring(0, 9), 10);
    int d2 = calcDigit(digits.substring(0, 9) + d1, 11);
    return digits.equals(digits.substring(0, 9) + d1 + d2);
  }

  private static int calcDigit(String base, int weight) {
    int sum = 0;
    for (int i = 0; i < base.length(); i++) {
      int num = base.charAt(i) - '0';
      sum += num * (weight - i);
    }
    int mod = sum % 11;
    int digit = 11 - mod;
    if (digit > 9) {
      return 0;
    }
    return digit;
  }
}
