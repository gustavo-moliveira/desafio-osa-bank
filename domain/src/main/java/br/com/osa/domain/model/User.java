package br.com.osa.domain.model;

import java.util.UUID;

public class User {

  private UUID id;
  private String fullName;
  private String cpf;
  private String login;
  private String passwordHash;

  public User(UUID id, String fullName, String cpf, String login, String passwordHash) {
    this.id = id;
    this.fullName = fullName;
    this.cpf = cpf;
    this.login = login;
    this.passwordHash = passwordHash;
  }

  public UUID getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getCpf() {
    return cpf;
  }

  public String getLogin() {
    return login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }
}
