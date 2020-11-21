package org.osgi.service.messaging.acknowledge;

import org.osgi.service.messaging.MessageContext;

public interface AcknowledgeMessageContext extends MessageContext {

    /**
     * Returns the state, if a message was acknowledge or not or none of them.
     *
     * @return the {@link AcknowledgeType}
     */
    AcknowledgeType getAcknowledgeState();

    /**
     * Returns the acknowledge handler
     *
     * @return the acknowledge handler instance
     */
    AcknowledgeHandler getAcknowledgeHandler();

}