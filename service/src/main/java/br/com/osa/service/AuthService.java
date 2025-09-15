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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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
    logger.info("Register request for login='{}'", login);
    validateFields(fullName, cpf, login, password);

    if (!CpfValidator.isValid(cpf)) {
      logger.warn("Invalid CPF provided for login='{}'", login);
      throw new ValidationException("Invalid CPF");
    }

    if (userRepositoryPort.existsByCpf(cpf) || userRepositoryPort.existsByLogin(login)) {
      logger.warn("Attempt to register existing user for login='{}'", login);
      throw new BusinessException("User already registered");
    }

    UUID userId = UUID.randomUUID();
    String encoded = passwordEncoderPort.encode(password);
    User user = new User(userId, fullName.trim(), cpf, login.trim(), encoded);
    userRepositoryPort.save(user);

    Account account = new Account(UUID.randomUUID(), userId, BigDecimal.ZERO, BigDecimal.ZERO);
    accountRepositoryPort.save(account);
    balanceCachePort.setBalance(account.getId(), BigDecimal.ZERO);

    logger.info("User registered successfully login='{}' userId='{}'", login, userId);
    return user;
  }

  private void validateFields(String fullName, String cpf, String login, String password) {
    checkRequired(fullName, "Full name");
    checkRequired(cpf, "CPF");
    checkRequired(login, "Login");
    checkRequired(password, "Password");
  }

  public Optional<User> authenticate(String login, String password) {
    logger.info("Authentication attempt for login='{}'", login);
    Optional<User> user = userRepositoryPort.findByLogin(login);
    if (user.isEmpty()) {
      logger.debug("Authentication failed: user not found for login='{}'", login);
      return Optional.empty();
    }
    boolean ok = passwordEncoderPort.matches(password, user.get().getPasswordHash());
    if (ok) {
      logger.info("Authentication successful for login='{}' userId='{}'", login, user.get().getId());
    } else {
      logger.warn("Authentication failed for login='{}'", login);
    }
    return ok ? user : Optional.empty();
  }

  private void checkRequired(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      logger.warn("Validation failed: {} is required", fieldName);
      throw new ValidationException(fieldName + " is required");
    }
  }

}
