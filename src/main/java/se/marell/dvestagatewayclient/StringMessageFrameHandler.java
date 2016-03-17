/*
 * Created by Daniel Marell 08/11/15.
 */
package se.marell.dvestagatewayclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class StringMessageFrameHandler implements StompFrameHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private StompSession session;
    private GatewayCommandListener commandListener;
    private CommandResponseSender responseSender = new CommandResponseSender() {
        @Override
        public void sendResponse(String messageId, String response) {
            StompHeaders headers = new StompHeaders();
            headers.setDestination("/app/system-message-response");
            session.send(headers, new StringMessage(messageId, response));
        }
    };

    public StringMessageFrameHandler(StompSession session, GatewayCommandListener commandListener) {
        this.session = session;
        this.commandListener = commandListener;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return StringMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("handleFrame: payload: " + payload);
        StringMessage message = (StringMessage) payload;
        String[] tokens = message.getMessageBody().split(" ");
        if (tokens.length > 0) {
            String command = tokens[0];
            List<String> args = new ArrayList<>(Arrays.asList(tokens));
            args.remove(0);
            commandListener.command(responseSender, message.getMessageId(), command, args);
        }
    }
}
