package br.com.osa.infrastructure.persistence.sql.repository;

import br.com.osa.infrastructure.persistence.sql.entity.TransactionEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {

  List<TransactionEntity> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
