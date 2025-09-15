package br.com.osa.infrastructure.persistence.sql.adapter;

import br.com.osa.domain.model.User;
import br.com.osa.infrastructure.persistence.sql.mapper.UserMapper;
import br.com.osa.infrastructure.persistence.sql.repository.UserJpaRepository;
import br.com.osa.port.repository.UserRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository repository;

  public UserRepositoryAdapter(UserJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public User save(User user) {
    return UserMapper.toDomain(repository.save(UserMapper.toEntity(user)));
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return repository.findByLogin(login).map(UserMapper::toDomain);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return repository.findById(id).map(UserMapper::toDomain);
  }

  @Override
  public boolean existsByCpf(String cpf) {
    return repository.existsByCpf(cpf);
  }

  @Override
  public boolean existsByLogin(String login) {
    return repository.existsByLogin(login);
  }
}
