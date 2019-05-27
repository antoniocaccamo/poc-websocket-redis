package me.antoniocaccamo.poc_websocket_redis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString @NoArgsConstructor
public class HelloMessage implements AutoCloseable{

    private String player;

    @JsonIgnore
    private String type;

    private String name;

    @Override
    public void close() throws Exception {

    }
}
