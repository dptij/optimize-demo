package at.jit.incidentlistener.demo;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ProcessApplication
public class DemoApplication extends SpringBootProcessApplication {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public static void main(String... args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @EventListener
    private void processPostDeploy(final PostDeployEvent evt) {
        System.out.println("X");

        for (int i=0; i < 20; i++) {
            startProcess("1", "1.1", String.format("Path 1.1, instance %d", i+1));
        }
        for (int i=0; i < 30; i++) {
            startProcess("1", "1.2", String.format("Path 1.2, instance %d", i + 1));
        }
        for (int i=0; i < 150; i++) {
            startProcess("2", null, String.format("Path 2, instance %d", i + 1));
        }
    }

    private void startProcess(final String path, final String subPath,
                              final String businessKey) {
        final Map<String, Object> vars = new HashMap<>();
        vars.put("path", path);
        vars.put("subPath", subPath);
        runtimeService.startProcessInstanceByKey("TestOptimizeProcess", businessKey, vars);
    }
}
