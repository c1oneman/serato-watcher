package org.ssldev.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.ssldev.api.app.SslApi;
import org.ssldev.api.web.TrackStatusWebSocketHandler;
import org.ssldev.core.utils.Logger;

@SpringBootApplication
@ComponentScan(basePackages = {"org.ssldev.api", "org.ssldev.api.web"})
public class SslApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SslApiApplication.class, args);
    }

    @Bean
    public SslApi sslApi(TrackStatusWebSocketHandler webSocketHandler) {
        Logger.info(this, "Creating SslApi bean with WebSocket handler");
        SslApi api = new SslApi();
        api.setWebSocketHandler(webSocketHandler);
        api.start();
        return api;
    }
} 