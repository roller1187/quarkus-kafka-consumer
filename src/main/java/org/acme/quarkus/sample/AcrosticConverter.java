package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "ui-topic" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class AcrosticConverter {

	@Incoming("ui-topic")
    @Outgoing("data-stream")
    @Broadcast
    public String process(String acrosticMessage) {
    	return acrosticMessage;
    }

}
