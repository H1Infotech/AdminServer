package com.h1infotech.smarthive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import com.h1infotech.smarthive.service.BeeBoxWebSocketServer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisMessageConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //channel "sensorData"
        container.addMessageListener(listenerAdapter, new PatternTopic("sensorData"));
        //add more channels
        //container.addMessageListener(listenerAdapter, new PatternTopic("moewChannel"));
        return container;
    }
    
    @Bean
    MessageListenerAdapter listenerAdapter(BeeBoxWebSocketServer beeBoxWebSocketServer) {
        return new MessageListenerAdapter(beeBoxWebSocketServer, "receiveSensorDataMessage");
    }
    
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
	
}
