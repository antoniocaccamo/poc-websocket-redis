package me.antoniocaccamo.poc_websocket_redis.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import me.antoniocaccamo.poc_websocket_redis.model.HelloMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author antonioaccamo on 24/05/2019.
 */
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<String>();

    private final SimpMessagingTemplate template;

    public RedisMessageSubscriber(SimpMessagingTemplate template) {
        this.template = template;
    }


    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: " + new String(message.getBody()));
        try (
            HelloMessage hm =
                new ObjectMapper().readValue(message.getBody(), HelloMessage.class)){

            Greeting greeting = new Greeting(hm.getPlayer(), hm.getName());

            template.convertAndSend("/topic/producer/players/10", greeting);
            template.convertAndSend("/topic/consumer/players/10", greeting);

            log.info("message sent : {}", greeting);


        } catch (Exception e) {
            log.error("error occurred", e);
        }
    }

}
