package br.com.fiap.techchallenge4.config;

import br.com.fiap.techchallenge4.application.port.in.CreateFeedbackInputPort;
import br.com.fiap.techchallenge4.application.port.in.GenerateWeeklyReportInputPort;
import br.com.fiap.techchallenge4.application.port.in.NotifyCriticalFeedbackInputPort;
import br.com.fiap.techchallenge4.application.port.out.CriticalNotificationPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackCreatedEventPort;
import br.com.fiap.techchallenge4.application.port.out.FeedbackRepositoryPort;
import br.com.fiap.techchallenge4.application.port.out.WeeklyReportPort;
import br.com.fiap.techchallenge4.application.usecase.CreateFeedbackUseCase;
import br.com.fiap.techchallenge4.application.usecase.GenerateWeeklyReportUseCase;
import br.com.fiap.techchallenge4.application.usecase.NotifyCriticalFeedbackUseCase;
import br.com.fiap.techchallenge4.adapters.outbound.DynamoDbFeedbackRepositoryAdapter;
import br.com.fiap.techchallenge4.adapters.outbound.EventBridgeFeedbackCreatedEventAdapter;
import br.com.fiap.techchallenge4.adapters.outbound.SesCriticalNotificationAdapter;
import br.com.fiap.techchallenge4.adapters.outbound.SesWeeklyReportAdapter;

public final class ApplicationModule {

    private static final FeedbackRepositoryPort FEEDBACK_REPOSITORY = new DynamoDbFeedbackRepositoryAdapter();
    private static final FeedbackCreatedEventPort FEEDBACK_CREATED_EVENT = new EventBridgeFeedbackCreatedEventAdapter();
    private static final CriticalNotificationPort CRITICAL_NOTIFICATION = new SesCriticalNotificationAdapter();
    private static final WeeklyReportPort WEEKLY_REPORT = new SesWeeklyReportAdapter();

    private static final CreateFeedbackInputPort CREATE_FEEDBACK_USE_CASE =
            new CreateFeedbackUseCase(FEEDBACK_REPOSITORY, FEEDBACK_CREATED_EVENT);

    private static final NotifyCriticalFeedbackInputPort NOTIFY_CRITICAL_FEEDBACK_USE_CASE =
            new NotifyCriticalFeedbackUseCase(CRITICAL_NOTIFICATION);

    private static final GenerateWeeklyReportInputPort GENERATE_WEEKLY_REPORT_USE_CASE =
            new GenerateWeeklyReportUseCase(FEEDBACK_REPOSITORY, WEEKLY_REPORT);

    private ApplicationModule() {
    }

    public static CreateFeedbackInputPort createFeedbackUseCase() {
        return CREATE_FEEDBACK_USE_CASE;
    }

    public static NotifyCriticalFeedbackInputPort notifyCriticalFeedbackUseCase() {
        return NOTIFY_CRITICAL_FEEDBACK_USE_CASE;
    }

    public static GenerateWeeklyReportInputPort generateWeeklyReportUseCase() {
        return GENERATE_WEEKLY_REPORT_USE_CASE;
    }
}