package br.com.osa.infrastructure.persistence.sql.repository;

import br.com.osa.infrastructure.persistence.sql.entity.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

  Optional<AccountEntity> findByUserId(UUID userId);
}
