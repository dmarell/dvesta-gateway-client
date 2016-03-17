/*
 * Created by Daniel Marell 17/03/16.
 */
package se.marell.dvestagatewayclient;

public interface CommandResponseSender {
    void sendResponse(String messageId, String response);
}
