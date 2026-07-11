package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.in.CreateFeedbackInputPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackCreatedEventPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;

import java.util.Objects;

public class CreateFeedbackUseCase implements CreateFeedbackInputPort {

    private final FeedbackRepositoryPort feedbackRepository;
    private final FeedbackCreatedEventPort feedbackCreatedEvent;

    public CreateFeedbackUseCase(FeedbackRepositoryPort feedbackRepository, FeedbackCreatedEventPort feedbackCreatedEvent) {
        this.feedbackRepository = Objects.requireNonNull(feedbackRepository, "feedbackRepository is required");
        this.feedbackCreatedEvent = Objects.requireNonNull(feedbackCreatedEvent, "feedbackCreatedEvent is required");
    }

    @Override
    public CreateFeedbackResult execute(CreateFeedbackCommand command) {
        Objects.requireNonNull(command, "command is required");

        Feedback feedback = Feedback.create(new FeedbackContent(command.content()), command.urgency());
        feedbackRepository.save(feedback);
        feedbackCreatedEvent.publish(feedback);

        return new CreateFeedbackResult(feedback.id());
    }
}