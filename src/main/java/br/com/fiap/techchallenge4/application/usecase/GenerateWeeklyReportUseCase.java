package br.com.fiap.techchallenge4.application.usecase;

import br.com.fiap.techchallenge4.application.port.in.GenerateWeeklyReportInputPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.application.port.out.WeeklyReportPort;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportResult;
import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;

import java.util.List;
import java.util.Objects;

public class GenerateWeeklyReportUseCase implements GenerateWeeklyReportInputPort {

    private final FeedbackRepositoryPort feedbackRepository;
    private final WeeklyReportPort weeklyReportPort;

    public GenerateWeeklyReportUseCase(FeedbackRepositoryPort feedbackRepository, WeeklyReportPort weeklyReportPort) {
        this.feedbackRepository = Objects.requireNonNull(feedbackRepository, "feedbackRepository is required");
        this.weeklyReportPort = Objects.requireNonNull(weeklyReportPort, "weeklyReportPort is required");
    }

    @Override
    public GenerateWeeklyReportResult execute(GenerateWeeklyReportCommand command) {
        Objects.requireNonNull(command, "command is required");

        List<Feedback> feedbacks = feedbackRepository.findBetween(command.periodStartInclusive(), command.periodEndExclusive());

        long low = countByUrgency(feedbacks, Urgency.LOW);
        long medium = countByUrgency(feedbacks, Urgency.MEDIUM);
        long high = countByUrgency(feedbacks, Urgency.HIGH);
        long critical = countByUrgency(feedbacks, Urgency.CRITICAL);

        WeeklyReport report = new WeeklyReport(
                command.periodStartInclusive(),
                command.periodEndExclusive(),
                feedbacks.size(),
                low,
                medium,
                high,
                critical
        );

        weeklyReportPort.publish(report);

        return new GenerateWeeklyReportResult(report);
    }

    private long countByUrgency(List<Feedback> feedbacks, Urgency urgency) {
        return feedbacks.stream().filter(feedback -> feedback.urgency() == urgency).count();
    }
}