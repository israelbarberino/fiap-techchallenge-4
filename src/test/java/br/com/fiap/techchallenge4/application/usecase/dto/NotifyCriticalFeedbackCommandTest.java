package br.com.fiap.techchallenge4.application.usecase.dto;

import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotifyCriticalFeedbackCommandTest {

    @Test
    void storesAllValues() {
        Instant createdAt = Instant.parse("2026-07-11T10:00:00Z");
        NotifyCriticalFeedbackCommand command = new NotifyCriticalFeedbackCommand(
                new FeedbackId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                "Falha crítica",
                Urgency.CRITICAL,
                createdAt
        );

        assertEquals(Urgency.CRITICAL, command.urgency());
        assertEquals(createdAt, command.createdAt());
    }

    @Test
    void rejectsNullFeedbackId() {
        assertThrows(NullPointerException.class, () -> new NotifyCriticalFeedbackCommand(null, "a", Urgency.LOW, Instant.now()));
    }
}
