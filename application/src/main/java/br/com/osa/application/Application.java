package br.com.osa.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.osa")
@EntityScan(basePackages = "br.com.osa.infrastructure.persistence.sql.entity")
@EnableJpaRepositories(basePackages = "br.com.osa.infrastructure.persistence.sql.repository")
@EnableMongoRepositories(basePackages = "br.com.osa.infrastructure.read.mongo.repository")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
