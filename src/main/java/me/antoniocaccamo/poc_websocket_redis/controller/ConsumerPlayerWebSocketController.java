package me.antoniocaccamo.poc_websocket_redis.controller;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import me.antoniocaccamo.poc_websocket_redis.model.HelloMessage;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller @Slf4j
public class ConsumerPlayerWebSocketController {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @MessageMapping("/consumer/players/{player}")
    @SendTo(  "/topic/consumer/players/{player}")
    public Greeting greeting(@DestinationVariable("player") String player, HelloMessage message) throws Exception {
        log.info("received message from player consumer {} : {}", player, message);
        Thread.sleep(1000); // simulated delay
        String s = String.format("Hello, %s(%s)!", player, message.getName());
        return new Greeting(player, s);
    }



}
