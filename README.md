<img src="https://developers.redhat.com/blog/wp-content/uploads/2018/10/Untitled-drawing-4.png" data-canonical-src="https://developers.redhat.com/blog/wp-content/uploads/2018/10/Untitled-drawing-4.png" width="300" height="140" /> <img src="https://quarkus.io/assets/images/quarkus_logo_horizontal_rgb_reverse.svg" data-canonical-src="https://quarkus.io/assets/images/quarkus_logo_horizontal_rgb_reverse.svg" width="300" height="140" />

# Quarkus Kafka Consumer

This project illustrates how you can interact with Apache Kafka using MicroProfile Reactive Messaging.

This purpose of this service is to:
  1. Listen to a Kafka topic and consume messages
  2. Decode the input and push the output of the acrostic map to a reactive HTML app

---

## Instructions for deploying on OpenShift:
  1. Login to OpenShift:
```sh
oc login <openshift_cluster>
```
  2. Use existing Kafka project created during the deployment of the [kafka-consumer](https://github.com/roller1187/kafka-consumer) service:
```sh
oc new-project kafka-$(oc whoami)
```
  3. Deploy the service using s2i (Source-2-Image). Don't forget to provide a Kafka topic:
```sh
oc new-app --name quarkus-kafka-consumer \
    --image-stream openjdk-11-rhel8 \
    --build-env=ARTIFACT_COPY_ARGS="-p -r lib/ *-runner.jar" \
    --env=JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0" \
    https://github.com/roller1187/quarkus-kafka-consumer.git
```
  4. Create a route:
```sh
oc expose svc/quarkus-kafka-consumer
```
  5. Navigate to the route for the Quarkus UI and append **"/acrostic.html"** to the URL. The output should look like this:
  
![Quarkus Demo](https://github.com/roller1187/quarkus-kafka-consumer/blob/master/.screens/quarkus_demo.gif)

*Acrostic example:

![Acrostic](https://www.researchgate.net/profile/Andrew_Finch/publication/260593143/figure/fig3/AS:392472879484941@1470584234596/Acrostic-poem-Teaching-points-Spelling-Vocabulary-Dictionary-Holmes-Moulton-2001.png)
