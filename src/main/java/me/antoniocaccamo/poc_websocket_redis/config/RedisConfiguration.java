package me.antoniocaccamo.poc_websocket_redis.config;

import me.antoniocaccamo.poc_websocket_redis.redis.MessagePublisher;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author antoniocaccamo on 27/05/2019.
 */

@Configuration
public class RedisConfiguration {


    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener(SimpMessagingTemplate template) {
        return new MessageListenerAdapter(
                new //ConsumerPlayerWebSocketController(template)
                        RedisMessageSubscriber(template)
        );
    }

    @Bean
    RedisMessageListenerContainer redisContainer(SimpMessagingTemplate template) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(template), patternTopic());

        return container;
    }

    @Bean
    MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), patternTopic());
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("players");
    }

    @Bean
    PatternTopic patternTopic() {
        return new PatternTopic("players:*");
    }
}
