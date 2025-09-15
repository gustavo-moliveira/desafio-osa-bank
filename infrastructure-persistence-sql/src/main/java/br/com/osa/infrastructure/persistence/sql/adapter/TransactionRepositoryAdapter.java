package br.com.osa.infrastructure.persistence.sql.adapter;

import br.com.osa.domain.model.Transaction;
import br.com.osa.infrastructure.persistence.sql.mapper.TransactionMapper;
import br.com.osa.infrastructure.persistence.sql.repository.TransactionJpaRepository;
import br.com.osa.port.repository.TransactionRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

  private final TransactionJpaRepository repository;

  public TransactionRepositoryAdapter(TransactionJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(Transaction transaction) {
    TransactionMapper.toDomain(
        repository.save(TransactionMapper.toEntity(transaction))
    );
  }
}
