package br.com.fiap.techchallenge4.adapters.inbound;

import br.com.fiap.techchallenge4.application.port.in.CreateFeedbackInputPort;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackCommand;
import br.com.fiap.techchallenge4.application.usecase.dto.CreateFeedbackResult;
import br.com.fiap.techchallenge4.config.ApplicationModule;
import br.com.fiap.techchallenge4.domain.model.Urgency;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;
import java.util.Map;

public class CreateFeedbackHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final CreateFeedbackInputPort useCase = ApplicationModule.createFeedbackUseCase();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        try {
            CreateFeedbackRequest request = OBJECT_MAPPER.readValue(event.getBody(), CreateFeedbackRequest.class);
            CreateFeedbackCommand command = new CreateFeedbackCommand(
                    request.content,
                    Urgency.valueOf(request.urgency.toUpperCase(Locale.ROOT))
            );

            CreateFeedbackResult result = useCase.execute(command);
            return response(201, toJson(Map.of("feedbackId", result.feedbackId().value().toString())));
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return response(400, toJson(Map.of("message", "invalid request")));
        } catch (Exception e) {
            return response(500, toJson(Map.of("message", "internal error")));
        }
    }

    private APIGatewayV2HTTPResponse response(int statusCode, String body) {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(body)
                .build();
    }

    private String toJson(Object payload) {
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("cannot serialize response");
        }
    }

    private static final class CreateFeedbackRequest {
        public String content;
        public String urgency;
    }
}