package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.GenerateWeeklyReportInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportCommand;
import br.com.fiap.techchallenge4.config.ApplicationModule;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.inject.Named;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Named("weekly-report")
public class WeeklyReportHandler implements RequestHandler<Map<String, Object>, Void> {

    private final GenerateWeeklyReportInputPort useCase;

    public WeeklyReportHandler() {
        this(ApplicationModule.generateWeeklyReportUseCase());
    }

    WeeklyReportHandler(GenerateWeeklyReportInputPort useCase) {
        this.useCase = useCase;
    }

    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        Instant periodEnd = Instant.now();
        Instant periodStart = periodEnd.minus(7, ChronoUnit.DAYS);
        useCase.execute(new GenerateWeeklyReportCommand(periodStart, periodEnd));
        return null;
    }
}