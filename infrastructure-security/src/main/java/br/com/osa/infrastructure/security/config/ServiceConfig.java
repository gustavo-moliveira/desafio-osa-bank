package br.com.osa.infrastructure.security.config;

import br.com.osa.port.repository.AccountRepositoryPort;
import br.com.osa.port.repository.TransactionRepositoryPort;
import br.com.osa.port.repository.UserRepositoryPort;
import br.com.osa.port.security.PasswordEncoderPort;
import br.com.osa.service.AccountService;
import br.com.osa.service.AuthService;
import br.com.osa.service.QueryService;
import br.com.port.read.BalanceCachePort;
import br.com.port.read.HistoryReadPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  public AuthService authService(UserRepositoryPort userRepositoryPort,
      AccountRepositoryPort accountRepositoryPort,
      PasswordEncoderPort passwordEncoderPort,
      BalanceCachePort balanceCachePort) {
    return new AuthService(userRepositoryPort, accountRepositoryPort, passwordEncoderPort,
        balanceCachePort);
  }

  @Bean
  public AccountService accountService(AccountRepositoryPort accountRepositoryPort,
      TransactionRepositoryPort transactionRepositoryPort,
      HistoryReadPort historyReadPort,
      BalanceCachePort balanceCachePort) {
    return new AccountService(accountRepositoryPort, transactionRepositoryPort, historyReadPort,
        balanceCachePort);
  }

  @Bean
  public QueryService queryService(AccountRepositoryPort accountRepositoryPort,
      BalanceCachePort balanceCachePort,
      HistoryReadPort historyReadPort) {
    return new QueryService(accountRepositoryPort, balanceCachePort, historyReadPort);
  }
}