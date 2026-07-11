package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.application.port.out.WeeklyReportPort;
import br.com.fiap.techchallenge4.application.usecase.dto.WeeklyReport;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

public class SesWeeklyReportAdapter implements WeeklyReportPort {

    private final SesClient sesClient;
    private final String sourceEmail;
    private final String targetEmail;

    public SesWeeklyReportAdapter() {
        this(
                SesClient.builder().build(),
                System.getenv().getOrDefault("SES_FROM", "no-reply@example.com"),
                System.getenv().getOrDefault("SES_TO", "reports@example.com")
        );
    }

    public SesWeeklyReportAdapter(SesClient sesClient, String sourceEmail, String targetEmail) {
        this.sesClient = sesClient;
        this.sourceEmail = sourceEmail;
        this.targetEmail = targetEmail;
    }

    @Override
    public void publish(WeeklyReport report) {
        String subject = "Weekly feedback report";
        String body = "Period: " + report.periodStartInclusive() + " to " + report.periodEndExclusive() + "\n"
                + "Total: " + report.total() + "\n"
                + "LOW: " + report.low() + "\n"
                + "MEDIUM: " + report.medium() + "\n"
                + "HIGH: " + report.high() + "\n"
                + "CRITICAL: " + report.critical();

        sendEmail(subject, body);
    }

    private void sendEmail(String subject, String bodyText) {
        SendEmailRequest request = SendEmailRequest.builder()
                .source(sourceEmail)
                .destination(Destination.builder().toAddresses(targetEmail).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder().data(bodyText).build()).build())
                        .build())
                .build();

        sesClient.sendEmail(request);
    }
}