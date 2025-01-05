package cn.wd.work.config;

import cn.wd.common.config.RedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisConfig.class})
public class RedisConfiguration {
}
