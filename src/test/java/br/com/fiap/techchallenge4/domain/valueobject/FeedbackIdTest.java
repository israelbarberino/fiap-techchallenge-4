package br.com.fiap.techchallenge4.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackIdTest {

    @Test
    void generatesIdentifier() {
        assertNotNull(FeedbackId.newId().value());
    }

    @Test
    void rejectsNullValue() {
        assertThrows(NullPointerException.class, () -> new FeedbackId(null));
    }
}
