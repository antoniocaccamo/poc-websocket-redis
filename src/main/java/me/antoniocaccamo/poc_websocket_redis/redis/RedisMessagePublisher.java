package me.antoniocaccamo.poc_websocket_redis.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * @author antonioaccamo on 24/05/2019.
 */
@Service @Slf4j
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
    public void publish(final String subTopic, final String message) {
        String tpc = null;
        if (StringUtils.isNotEmpty(subTopic)) {
            tpc = String.format("%s::%s", topic.getTopic(), subTopic);
        } else {
            tpc = topic.getTopic();
        }
        log.info("publish on topic {} => message {} ", tpc, message);
        redisTemplate.convertAndSend(tpc, message);
    }
}
