package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.GenerateWeeklyReportInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportResult;
import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class WeeklyReportHandlerTest {

    @Test
    void computesLastSevenDaysWindow() {
        CapturingGenerateWeeklyReportInputPort useCase = new CapturingGenerateWeeklyReportInputPort();
        WeeklyReportHandler handler = new WeeklyReportHandler(useCase);

        Void result = handler.handleRequest(Map.of(), mock(Context.class));

        assertNull(result);
        assertTrue(useCase.invoked);
        assertEquals(Duration.ofDays(7), Duration.between(useCase.command.periodStartInclusive(), useCase.command.periodEndExclusive()));
    }

    private static final class CapturingGenerateWeeklyReportInputPort implements GenerateWeeklyReportInputPort {
        private boolean invoked;
        private GenerateWeeklyReportCommand command;

        @Override
        public GenerateWeeklyReportResult execute(GenerateWeeklyReportCommand command) {
            invoked = true;
            this.command = command;
            return new GenerateWeeklyReportResult(new WeeklyReport(command.periodStartInclusive(), command.periodEndExclusive(), 0, 0, 0, 0, 0));
        }
    }
}
