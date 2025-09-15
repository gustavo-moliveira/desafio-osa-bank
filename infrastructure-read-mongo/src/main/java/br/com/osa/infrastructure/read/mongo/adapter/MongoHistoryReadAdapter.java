package br.com.osa.infrastructure.read.mongo.adapter;

import br.com.osa.domain.model.Transaction;
import br.com.osa.infrastructure.read.mongo.document.TransactionDocument;
import br.com.osa.infrastructure.read.mongo.repository.TransactionMongoRepository;
import br.com.port.read.HistoryReadPort;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MongoHistoryReadAdapter implements HistoryReadPort {

  private final TransactionMongoRepository repository;

  public MongoHistoryReadAdapter(TransactionMongoRepository repository) {
    this.repository = repository;
  }

  @Override
  public void append(Transaction transaction) {
    TransactionDocument doc = new TransactionDocument();
    doc.setId(transaction.getId());
    doc.setAccountId(transaction.getAccountId());
    doc.setType(transaction.getType());
    doc.setAmount(transaction.getAmount());
    doc.setCreatedAt(transaction.getCreatedAt());

    repository.save(doc);
  }

  @Override
  public List<Transaction> get(UUID accountId) {
    return repository.findByAccountIdOrderByCreatedAtDesc(accountId)
        .stream()
        .map(doc -> new Transaction(
            doc.getId(),
            doc.getAccountId(),
            doc.getType(),
            doc.getAmount(),
            doc.getCreatedAt()
        ))
        .toList();
  }
}
