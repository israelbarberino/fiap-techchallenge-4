package br.com.fiap.techchallenge4.application.usecase.dto;

import java.time.Instant;
import java.util.Objects;

public record GenerateWeeklyReportCommand(Instant periodStartInclusive, Instant periodEndExclusive) {
    public GenerateWeeklyReportCommand {
        Objects.requireNonNull(periodStartInclusive, "periodStartInclusive is required");
        Objects.requireNonNull(periodEndExclusive, "periodEndExclusive is required");

        if (!periodStartInclusive.isBefore(periodEndExclusive)) {
            throw new IllegalArgumentException("periodStartInclusive must be before periodEndExclusive");
        }
    }
}