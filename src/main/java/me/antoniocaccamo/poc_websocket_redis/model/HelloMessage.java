package me.antoniocaccamo.poc_websocket_redis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString @NoArgsConstructor
public class HelloMessage implements AutoCloseable{

    private String player;

    private String name;

    @Override
    public void close() throws Exception {

    }
}
