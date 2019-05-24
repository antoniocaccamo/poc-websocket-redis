package me.antoniocaccamo.poc_websocket_redis.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * @author antonioaccamo on 24/05/2019.
 */
@Service
public class RedisMessagePublisher implements MessagePublisher {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(final String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
