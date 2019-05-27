package me.antoniocaccamo.poc_websocket_redis.config;

import me.antoniocaccamo.poc_websocket_redis.controller.ConsumerPlayerWebSocketController;
import me.antoniocaccamo.poc_websocket_redis.redis.MessagePublisher;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessagePublisher;
import me.antoniocaccamo.poc_websocket_redis.redis.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.servlet.Filter;

/**
 * @author antonioaccamo
 */

//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    /*
//     * limit message size
//     */
//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(8192);
//        container.setMaxBinaryMessageBufferSize(8192);
//        return container;
//    }
//
//
//
//    @Bean
//    public WebSocketHandler myHandler() {
//        return new MyTextWebSocketHandler();
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//
//    }
//}

@Configuration @ComponentScan("me.antoniocaccamo.poc_websocket_redis")
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/js-websocket-producer")
                .withSockJS()
        ;

        registry
                .addEndpoint("/js-websocket-consumer")
                .withSockJS()
        ;
    }



    @Bean(name = "TeeFilter")
    public Filter teeFilter() {
        return new ch.qos.logback.access.servlet.TeeFilter();
    }

//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory
//
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//
//        // put logback-access.xml in src/main/resources/conf
//        tomcat.addContextValves(new LogbackValve());
//
//        return tomcat;
//    }


    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener(SimpMessagingTemplate template) {
        return new MessageListenerAdapter(
                new //ConsumerPlayerWebSocketController(template)
                        RedisMessageSubscriber(template)
        );
    }

    @Bean
    RedisMessageListenerContainer redisContainer(SimpMessagingTemplate template) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(template), patternTopic());

        return container;
    }

    @Bean
    MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), patternTopic());
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:players");
    }

    @Bean
    PatternTopic patternTopic(){
        return new PatternTopic("pubsub:players*");
    }

}
