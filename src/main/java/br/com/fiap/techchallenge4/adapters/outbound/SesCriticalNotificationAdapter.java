package br.com.fiap.techchallenge4.adapters.outbound;

import br.com.fiap.techchallenge4.application.port.out.CriticalNotificationPort;
import br.com.fiap.techchallenge4.domain.model.Feedback;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

public class SesCriticalNotificationAdapter implements CriticalNotificationPort {

    private final SesClient sesClient;
    private final String sourceEmail;
    private final String targetEmail;

    public SesCriticalNotificationAdapter() {
        this(
            SesClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build(),
                System.getenv().getOrDefault("SES_FROM", "no-reply@example.com"),
                System.getenv().getOrDefault("SES_TO", "alerts@example.com")
        );
    }

    public SesCriticalNotificationAdapter(SesClient sesClient, String sourceEmail, String targetEmail) {
        this.sesClient = sesClient;
        this.sourceEmail = sourceEmail;
        this.targetEmail = targetEmail;
    }

    @Override
    public void notify(Feedback feedback) {
        String subject = "Critical feedback received";
        String body = "Feedback ID: " + feedback.id().value() + "\n"
                + "Urgency: " + feedback.urgency().name() + "\n"
                + "Created At: " + feedback.createdAt() + "\n\n"
                + feedback.content().value();

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