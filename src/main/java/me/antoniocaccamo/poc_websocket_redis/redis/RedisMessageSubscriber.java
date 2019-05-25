package me.antoniocaccamo.poc_websocket_redis.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.net.URL;
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
        try (
            Greeting greeting =
                new ObjectMapper().readValue(message.getBody(), Greeting.class)){

            WebSocketClient client = new StandardWebSocketClient();

            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            String url = String.format("app/consumer/players/%s", greeting.getPlayer());

            stompClient.connect( "ws://localhost:8080/js-websocket-consumer" , new StompSessionHandler() {
                @Override
                public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
                    log.info("New session established : " + stompSession.getSessionId());
                    stompSession.subscribe(String.format("/topic/consumer/players/%s", greeting.getPlayer()), this);
                    log.info("Subscribed to /topic/messages");
                    stompSession.send(String.format("app/consumer/players/%s", greeting.getPlayer()), greeting);
                    log.info("Message sent to websocket server");
                }

                @Override
                public void handleException(StompSession stompSession, @Nullable StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
                    log.error("error occurred", throwable);
                }

                @Override
                public void handleTransportError(StompSession stompSession, Throwable throwable) {
                    log.error("error occurred", throwable);
                }

                @Override
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return Greeting.class;
                }

                @Override
                public void handleFrame(StompHeaders stompHeaders, @Nullable Object object) {
                    log.info("object : {}", object);
                }
            });

        } catch (Exception e) {
            log.error("error occurred", e);
        }
    }

}
