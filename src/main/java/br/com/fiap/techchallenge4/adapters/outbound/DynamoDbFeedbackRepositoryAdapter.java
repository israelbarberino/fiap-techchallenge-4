package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DynamoDbFeedbackRepositoryAdapter implements FeedbackRepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public DynamoDbFeedbackRepositoryAdapter() {
        this(
            DynamoDbClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build(),
            System.getenv().getOrDefault("FEEDBACK_TABLE", "Feedback")
        );
    }

    public DynamoDbFeedbackRepositoryAdapter(DynamoDbClient dynamoDbClient, String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    @Override
    public void save(Feedback feedback) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("feedbackId", AttributeValue.builder().s(feedback.id().value().toString()).build());
        item.put("content", AttributeValue.builder().s(feedback.content().value()).build());
        item.put("urgency", AttributeValue.builder().s(feedback.urgency().name()).build());
        item.put("createdAt", AttributeValue.builder().n(String.valueOf(feedback.createdAt().toEpochMilli())).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());
    }

    @Override
    public List<Feedback> findBetween(Instant startInclusive, Instant endExclusive) {
        Map<String, String> attributeNames = Map.of("#createdAt", "createdAt");
        Map<String, AttributeValue> attributeValues = Map.of(
                ":start", AttributeValue.builder().n(String.valueOf(startInclusive.toEpochMilli())).build(),
                ":end", AttributeValue.builder().n(String.valueOf(endExclusive.toEpochMilli())).build()
        );

        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .filterExpression("#createdAt >= :start AND #createdAt < :end")
                .expressionAttributeNames(attributeNames)
                .expressionAttributeValues(attributeValues)
                .build();

        return dynamoDbClient.scan(request).items().stream()
                .map(this::toFeedback)
                .toList();
    }

    private Feedback toFeedback(Map<String, AttributeValue> item) {
        FeedbackId id = new FeedbackId(UUID.fromString(item.get("feedbackId").s()));
        FeedbackContent content = new FeedbackContent(item.get("content").s());
        Urgency urgency = Urgency.valueOf(item.get("urgency").s());
        Instant createdAt = Instant.ofEpochMilli(Long.parseLong(item.get("createdAt").n()));
        return Feedback.restore(id, content, urgency, createdAt);
    }
}