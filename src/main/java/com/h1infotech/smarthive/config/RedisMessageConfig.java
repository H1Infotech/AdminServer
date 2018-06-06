package com.h1infotech.smarthive.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisMessageConfig {

//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
        //channel "sensorData"
//        container.addMessageListener(listenerAdapter, new PatternTopic("sensorData"));
        //add more channels
        //container.addMessageListener(listenerAdapter, new PatternTopic("moewChannel"));
//        return container;
//    }
//    
//    @Bean
//    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
//        return new StringRedisTemplate(connectionFactory);
//    }
	
}
