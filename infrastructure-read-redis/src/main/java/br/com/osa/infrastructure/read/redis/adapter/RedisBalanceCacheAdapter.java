package br.com.osa.infrastructure.read.redis.adapter;

import br.com.port.read.BalanceCachePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class RedisBalanceCacheAdapter implements BalanceCachePort {

    private final RedisTemplate<String, String> redis;

    public RedisBalanceCacheAdapter(RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    @Override
    public void setBalance(UUID accountId, BigDecimal balance) {
        redis.opsForValue().set("balance:" + accountId, balance.toPlainString());
    }

    @Override
    public BigDecimal getBalance(UUID accountId) {
        String value = redis.opsForValue().get("balance:" + accountId);
        return value != null ? new BigDecimal(value) : null;
    }
}