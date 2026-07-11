package br.com.fiap.techchallenge4.application.port.in;

import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.GenerateWeeklyReportResult;

public interface GenerateWeeklyReportInputPort {
    GenerateWeeklyReportResult execute(GenerateWeeklyReportCommand command);
}