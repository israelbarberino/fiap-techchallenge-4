package br.com.fiap.techchallenge4.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationModuleTest {

    private static final String DEFAULT_AWS_REGION = "us-east-1";
    private static String previousAwsRegion;

    @BeforeAll
    static void setUpAwsRegion() {
        previousAwsRegion = System.getProperty("aws.region");
        System.setProperty("aws.region", System.getenv().getOrDefault("AWS_REGION", DEFAULT_AWS_REGION));
    }

    @AfterAll
    static void restoreAwsRegion() {
        if (previousAwsRegion == null) {
            System.clearProperty("aws.region");
            return;
        }
        System.setProperty("aws.region", previousAwsRegion);
    }

    @Test
    void exposesAllUseCases() {
        assertNotNull(ApplicationModule.createFeedbackUseCase());
        assertNotNull(ApplicationModule.notifyCriticalFeedbackUseCase());
        assertNotNull(ApplicationModule.generateWeeklyReportUseCase());
    }
}
