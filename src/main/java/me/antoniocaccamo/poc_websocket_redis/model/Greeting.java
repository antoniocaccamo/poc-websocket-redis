package me.antoniocaccamo.poc_websocket_redis.model;

import lombok.*;

@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor
public class Greeting implements AutoCloseable{

    private String player;

    private String content;

    @Override
    public void close() throws Exception {

    }
}
