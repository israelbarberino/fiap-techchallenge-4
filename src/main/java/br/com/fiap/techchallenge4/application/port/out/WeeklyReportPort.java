package br.com.fiap.techchallenge4.application.port.out;

import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;

public interface WeeklyReportPort {
    void publish(WeeklyReport report);
}