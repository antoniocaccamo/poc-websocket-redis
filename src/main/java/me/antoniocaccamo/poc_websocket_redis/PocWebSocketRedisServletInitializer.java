package me.antoniocaccamo.poc_websocket_redis;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * @author antoniocaccamo on 27/05/2019.
 */
public class PocWebSocketRedisServletInitializer extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PocWebsocketRedisApplication.class);
    }
}
