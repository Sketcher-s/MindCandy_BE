package shop.catchmind.auth.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static shop.catchmind.auth.constant.JwtConstant.REDIS_LOGOUT_KEY;

@Component
@RequiredArgsConstructor
public class RedisProvider {

    private final RedisTemplate<String, String> redisTemplate;

    public void registerBlackList(final String accessToken, final Long remainingExpirationTime) {
        redisTemplate.opsForValue().set(REDIS_LOGOUT_KEY, accessToken, remainingExpirationTime, TimeUnit.SECONDS);
    }

    public boolean isNotTokenBlacklisted(final String accessToken) {
        return Boolean.FALSE.equals(redisTemplate.hasKey(REDIS_LOGOUT_KEY + accessToken));
    }

}
