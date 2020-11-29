/*******************************************************************************
 * Copyright 2020 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package in.bytehue.messaging.mqtt5.example;

import java.nio.ByteBuffer;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.messaging.Features;
import org.osgi.service.messaging.Message;
import org.osgi.service.messaging.MessageContextBuilder;
import org.osgi.service.messaging.MessagePublisher;

@Component
public final class Mqtt5LastWillExample {

    @Reference(target = "(osgi.messaging.protocol=mqtt5)")
    private MessagePublisher mqttPublisher;

    @Reference(target = "(osgi.messaging.protocol=mqtt5)")
    private ComponentServiceObjects<MessageContextBuilder> mcbFactory;

    public void publishMessage() {
        final MessageContextBuilder mcb = mcbFactory.getService();
        try {
            // @formatter:off
            final Message lastWill =
                    mcb.channel("last-will-channel")
                       .content(ByteBuffer.wrap("EXIT".getBytes()))
                       .extensionEntry(Features.EXTENSION_QOS, "2")
                       .extensionEntry(Features.EXTENSION_LAST_WILL, Boolean.TRUE)
                       .buildMessage();
            // @formatter:on
            mqttPublisher.publish(lastWill);
        } finally {
            mcbFactory.ungetService(mcb);
        }
    }

}