package br.com.osa.port.security;

public interface TokenProviderPort {

    String generateToken(String userId, String subject);
}
