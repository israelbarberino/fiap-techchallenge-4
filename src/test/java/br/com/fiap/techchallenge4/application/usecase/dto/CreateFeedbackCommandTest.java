package br.com.fiap.techchallenge4.application.usecase.dto;

import br.com.fiap.techchallenge4.domain.model.Urgency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateFeedbackCommandTest {

    @Test
    void storesContentAndUrgency() {
        CreateFeedbackCommand command = new CreateFeedbackCommand("Atendimento", Urgency.LOW);

        assertEquals("Atendimento", command.content());
        assertEquals(Urgency.LOW, command.urgency());
    }

    @Test
    void rejectsNullContent() {
        assertThrows(NullPointerException.class, () -> new CreateFeedbackCommand(null, Urgency.LOW));
    }
}
