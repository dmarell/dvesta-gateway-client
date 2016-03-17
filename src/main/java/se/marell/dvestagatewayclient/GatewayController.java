/*
 * Created by Daniel Marell 13/03/16.
 */
package se.marell.dvestagatewayclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Component
class GatewayController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private StompSession session;

    @Autowired
    private Environment environment;

    private String gatewayWebsockEndpoint;
    private String username;
    private String password;

    @Autowired
    private GatewayCommandListener commandListener;

    @PostConstruct
    public void init() {
        gatewayWebsockEndpoint = environment.getRequiredProperty("dvesta-gateway.websock-endpoint");
        username = environment.getRequiredProperty("dvesta-gateway.username");
        password = environment.getRequiredProperty("dvesta-gateway.password");
        reconnect();
    }

    @Scheduled(fixedRate = 10000)
    public void reconnect() {
        if (session == null || !session.isConnected()) {
            WebSocketClient transport = new SockJsClient(Arrays.asList((new WebSocketTransport(new StandardWebSocketClient()))));
            WebSocketStompClient client = new WebSocketStompClient(transport);
            client.setMessageConverter(new MappingJackson2MessageConverter());

            String encoding = new String(Base64.encode((username + ":" + password).getBytes()), Charset.forName("UTF-8"));
            WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
            wsHeaders.add("Authorization", "Basic " + encoding);

            log.info("Not connected, connecting...");
            try {
                StompHeaders headers = new StompHeaders();
                headers.setDestination("/app/system-connect");
                session = client.connect(gatewayWebsockEndpoint, wsHeaders, new ConnectSystemMessageHandler()).get();
                session.subscribe("/user/system-message-request", new StringMessageFrameHandler(session, commandListener));
                session.send(headers, new SystemConnectMessage(username));
                log.info("Connected");
            } catch (InterruptedException | ExecutionException e) {
                log.info("Failed to connect: {}", e.getMessage());
            }
        }
    }
}
