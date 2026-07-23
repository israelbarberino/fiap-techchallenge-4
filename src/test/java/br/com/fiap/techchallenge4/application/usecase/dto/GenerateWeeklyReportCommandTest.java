package br.com.fiap.techchallenge4.application.usecase.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GenerateWeeklyReportCommandTest {

    @Test
    void storesWindow() {
        Instant start = Instant.parse("2026-07-04T00:00:00Z");
        Instant end = Instant.parse("2026-07-11T00:00:00Z");

        GenerateWeeklyReportCommand command = new GenerateWeeklyReportCommand(start, end);

        assertEquals(start, command.periodStartInclusive());
        assertEquals(end, command.periodEndExclusive());
    }

    @Test
    void rejectsInvalidWindow() {
        Instant now = Instant.parse("2026-07-11T00:00:00Z");
        assertThrows(IllegalArgumentException.class, () -> new GenerateWeeklyReportCommand(now, now));
    }
}
