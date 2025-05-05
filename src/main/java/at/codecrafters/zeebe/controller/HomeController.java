package at.codecrafters.zeebe.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value="/execute")
    public String execute(){
        try (ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("localhost:26500")
                .usePlaintext()
                .build()) {

//            Map<String, Object> variables = new HashMap<>();
//            variables.put("message", "Hello Zeebe");
//            variables.put("inputName", "set Server Side before send");

            ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("first_bpmn_execute")
                    .latestVersion()
                    //.variables(variables)
                    //kürzer mit
                    .variables("{\"message\": \"Hello Zeebe\", \"inputName\": \"set Server Side before send\"}")
                    .send()
                    .join();

            System.out.println("Started process with key: " + event.getProcessInstanceKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "BPMN x has executed";
    }
}
