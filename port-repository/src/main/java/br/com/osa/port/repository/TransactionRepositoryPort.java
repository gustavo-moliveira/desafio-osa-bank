package br.com.osa.port.repository;

import br.com.osa.domain.model.Transaction;

public interface TransactionRepositoryPort {

  void save(Transaction transaction);
}