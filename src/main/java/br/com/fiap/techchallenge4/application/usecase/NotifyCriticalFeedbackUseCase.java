package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.in.NotifyCriticalFeedbackInputPort;
import br.com.fiap.techchallenge4.application.port.out.CriticalNotificationPort;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;

import java.util.Objects;

public class NotifyCriticalFeedbackUseCase implements NotifyCriticalFeedbackInputPort {

    private final CriticalNotificationPort criticalNotificationPort;

    public NotifyCriticalFeedbackUseCase(CriticalNotificationPort criticalNotificationPort) {
        this.criticalNotificationPort = Objects.requireNonNull(criticalNotificationPort, "criticalNotificationPort is required");
    }

    @Override
    public NotifyCriticalFeedbackResult execute(NotifyCriticalFeedbackCommand command) {
        Objects.requireNonNull(command, "command is required");

        Feedback feedback = Feedback.restore(
                command.feedbackId(),
                new FeedbackContent(command.content()),
                command.urgency(),
                command.createdAt()
        );

        if (!feedback.isCritical()) {
            return new NotifyCriticalFeedbackResult(false);
        }

        criticalNotificationPort.notify(feedback);
        return new NotifyCriticalFeedbackResult(true);
    }
}