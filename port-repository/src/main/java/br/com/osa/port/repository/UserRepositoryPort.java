package br.com.osa.port.repository;

import br.com.osa.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {

  void save(User user);

  Optional<User> findByLogin(String login);

  boolean existsByCpf(String cpf);

  boolean existsByLogin(String login);
}