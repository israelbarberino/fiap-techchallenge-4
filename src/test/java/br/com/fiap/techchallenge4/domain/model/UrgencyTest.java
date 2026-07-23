package br.com.fiap.techchallenge4.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrgencyTest {

    @Test
    void exposesExpectedValues() {
        assertEquals(4, Urgency.values().length);
        assertEquals(Urgency.CRITICAL, Urgency.valueOf("CRITICAL"));
    }
}
