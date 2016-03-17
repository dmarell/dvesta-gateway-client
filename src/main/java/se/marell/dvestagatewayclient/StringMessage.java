/*
 * Created by Daniel Marell 03/03/16.
 */
package se.marell.dvestagatewayclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class StringMessage {
    private String messageId;
    private String messageBody;
}
