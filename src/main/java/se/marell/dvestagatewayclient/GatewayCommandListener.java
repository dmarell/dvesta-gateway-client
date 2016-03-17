/*
 * Created by Daniel Marell 17/03/16.
 */
package se.marell.dvestagatewayclient;

import java.util.List;

public interface GatewayCommandListener {
    void command(CommandResponseSender sender, String messageId, String commandName, List<String> commandArgs);
}
