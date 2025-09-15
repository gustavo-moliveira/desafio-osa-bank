package br.com.osa.infrastructure.persistence.sql.mapper;

import br.com.osa.domain.model.Transaction;
import br.com.osa.domain.model.TransactionType;
import br.com.osa.infrastructure.persistence.sql.entity.TransactionEntity;

public class TransactionMapper {

  public static TransactionEntity toEntity(Transaction t) {
    TransactionEntity e = new TransactionEntity();
    e.setId(t.getId());
    e.setAccountId(t.getAccountId());
    e.setType(t.getType().name());
    e.setAmount(t.getAmount());
    e.setCreatedAt(t.getCreatedAt());
    return e;
  }

  public static Transaction toDomain(TransactionEntity e) {
    return new Transaction(e.getId(), e.getAccountId(), TransactionType.valueOf(e.getType()),
        e.getAmount(), e.getCreatedAt());
  }
}
