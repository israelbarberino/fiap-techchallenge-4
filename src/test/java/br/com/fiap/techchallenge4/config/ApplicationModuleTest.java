package br.com.fiap.techchallenge4.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationModuleTest {

    @Test
    void exposesAllUseCases() {
        assertNotNull(ApplicationModule.createFeedbackUseCase());
        assertNotNull(ApplicationModule.notifyCriticalFeedbackUseCase());
        assertNotNull(ApplicationModule.generateWeeklyReportUseCase());
    }
}
