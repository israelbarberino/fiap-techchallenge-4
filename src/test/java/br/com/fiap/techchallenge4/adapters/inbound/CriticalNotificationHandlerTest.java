package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.NotifyCriticalFeedbackInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CriticalNotificationHandlerTest {

    @Test
    void delegatesCriticalEventDetailToUseCase() {
        CapturingNotifyCriticalFeedbackInputPort useCase = new CapturingNotifyCriticalFeedbackInputPort();
        CriticalNotificationHandler handler = new CriticalNotificationHandler(useCase);
        Map<String, Object> event = Map.of(
                "detail", Map.of(
                        "feedbackId", "123e4567-e89b-12d3-a456-426614174000",
                        "content", "Falha grave",
                        "urgency", "critical",
                        "createdAt", "2026-07-11T10:00:00Z"
                )
        );

        Void result = handler.handleRequest(event, mock(Context.class));

        assertNull(result);
        assertTrue(useCase.invoked);
        assertEquals("Falha grave", useCase.command.content());
        assertEquals(Urgency.CRITICAL, useCase.command.urgency());
    }

    private static final class CapturingNotifyCriticalFeedbackInputPort implements NotifyCriticalFeedbackInputPort {
        private boolean invoked;
        private NotifyCriticalFeedbackCommand command;

        @Override
        public NotifyCriticalFeedbackResult execute(NotifyCriticalFeedbackCommand command) {
            invoked = true;
            this.command = command;
            return new NotifyCriticalFeedbackResult(true);
        }
    }
}
