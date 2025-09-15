package br.com.osa.infrastructure.security.adapter;

import br.com.osa.port.security.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public String encode(String raw) {
    return encoder.encode(raw);
  }

  @Override
  public boolean matches(String raw, String encoded) {
    return encoder.matches(raw, encoded);
  }
}
