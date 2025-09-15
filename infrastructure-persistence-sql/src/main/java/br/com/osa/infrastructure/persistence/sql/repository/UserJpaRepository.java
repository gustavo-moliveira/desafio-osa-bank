package br.com.osa.infrastructure.persistence.sql.repository;

import br.com.osa.infrastructure.persistence.sql.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByLogin(String login);

  boolean existsByCpf(String cpf);

  boolean existsByLogin(String login);
}