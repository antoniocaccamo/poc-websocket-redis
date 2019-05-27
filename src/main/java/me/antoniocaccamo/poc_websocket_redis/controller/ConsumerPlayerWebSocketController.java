package me.antoniocaccamo.poc_websocket_redis.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.poc_websocket_redis.handler.MyTextWebSocketHandler;
import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import me.antoniocaccamo.poc_websocket_redis.model.HelloMessage;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
@Slf4j @AllArgsConstructor
public class ConsumerPlayerWebSocketController {

    private final SimpMessagingTemplate template;


    @MessageMapping("/consumer/players/{player}")
    @SendTo(  "/topic/consumer/players/{player}")
    public Greeting greeting(@DestinationVariable("player") String player, HelloMessage message) throws Exception {
        log.info("received message from player consumer {} : {}", player, message);
        Thread.sleep(1000); // simulated delay
        String s = String.format("Hello, %s(%s)!", player, message.getName());
        return new Greeting(player, s);
    }



}
