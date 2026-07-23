package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventBridgeFeedbackCreatedEventAdapterTest {

    @Test
    void publishesFeedbackCreatedEvent() {
        EventBridgeClient eventBridgeClient = mock(EventBridgeClient.class);
        when(eventBridgeClient.putEvents(any(PutEventsRequest.class))).thenReturn(mock(PutEventsResponse.class));
        EventBridgeFeedbackCreatedEventAdapter adapter = new EventBridgeFeedbackCreatedEventAdapter(eventBridgeClient);
        Feedback feedback = Feedback.restore(
                new br.com.fiap.techchallenge4.domain.valueobject.FeedbackId(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new FeedbackContent("Ótimo atendimento"),
                Urgency.CRITICAL,
                Instant.parse("2026-07-11T10:00:00Z")
        );

        adapter.publish(feedback);

        var captor = org.mockito.ArgumentCaptor.forClass(PutEventsRequest.class);
        verify(eventBridgeClient).putEvents(captor.capture());
        assertEquals(1, captor.getValue().entries().size());
        assertEquals("fiap.feedback", captor.getValue().entries().get(0).source());
        assertEquals("FeedbackCreated", captor.getValue().entries().get(0).detailType());
    }
}
