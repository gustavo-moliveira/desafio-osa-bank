package br.com.osa.port.security;

public interface PasswordEncoderPort {

  String encode(String raw);

  boolean matches(String raw, String encoded);
}
