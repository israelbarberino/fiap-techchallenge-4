package br.com.fiap.techchallenge4.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationModuleTest {

    @BeforeAll
    static void setUpAwsRegion() {
        System.setProperty("aws.region", "us-east-1");
    }

    @Test
    void exposesAllUseCases() {
        assertNotNull(ApplicationModule.createFeedbackUseCase());
        assertNotNull(ApplicationModule.notifyCriticalFeedbackUseCase());
        assertNotNull(ApplicationModule.generateWeeklyReportUseCase());
    }
}
