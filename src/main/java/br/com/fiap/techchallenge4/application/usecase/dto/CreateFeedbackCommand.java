package br.com.fiap.techchallenge4.application.usecase.dto;

import br.com.fiap.techchallenge4.domain.model.Urgency;

import java.util.Objects;

public record CreateFeedbackCommand(String content, Urgency urgency) {
    public CreateFeedbackCommand {
        Objects.requireNonNull(content, "content is required");
        Objects.requireNonNull(urgency, "urgency is required");
    }
}