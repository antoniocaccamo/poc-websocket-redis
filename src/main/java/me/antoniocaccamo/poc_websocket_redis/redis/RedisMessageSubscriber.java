package me.antoniocaccamo.poc_websocket_redis.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author antonioaccamo on 24/05/2019.
 */
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<String>();

    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: " + new String(message.getBody()));
    }

}
