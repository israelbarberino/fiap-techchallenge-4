package br.com.fiap.techchallenge4.domain.model;

import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackTest {

    @Test
    void createsFeedbackWithGeneratedIdAndTimestamp() {
        Feedback feedback = Feedback.create(new FeedbackContent("Atendimento excelente"), Urgency.LOW);

        assertNotNull(feedback.id());
        assertEquals("Atendimento excelente", feedback.content().value());
        assertEquals(Urgency.LOW, feedback.urgency());
        assertNotNull(feedback.createdAt());
        assertFalse(feedback.isCritical());
    }

    @Test
    void restoresCriticalFeedback() {
        Instant createdAt = Instant.parse("2026-07-11T10:00:00Z");
        Feedback feedback = Feedback.restore(
                new FeedbackId(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new FeedbackContent("Falha grave no atendimento"),
                Urgency.CRITICAL,
                createdAt
        );

        assertEquals(Urgency.CRITICAL, feedback.urgency());
        assertEquals(createdAt, feedback.createdAt());
        assertTrue(feedback.isCritical());
    }
}
