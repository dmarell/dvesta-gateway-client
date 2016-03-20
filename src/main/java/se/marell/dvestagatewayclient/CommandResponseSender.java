/*
 * Created by Daniel Marell 2016-03-16
 */
package se.marell.dvestagatewayclient;

import org.springframework.http.MediaType;

public interface CommandResponseSender {
    void sendResponse(String messageId, String response);

    void sendResponse(String messageId, int responseCode, MediaType mediaType, byte[] response);
}
