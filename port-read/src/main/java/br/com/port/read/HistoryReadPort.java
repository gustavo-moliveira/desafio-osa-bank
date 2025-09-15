package br.com.port.read;

import br.com.osa.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface HistoryReadPort {

  void append(Transaction transaction);

  List<Transaction> get(UUID accountId);
}
