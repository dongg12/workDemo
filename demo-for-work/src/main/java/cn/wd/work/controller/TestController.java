package cn.wd.work.controller;

import cn.wd.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;

  @Autowired
  private RedisUtil redisUtil;

  @PostMapping("/test1")
  public void test1() {
    System.out.println(redisUtil.get("wang"));
  }

  @PostMapping("/test")
  public void test() {
    System.out.println(redisTemplate.opsForValue().get("wang"));
  }
}
