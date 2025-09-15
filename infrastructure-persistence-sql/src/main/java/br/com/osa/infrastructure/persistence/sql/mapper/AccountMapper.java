package br.com.osa.infrastructure.persistence.sql.mapper;

import br.com.osa.domain.model.Account;
import br.com.osa.infrastructure.persistence.sql.entity.AccountEntity;

public class AccountMapper {

  public static AccountEntity toEntity(Account a) {
    AccountEntity e = new AccountEntity();
    e.setId(a.getId());
    e.setUserId(a.getUserId());
    e.setBalance(a.getBalance());
    e.setNegativeDebt(a.getNegativeDebt());
    return e;
  }

  public static Account toDomain(AccountEntity e) {
    return new Account(e.getId(), e.getUserId(), e.getBalance(), e.getNegativeDebt());
  }
}
