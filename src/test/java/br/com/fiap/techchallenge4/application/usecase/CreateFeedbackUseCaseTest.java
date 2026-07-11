package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.out.FeedbackCreatedEventPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateFeedbackUseCaseTest {

    @Test
    void savesAndPublishesFeedback() {
        CapturingFeedbackRepository repository = new CapturingFeedbackRepository();
        CapturingFeedbackCreatedEvent eventPort = new CapturingFeedbackCreatedEvent();
        CreateFeedbackUseCase useCase = new CreateFeedbackUseCase(repository, eventPort);

        CreateFeedbackResult result = useCase.execute(new CreateFeedbackCommand("Atendimento excelente", Urgency.MEDIUM));

        assertNotNull(result.feedbackId());
        assertNotNull(repository.savedFeedback);
        assertEquals("Atendimento excelente", repository.savedFeedback.content().value());
        assertEquals(Urgency.MEDIUM, repository.savedFeedback.urgency());
        assertEquals(repository.savedFeedback, eventPort.publishedFeedback);
        assertEquals(result.feedbackId(), repository.savedFeedback.id());
    }

    private static final class CapturingFeedbackRepository implements FeedbackRepositoryPort {
        private Feedback savedFeedback;

        @Override
        public void save(Feedback feedback) {
            savedFeedback = feedback;
        }

        @Override
        public List<Feedback> findBetween(Instant startInclusive, Instant endExclusive) {
            return List.of();
        }
    }

    private static final class CapturingFeedbackCreatedEvent implements FeedbackCreatedEventPort {
        private Feedback publishedFeedback;

        @Override
        public void publish(Feedback feedback) {
            publishedFeedback = feedback;
        }
    }
}
