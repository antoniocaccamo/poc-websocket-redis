package me.antoniocaccamo.poc_websocket_redis.redis;

/**
 * @author antoniocaccamo on 24/05/2019.
 */
public interface MessagePublisher {

    public void publish(final String subtopic, final String message) ;
}
