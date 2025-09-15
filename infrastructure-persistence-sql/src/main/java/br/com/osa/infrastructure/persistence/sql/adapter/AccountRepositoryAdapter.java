package br.com.osa.infrastructure.persistence.sql.adapter;

import br.com.osa.domain.model.Account;
import br.com.osa.infrastructure.persistence.sql.mapper.AccountMapper;
import br.com.osa.infrastructure.persistence.sql.repository.AccountJpaRepository;
import br.com.osa.port.repository.AccountRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

  private final AccountJpaRepository repository;

  public AccountRepositoryAdapter(AccountJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(Account account) {
    AccountMapper.toDomain(repository.save(AccountMapper.toEntity(account)));
  }

  @Override
  public Optional<Account> findByUserId(UUID userId) {
    return repository.findByUserId(userId).map(AccountMapper::toDomain);
  }
}
