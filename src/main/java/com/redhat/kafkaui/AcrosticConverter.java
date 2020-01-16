package com.redhat.kafkaui;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.annotations.Broadcast;

/**
 * A bean consuming data from the "ui-topic" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */

@Dependent
public class AcrosticConverter {

	public static Logger logger = LoggerFactory.getLogger(AcrosticConverter.class);
    
	@ConfigProperty(name = "kafka.cert.path") String kafkaCertPath;
	@ConfigProperty(name = "mp.messaging.incoming.ui-topic.ssl.truststore.location") String kafkaTrustStoreLocation;
	@ConfigProperty(name = "mp.messaging.incoming.ui-topic.ssl.truststore.password") String kafkaTrustStorePassword;
	
	@Initialized(ApplicationScoped.class)
	void onStart(@Observes StartupEvent event) throws GeneralSecurityException, IOException { 
		TrustStore.createFromCrtFile(kafkaCertPath,
									 kafkaTrustStoreLocation,
									 kafkaTrustStorePassword.toCharArray());
	}
	
	@Incoming("ui-topic")
    @Outgoing("data-stream")
    @Broadcast
    public String process(String acrosticMessage) {
		byte[] decodedAcrosticBytes = Base64.getDecoder().decode(acrosticMessage);
		String decodedAcrosticJSON = new String(decodedAcrosticBytes);
		
		String htmlOutput = "";
		JsonElement jelement = new JsonParser().parse(decodedAcrosticJSON);
		JsonObject  jobject = jelement.getAsJsonObject();
		
		logger.info("Received new message: " + jobject.get("message").toString());
		htmlOutput += "<div><h3>Message:&nbsp<b>" + jobject.get("message").toString() + "</b></h3></div>";
		
		JsonArray jarray = jobject.getAsJsonArray("acrostic");
		htmlOutput += "<div><h3>Acrostic:</h3>";
		htmlOutput += "<div><table style='width: 50%; text-align: center' border='1pt'>"
					+ "<tr><th style='text-align: center'><h3>Letter</h3>" 
					+ "</th><th style='text-align: center'><h3>Word</h3></th></tr>";

		for (int i=0; i<jarray.size(); i++) {
			jobject = jarray.get(i).getAsJsonObject();
			htmlOutput += "<tr><td>" + jobject.get("letter").toString() + "</td><td>" +
							jobject.get("word").toString() + "</td></tr>";
			logger.info(jobject.get("letter").toString() + " -> " + jobject.get("word").toString());
			
		}
		htmlOutput += "</table></div></div>";

		return htmlOutput.replaceAll("\"", "");
    }

}
