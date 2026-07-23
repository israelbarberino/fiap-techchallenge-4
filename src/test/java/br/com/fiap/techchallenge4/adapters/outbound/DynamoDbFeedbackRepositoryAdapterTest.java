package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DynamoDbFeedbackRepositoryAdapterTest {

    @Test
    void savesFeedbackUsingExpectedItemShape() {
        DynamoDbClient dynamoDbClient = mock(DynamoDbClient.class);
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mock(PutItemResponse.class));
        DynamoDbFeedbackRepositoryAdapter adapter = new DynamoDbFeedbackRepositoryAdapter(dynamoDbClient, "Feedback");
        Feedback feedback = Feedback.restore(
                new FeedbackId(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new FeedbackContent("Ótimo atendimento"),
                Urgency.HIGH,
                Instant.parse("2026-07-11T10:00:00Z")
        );

        adapter.save(feedback);

        var captor = org.mockito.ArgumentCaptor.forClass(PutItemRequest.class);
        verify(dynamoDbClient).putItem(captor.capture());
        assertEquals("Feedback", captor.getValue().tableName());
        assertEquals("Ótimo atendimento", captor.getValue().item().get("content").s());
        assertEquals("HIGH", captor.getValue().item().get("urgency").s());
        assertNotNull(captor.getValue().item().get("createdAt").n());
    }

    @Test
    void mapsScanResultsBackToFeedback() {
        DynamoDbClient dynamoDbClient = mock(DynamoDbClient.class);
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(ScanResponse.builder()
                .items(List.of(item("LOW"), item("CRITICAL")))
                .build());
        DynamoDbFeedbackRepositoryAdapter adapter = new DynamoDbFeedbackRepositoryAdapter(dynamoDbClient, "Feedback");

        List<Feedback> feedbacks = adapter.findBetween(
                Instant.parse("2026-07-04T00:00:00Z"),
                Instant.parse("2026-07-11T00:00:00Z")
        );

        assertEquals(2, feedbacks.size());
        assertEquals(Urgency.LOW, feedbacks.get(0).urgency());
        assertEquals(Urgency.CRITICAL, feedbacks.get(1).urgency());
    }

    private static Map<String, AttributeValue> item(String urgency) {
        return Map.of(
                "feedbackId", AttributeValue.builder().s("123e4567-e89b-12d3-a456-426614174000").build(),
                "content", AttributeValue.builder().s("Feedback " + urgency).build(),
                "urgency", AttributeValue.builder().s(urgency).build(),
                "createdAt", AttributeValue.builder().n("1720173600000").build()
        );
    }
}
