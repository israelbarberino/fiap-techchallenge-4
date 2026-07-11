package br.com.fiap.techchallenge4.application.port.in;

import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.NotifyCriticalFeedbackResult;

public interface NotifyCriticalFeedbackInputPort {
    NotifyCriticalFeedbackResult execute(NotifyCriticalFeedbackCommand command);
}