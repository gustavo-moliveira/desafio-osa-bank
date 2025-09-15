package br.com.osa.infrastructure.persistence.sql.mapper;

import br.com.osa.domain.model.User;
import br.com.osa.infrastructure.persistence.sql.entity.UserEntity;

public class UserMapper {

  public static UserEntity toEntity(User user) {
    UserEntity e = new UserEntity();
    e.setId(user.getId());
    e.setFullName(user.getFullName());
    e.setCpf(user.getCpf());
    e.setLogin(user.getLogin());
    e.setPasswordHash(user.getPasswordHash());
    return e;
  }

  public static User toDomain(UserEntity e) {
    return new User(e.getId(), e.getFullName(), e.getCpf(), e.getLogin(), e.getPasswordHash());
  }
}
