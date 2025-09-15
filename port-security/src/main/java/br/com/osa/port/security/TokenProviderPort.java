package br.com.osa.port.security;

/**
 * Porta para geração de tokens de autenticação.
 * Implementações típicas: JWT, OAuth2 tokens, etc.
 */
public interface TokenProviderPort {

    /**
     * Gera um token de autenticação para um usuário.
     *
     * @param userId  identificador único do usuário
     * @param subject informação adicional (ex: login)
     * @return token gerado
     */
    String generateToken(String userId, String subject);
}
