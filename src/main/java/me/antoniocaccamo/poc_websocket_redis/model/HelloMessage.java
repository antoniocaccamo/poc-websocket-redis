package me.antoniocaccamo.poc_websocket_redis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
public class HelloMessage {

    private String player;

    private String name;

}
