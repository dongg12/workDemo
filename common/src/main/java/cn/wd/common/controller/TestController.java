package cn.wd.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;

  @PostMapping("/test")
  public void test() {
    redisTemplate.opsForValue().set("wang","123");
    System.out.println(redisTemplate.opsForValue().get("wang"));
  }
}
