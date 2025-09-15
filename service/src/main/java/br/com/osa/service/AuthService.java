package br.com.osa.service;

import br.com.osa.domain.exception.BusinessException;
import br.com.osa.domain.exception.ValidationException;
import br.com.osa.domain.model.Account;
import br.com.osa.domain.model.User;
import br.com.osa.domain.util.CpfValidator;
import br.com.osa.port.repository.AccountRepositoryPort;
import br.com.osa.port.repository.UserRepositoryPort;
import br.com.osa.port.security.PasswordEncoderPort;
import br.com.port.read.BalanceCachePort;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class AuthService {

  private final UserRepositoryPort userRepositoryPort;
  private final AccountRepositoryPort accountRepositoryPort;
  private final PasswordEncoderPort passwordEncoderPort;
  private final BalanceCachePort balanceCachePort;

  public AuthService(UserRepositoryPort userRepositoryPort,
      AccountRepositoryPort accountRepositoryPort,
      PasswordEncoderPort passwordEncoderPort,
      BalanceCachePort balanceCachePort) {
    this.userRepositoryPort = userRepositoryPort;
    this.accountRepositoryPort = accountRepositoryPort;
    this.passwordEncoderPort = passwordEncoderPort;
    this.balanceCachePort = balanceCachePort;
  }

  public User register(String fullName, String cpf, String login, String password) {
    validateFields(fullName, cpf, login, password);

    if (!CpfValidator.isValid(cpf)) {
      throw new ValidationException("CPF inválido");
    }

    if (userRepositoryPort.existsByCpf(cpf) || userRepositoryPort.existsByLogin(login)) {
      throw new BusinessException("Usuário já cadastrado");
    }

    UUID userId = UUID.randomUUID();
    String encoded = passwordEncoderPort.encode(password);
    User user = new User(userId, fullName.trim(), cpf, login.trim(), encoded);
    userRepositoryPort.save(user);

    Account account = new Account(UUID.randomUUID(), userId, BigDecimal.ZERO, BigDecimal.ZERO);
    accountRepositoryPort.save(account);
    balanceCachePort.setBalance(account.getId(), BigDecimal.ZERO);

    return user;
  }

  private void validateFields(String fullName, String cpf, String login, String password) {
    if (fullName == null || fullName.trim().isEmpty()) {
      throw new ValidationException("Nome completo é obrigatório");
    }
    if (cpf == null || cpf.trim().isEmpty()) {
      throw new ValidationException("CPF é obrigatório");
    }
    if (login == null || login.trim().isEmpty()) {
      throw new ValidationException("Login é obrigatório");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new ValidationException("Senha é obrigatória");
    }
  }

  public Optional<User> authenticate(String login, String password) {
    Optional<User> user = userRepositoryPort.findByLogin(login);
    if (user.isEmpty()) {
      return Optional.empty();
    }
    boolean ok = passwordEncoderPort.matches(password, user.get().getPasswordHash());
    return ok ? user : Optional.empty();
  }
}
