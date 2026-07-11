package br.com.fiap.techchallenge4.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record FeedbackId(UUID value) {

    public FeedbackId {
        Objects.requireNonNull(value, "feedback id is required");
    }

    public static FeedbackId newId() {
        return new FeedbackId(UUID.randomUUID());
    }
}