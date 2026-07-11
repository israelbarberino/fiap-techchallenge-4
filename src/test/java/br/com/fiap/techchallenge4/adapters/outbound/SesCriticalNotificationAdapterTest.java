package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.domain.model.Feedback;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackContent;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SesCriticalNotificationAdapterTest {

    @Test
    void sendsCriticalNotificationEmail() {
        SesClient sesClient = mock(SesClient.class);
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(mock(SendEmailResponse.class));
        SesCriticalNotificationAdapter adapter = new SesCriticalNotificationAdapter(sesClient, "from@example.com", "to@example.com");
        Feedback feedback = Feedback.restore(
                new FeedbackId(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new FeedbackContent("Falha crítica"),
                Urgency.CRITICAL,
                Instant.parse("2026-07-11T10:00:00Z")
        );

        adapter.notify(feedback);

        var captor = org.mockito.ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(captor.capture());
        assertTrue(captor.getValue().message().subject().data().contains("Critical feedback received"));
        assertTrue(captor.getValue().message().body().text().data().contains("Falha crítica"));
    }
}
