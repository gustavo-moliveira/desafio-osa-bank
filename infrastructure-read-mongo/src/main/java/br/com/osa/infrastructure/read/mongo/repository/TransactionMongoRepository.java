package br.com.osa.infrastructure.read.mongo.repository;

import br.com.osa.infrastructure.read.mongo.document.TransactionDocument;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, UUID> {

  List<TransactionDocument> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
