package cn.wd.work.config;

import cn.wd.common.config.RedisConfig;
import cn.wd.common.util.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Import({RedisConfig.class})
public class RedisConfiguration {
}
