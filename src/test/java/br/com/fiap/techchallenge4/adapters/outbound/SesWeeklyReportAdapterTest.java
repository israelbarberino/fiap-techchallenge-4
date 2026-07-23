package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;
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

class SesWeeklyReportAdapterTest {

    @Test
    void sendsWeeklyReportEmail() {
        SesClient sesClient = mock(SesClient.class);
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(mock(SendEmailResponse.class));
        SesWeeklyReportAdapter adapter = new SesWeeklyReportAdapter(sesClient, "from@example.com", "to@example.com");
        WeeklyReport report = new WeeklyReport(
                Instant.parse("2026-07-04T00:00:00Z"),
                Instant.parse("2026-07-11T00:00:00Z"),
                5,
                1,
                2,
                1,
                1
        );

        adapter.publish(report);

        var captor = org.mockito.ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(captor.capture());
        assertTrue(captor.getValue().message().subject().data().contains("Weekly feedback report"));
        assertTrue(captor.getValue().message().body().text().data().contains("Total: 5"));
    }
}
