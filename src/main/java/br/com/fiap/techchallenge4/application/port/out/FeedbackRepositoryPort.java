package br.com.fiap.techchallenge4.application.port.out;

import br.com.fiap.techchallenge4.domain.model.Feedback;

import java.time.Instant;
import java.util.List;

public interface FeedbackRepositoryPort {
    void save(Feedback feedback);

    List<Feedback> findBetween(Instant startInclusive, Instant endExclusive);
}