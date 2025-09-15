package br.com.osa.infrastructure.read.redis.adapter;

import br.com.port.read.BalanceCachePort;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RedisBalanceCacheAdapter implements BalanceCachePort {

    private static final Logger logger = LoggerFactory.getLogger(RedisBalanceCacheAdapter.class);
    private final RedisTemplate<String, String> redis;
    private static final String KEY_PREFIX = "balance:";
    private static final Duration TTL = Duration.ofMinutes(10);

    public RedisBalanceCacheAdapter(RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    @Override
    public void setBalance(UUID accountId, BigDecimal balance) {
        if (accountId == null || balance == null) return;
        String key = KEY_PREFIX + accountId;
        try {
            redis.opsForValue().set(key, balance.toPlainString(), TTL);
            logger.debug("Set balance cache for key={}, ttlMinutes={}", key, TTL.toMinutes());
        } catch (Exception e) {
            logger.error("Failed to set balance in Redis for key={}: {}", key, e.getMessage());
        }
    }

    @Override
    public Optional<BigDecimal> getBalance(UUID accountId) {
        if (accountId == null) return Optional.empty();
        String key = KEY_PREFIX + accountId;
        try {
            String value = redis.opsForValue().get(key);
            if (value == null) {
                logger.debug("Cache miss for key={}", key);
                return Optional.empty();
            }
            try {
                BigDecimal parsed = new BigDecimal(value);
                logger.debug("Cache hit for key={}", key);
                return Optional.of(parsed);
            } catch (NumberFormatException ex) {
                logger.warn("Invalid cached balance for key={}: {}", key, value);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Failed to read balance from Redis for key={}: {}", key, e.getMessage());
            return Optional.empty();
        }
    }
}