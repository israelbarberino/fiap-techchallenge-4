package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.CreateFeedbackInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackResult;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import br.com.fiap.techchallenge4.domain.valueobject.FeedbackId;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext.Http;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CreateFeedbackHandlerTest {

    @Test
    void returnsHealthcheckResponseForGetHealth() {
        CapturingCreateFeedbackInputPort useCase = new CapturingCreateFeedbackInputPort();
        CreateFeedbackHandler handler = new CreateFeedbackHandler(useCase);
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        event.setRawPath("/health");
        event.setRequestContext(requestContext("GET", "/health"));

        APIGatewayV2HTTPResponse response = handler.handleRequest(event, mock(Context.class));

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
        assertFalse(useCase.invoked);
    }

    @Test
    void returnsBadRequestForInvalidPayload() {
        CreateFeedbackHandler handler = new CreateFeedbackHandler(new CapturingCreateFeedbackInputPort());
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        event.setRawPath("/feedback");
        event.setRequestContext(requestContext("POST", "/feedback"));
        event.setBody("{");

        APIGatewayV2HTTPResponse response = handler.handleRequest(event, mock(Context.class));

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("invalid request"));
    }

    @Test
    void createsFeedbackForValidPayload() {
        CapturingCreateFeedbackInputPort useCase = new CapturingCreateFeedbackInputPort();
        CreateFeedbackHandler handler = new CreateFeedbackHandler(useCase);
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        event.setRawPath("/feedback");
        event.setRequestContext(requestContext("POST", "/feedback"));
        event.setBody("{\"content\":\"Excelente atendimento\",\"urgency\":\"medium\"}");

        APIGatewayV2HTTPResponse response = handler.handleRequest(event, mock(Context.class));

        assertEquals(201, response.getStatusCode());
        assertTrue(response.getBody().contains(useCase.feedbackId.value().toString()));
        assertEquals("Excelente atendimento", useCase.command.content());
        assertEquals(Urgency.MEDIUM, useCase.command.urgency());
    }

    private static RequestContext requestContext(String method, String path) {
        RequestContext requestContext = new RequestContext();
        Http http = new Http();
        http.setMethod(method);
        http.setPath(path);
        requestContext.setHttp(http);
        return requestContext;
    }

    private static final class CapturingCreateFeedbackInputPort implements CreateFeedbackInputPort {
        private final FeedbackId feedbackId = new FeedbackId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        private boolean invoked;
        private CreateFeedbackCommand command;

        @Override
        public CreateFeedbackResult execute(CreateFeedbackCommand command) {
            invoked = true;
            this.command = command;
            return new CreateFeedbackResult(feedbackId);
        }
    }
}
