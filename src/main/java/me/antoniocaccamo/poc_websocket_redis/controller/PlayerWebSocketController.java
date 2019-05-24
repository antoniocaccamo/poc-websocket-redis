package me.antoniocaccamo.poc_websocket_redis.controller;

import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import me.antoniocaccamo.poc_websocket_redis.model.HelloMessage;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.websocket.server.PathParam;

@Controller
public class PlayerWebSocketController {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @MessageMapping("/players/{player}")
    @SendTo(  "/topic/players/{player}")
    public Greeting greeting(@DestinationVariable("player") String player, HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        String s = String.format("Hello, %s(%s)!", player, message.getName());
        redisMessagePublisher.publish(player, s);
        return new Greeting(s);
    }



}
