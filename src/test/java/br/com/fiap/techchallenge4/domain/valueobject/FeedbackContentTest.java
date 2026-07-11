package br.com.fiap.techchallenge4.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackContentTest {

    @Test
    void trimsContent() {
        FeedbackContent content = new FeedbackContent("  Atendimento excelente  ");

        assertEquals("Atendimento excelente", content.value());
    }

    @Test
    void rejectsShortContent() {
        assertThrows(IllegalArgumentException.class, () -> new FeedbackContent("ok"));
    }

    @Test
    void rejectsNullContent() {
        assertThrows(NullPointerException.class, () -> new FeedbackContent(null));
    }
}
