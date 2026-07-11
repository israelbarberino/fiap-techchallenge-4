package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.application.port.out.FeedbackCreatedEventPort;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;

import java.util.Map;

public class EventBridgeFeedbackCreatedEventAdapter implements FeedbackCreatedEventPort {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final EventBridgeClient eventBridgeClient;

    public EventBridgeFeedbackCreatedEventAdapter() {
        this(EventBridgeClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build());
    }

    public EventBridgeFeedbackCreatedEventAdapter(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    @Override
    public void publish(Feedback feedback) {
        String detail = toJson(Map.of(
                "feedbackId", feedback.id().value().toString(),
                "content", feedback.content().value(),
                "urgency", feedback.urgency().name(),
                "createdAt", feedback.createdAt().toString()
        ));

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .source("fiap.feedback")
                .detailType("FeedbackCreated")
                .detail(detail)
                .build();

        eventBridgeClient.putEvents(PutEventsRequest.builder().entries(entry).build());
    }

    private String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("cannot serialize event detail");
        }
    }
}