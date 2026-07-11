package br.com.fiap.techchallenge4.application.usecase.dto;

import java.time.Instant;

public record WeeklyReport(
        Instant periodStartInclusive,
        Instant periodEndExclusive,
        long total,
        long low,
        long medium,
        long high,
        long critical
) {
}