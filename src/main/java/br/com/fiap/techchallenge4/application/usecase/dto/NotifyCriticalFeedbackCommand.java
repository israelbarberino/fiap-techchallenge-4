package br.com.fiap.techchallenge4.application.usecase.dto;

import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;

import java.time.Instant;
import java.util.Objects;

public record NotifyCriticalFeedbackCommand(FeedbackId feedbackId, String content, Urgency urgency, Instant createdAt) {
    public NotifyCriticalFeedbackCommand {
        Objects.requireNonNull(feedbackId, "feedbackId is required");
        Objects.requireNonNull(content, "content is required");
        Objects.requireNonNull(urgency, "urgency is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
    }
}