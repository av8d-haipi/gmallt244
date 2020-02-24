package com.lxkj.gmall.gmall_redisson_test.redissontest;


import com.lxkj.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class RedissonController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("testRedisson")
    @ResponseBody
    public String testRedisson(){
        //声明锁
        RLock lock = redissonClient.getLock("lock");
        Jedis jedis =redisUtil.getJedis();
        lock.lock();//上锁
        try{
            String v = jedis.get("k");
            if(StringUtils.isBlank(v)){
                v = "1";
            }
            System.out.println("-->"+v);
            jedis.set("k",(Integer.parseInt(v)+1)+"");
        }finally {
            jedis.close();
            lock.unlock();//解锁
        }
        return "success";
    }
}
