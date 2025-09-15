package br.com.osa.port.repository;

import br.com.osa.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

  void save(Account account);

  Optional<Account> findByUserId(UUID userId);
}
