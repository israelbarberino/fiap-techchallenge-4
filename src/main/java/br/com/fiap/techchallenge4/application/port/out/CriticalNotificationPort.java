package br.com.fiap.techchallenge4.application.port.out;

import br.com.fiap.techchallenge4.domain.model.Feedback;

public interface CriticalNotificationPort {
    void notify(Feedback feedback);
}