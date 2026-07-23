package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.out.CriticalNotificationPort;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotifyCriticalFeedbackUseCaseTest {

    @Test
    void doesNotNotifyWhenFeedbackIsNotCritical() {
        CapturingCriticalNotificationPort notificationPort = new CapturingCriticalNotificationPort();
        NotifyCriticalFeedbackUseCase useCase = new NotifyCriticalFeedbackUseCase(notificationPort);

        NotifyCriticalFeedbackResult result = useCase.execute(new NotifyCriticalFeedbackCommand(
                new FeedbackId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                "Atendimento bom",
                Urgency.HIGH,
                Instant.parse("2026-07-11T10:00:00Z")
        ));

        assertFalse(result.notificationSent());
        assertFalse(notificationPort.notified);
    }

    @Test
    void notifiesWhenFeedbackIsCritical() {
        CapturingCriticalNotificationPort notificationPort = new CapturingCriticalNotificationPort();
        NotifyCriticalFeedbackUseCase useCase = new NotifyCriticalFeedbackUseCase(notificationPort);

        NotifyCriticalFeedbackResult result = useCase.execute(new NotifyCriticalFeedbackCommand(
                new FeedbackId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                "Falha grave",
                Urgency.CRITICAL,
                Instant.parse("2026-07-11T10:00:00Z")
        ));

        assertTrue(result.notificationSent());
        assertTrue(notificationPort.notified);
    }

    private static final class CapturingCriticalNotificationPort implements CriticalNotificationPort {
        private boolean notified;

        @Override
        public void notify(Feedback feedback) {
            notified = true;
        }
    }
}
