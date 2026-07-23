package br.com.fiap.techchallenge4.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackContentMaxLengthTest {

    @Test
    void rejectsTooLongContent() {
        String tooLong = "a".repeat(2001);

        assertThrows(IllegalArgumentException.class, () -> new FeedbackContent(tooLong));
    }
}
