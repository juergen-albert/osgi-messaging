/*******************************************************************************
 * Copyright 2021 Amit Kumar Mondal
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
package in.bytehue.messaging.mqtt5.provider;

import static in.bytehue.messaging.mqtt5.provider.TestHelper.waitForRequestProcessing;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.service.messaging.Message;
import org.osgi.service.messaging.MessagePublisher;
import org.osgi.service.messaging.replyto.ReplyToManyPublisher;

import aQute.launchpad.Launchpad;
import aQute.launchpad.LaunchpadBuilder;
import aQute.launchpad.Service;
import aQute.launchpad.junit.LaunchpadRunner;
import in.bytehue.messaging.mqtt5.api.MqttMessageContextBuilder;

@RunWith(LaunchpadRunner.class)
public final class MessageReplyToManyPublisherTest {

    @Service
    private Launchpad launchpad;

    @Service
    private MessagePublisher publisher;

    @Service
    private ReplyToManyPublisher replyToPublisher;

    @Service
    private MqttMessageContextBuilder mcb;

    static LaunchpadBuilder builder = new LaunchpadBuilder().bndrun("test.bndrun").export("sun.misc");

    @Test
    public void test_publish_with_reply_many() throws Exception {
        final AtomicBoolean flag = new AtomicBoolean();

        final String reqChannel = "a/b";
        final String resChannel = "c/d";
        final String payload = "abc";
        final String stopPayload = "stop";

        // @formatter:off
        final Message message = mcb.channel(reqChannel)
                                   .replyTo(resChannel)
                                   .content(ByteBuffer.wrap(payload.getBytes()))
                                   .buildMessage();

        replyToPublisher.publishWithReplyMany(message).forEach(m -> flag.set(true));

        final Message reqMessage = mcb.channel(reqChannel)
                                      .content(ByteBuffer.wrap(payload.getBytes()))
                                      .buildMessage();

        final MessageProvider stopMessage = new MessageProvider();
        stopMessage.byteBuffer = ByteBuffer.wrap(stopPayload.getBytes());
        stopMessage.messageContext = new MessageContextProvider();
        // @formatter:on

        publisher.publish(reqMessage);
        publisher.publish(reqMessage);
        publisher.publish(reqMessage);

        publisher.publish(stopMessage, reqChannel);

        waitForRequestProcessing(flag);
    }

}
