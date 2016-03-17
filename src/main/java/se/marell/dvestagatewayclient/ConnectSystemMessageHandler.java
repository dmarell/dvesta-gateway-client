/*
 * Created by Daniel Marell 17/03/16.
 */
package se.marell.dvestagatewayclient;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

class ConnectSystemMessageHandler extends StompSessionHandlerAdapter {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SystemConnectMessage.class;
    }
}
