package at.codecrafters.zeebe.bpmnWorker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.JobContext;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@OutboundConnector(
        name = "print-message",
        type = "io.camunda:print-message",
        inputVariables = {"message"}
)
public class PrintMessageWorker  implements OutboundConnectorFunction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintMessageWorker.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Object execute(OutboundConnectorContext outboundConnectorContext) throws Exception {
        JobContext jobContext = outboundConnectorContext.getJobContext();
        if (jobContext == null) {
            LOGGER.warn("JobContext is null.");
            return Map.of("status", "No JobContext available");
        }

        String variablesJson = jobContext.getVariables();
        if (variablesJson == null || variablesJson.isEmpty()) {
            LOGGER.warn("Variables are empty.");
            return Map.of("status", "No variables provided");
        }

        Map<String, Object> context = OBJECT_MAPPER.readValue(variablesJson, Map.class);
        String message = (String) context.get("message");

        if (message != null && !message.isEmpty()) {
            LOGGER.info("Received message: {}", message);
        } else {
            LOGGER.warn("No message provided.");
        }

        return Map.of("status", "Message processed successfully");
    }
}

