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
public class PriceConverter {

//    private static final double CONVERSION_RATE = 0.88;

    @Incoming("ui-topic")
    @Outgoing("ui")
    @Broadcast
    public String process(String acrosticMessage) {
    	return "HELLO - " + acrosticMessage;
    }

}
