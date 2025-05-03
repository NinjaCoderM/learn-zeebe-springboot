package at.codecrafters.zeebe.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value="/execute")
    public String execute(){
        try (ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("localhost:26500")
                .usePlaintext()
                .build()) {

            ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("first_bpmn_execute")
                    .latestVersion()
                    .send()
                    .join();

            System.out.println("Started process with key: " + event.getProcessInstanceKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "BPMN x has executed";
    }
}
