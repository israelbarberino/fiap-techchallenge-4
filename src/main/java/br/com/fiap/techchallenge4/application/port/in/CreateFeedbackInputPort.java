package br.com.fiap.techchallenge4.application.port.in;

import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackResult;

public interface CreateFeedbackInputPort {
    CreateFeedbackResult execute(CreateFeedbackCommand command);
}