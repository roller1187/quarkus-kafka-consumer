package org.acme.quarkus.sample;

import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import io.smallrye.reactive.messaging.annotations.Broadcast;

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
		byte[] decodedAcrosticBytes = Base64.getDecoder().decode(acrosticMessage);
		String decodedAcrosticJSON = new String(decodedAcrosticBytes);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJSONString = gson.toJson(new JsonParser().parse(decodedAcrosticJSON));
		String htmlJSONString;
		
		htmlJSONString = prettyJSONString.replaceAll(" ", "&nbsp;");
		htmlJSONString = htmlJSONString.replaceAll("(\r\n|\n)", "<br />");
		
		return htmlJSONString;
    }

}
