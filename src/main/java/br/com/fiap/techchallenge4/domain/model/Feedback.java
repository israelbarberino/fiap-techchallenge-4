package br.com.fiap.techchallenge4.domain.model;

import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;

import java.time.Instant;
import java.util.Objects;

public final class Feedback {

    private final FeedbackId id;
    private final FeedbackContent content;
    private final Urgency urgency;
    private final Instant createdAt;

    private Feedback(FeedbackId id, FeedbackContent content, Urgency urgency, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.content = Objects.requireNonNull(content, "content is required");
        this.urgency = Objects.requireNonNull(urgency, "urgency is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
    }

    public static Feedback create(FeedbackContent content, Urgency urgency) {
        return new Feedback(FeedbackId.newId(), content, urgency, Instant.now());
    }

    public static Feedback restore(FeedbackId id, FeedbackContent content, Urgency urgency, Instant createdAt) {
        return new Feedback(id, content, urgency, createdAt);
    }

    public FeedbackId id() {
        return id;
    }

    public FeedbackContent content() {
        return content;
    }

    public Urgency urgency() {
        return urgency;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isCritical() {
        return Urgency.CRITICAL.equals(urgency);
    }
}