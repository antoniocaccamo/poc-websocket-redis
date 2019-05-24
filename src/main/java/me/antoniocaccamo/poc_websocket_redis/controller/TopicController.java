package me.antoniocaccamo.poc_websocket_redis.controller;

import me.antoniocaccamo.poc_websocket_redis.model.Greeting;
import me.antoniocaccamo.poc_websocket_redis.model.HelloMessage;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TopicController {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        String s = String.format("Hello, %s!", message.getName());
        redisMessagePublisher.publish(s);
        return new Greeting(s);
    }



}
