package com.lcydl.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 手动配置Ribbon负载均衡策略为随机
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule main() {
        return new RandomRule(); // 随机
    }

}
