package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.NotifyCriticalFeedbackInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackCommand;
import br.com.fiap.techchallenge4.config.ApplicationModule;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CriticalNotificationHandler implements RequestHandler<Map<String, Object>, Void> {

    private final NotifyCriticalFeedbackInputPort useCase = ApplicationModule.notifyCriticalFeedbackUseCase();

    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        Map<String, Object> detail = asMap(event.get("detail"));
        NotifyCriticalFeedbackCommand command = new NotifyCriticalFeedbackCommand(
                new FeedbackId(UUID.fromString((String) detail.get("feedbackId"))),
                (String) detail.get("content"),
                Urgency.valueOf(((String) detail.get("urgency")).toUpperCase(Locale.ROOT)),
                Instant.parse((String) detail.get("createdAt"))
        );

        useCase.execute(command);
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("detail must be an object");
    }
}