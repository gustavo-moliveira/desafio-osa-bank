package br.com.osa.infrastructure.persistence.sql.adapter;

import br.com.osa.domain.model.Transaction;
import br.com.osa.infrastructure.persistence.sql.mapper.TransactionMapper;
import br.com.osa.infrastructure.persistence.sql.repository.TransactionJpaRepository;
import br.com.osa.port.repository.TransactionRepositoryPort;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

  private final TransactionJpaRepository repository;

  public TransactionRepositoryAdapter(TransactionJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Transaction save(Transaction transaction) {
    return TransactionMapper.toDomain(
        repository.save(TransactionMapper.toEntity(transaction))
    );
  }

  @Override
  public List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId) {
    return repository.findByAccountIdOrderByCreatedAtDesc(accountId)
        .stream()
        .map(TransactionMapper::toDomain)
        .collect(Collectors.toList());
  }
}
