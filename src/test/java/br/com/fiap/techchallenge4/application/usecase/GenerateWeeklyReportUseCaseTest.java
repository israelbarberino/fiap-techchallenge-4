package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.application.port.out.WeeklyReportPort;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportResult;
import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenerateWeeklyReportUseCaseTest {

    @Test
    void countsFeedbackByUrgencyAndPublishesReport() {
        Instant start = Instant.parse("2026-07-04T00:00:00Z");
        Instant end = Instant.parse("2026-07-11T00:00:00Z");
        List<Feedback> feedbacks = List.of(
                feedback(Urgency.LOW),
                feedback(Urgency.MEDIUM),
                feedback(Urgency.MEDIUM),
                feedback(Urgency.HIGH),
                feedback(Urgency.CRITICAL)
        );

        CapturingFeedbackRepository repository = new CapturingFeedbackRepository(feedbacks);
        CapturingWeeklyReportPort reportPort = new CapturingWeeklyReportPort();
        GenerateWeeklyReportUseCase useCase = new GenerateWeeklyReportUseCase(repository, reportPort);

        GenerateWeeklyReportResult result = useCase.execute(new GenerateWeeklyReportCommand(start, end));

        assertNotNull(result.report());
        assertEquals(5, result.report().total());
        assertEquals(1, result.report().low());
        assertEquals(2, result.report().medium());
        assertEquals(1, result.report().high());
        assertEquals(1, result.report().critical());
        assertEquals(result.report(), reportPort.publishedReport);
    }

    private static Feedback feedback(Urgency urgency) {
        return Feedback.restore(
                new FeedbackId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new FeedbackContent("Feedback para " + urgency.name()),
                urgency,
                Instant.parse("2026-07-08T10:00:00Z")
        );
    }

    private static final class CapturingFeedbackRepository implements FeedbackRepositoryPort {
        private final List<Feedback> feedbacks;

        private CapturingFeedbackRepository(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
        }

        @Override
        public void save(Feedback feedback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Feedback> findBetween(Instant startInclusive, Instant endExclusive) {
            return feedbacks;
        }
    }

    private static final class CapturingWeeklyReportPort implements WeeklyReportPort {
        private WeeklyReport publishedReport;

        @Override
        public void publish(WeeklyReport report) {
            publishedReport = report;
        }
    }
}
