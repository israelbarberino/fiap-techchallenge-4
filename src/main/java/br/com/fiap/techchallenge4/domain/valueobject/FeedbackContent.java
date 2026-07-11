package br.com.fiap.techchallenge4.domain.valueobject;

import java.util.Objects;

public record FeedbackContent(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 2000;

    public FeedbackContent {
        Objects.requireNonNull(value, "feedback content is required");
        String normalized = value.trim();

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("feedback content must have at least " + MIN_LENGTH + " characters");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("feedback content must have at most " + MAX_LENGTH + " characters");
        }

        value = normalized;
    }
}