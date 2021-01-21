package at.jit.incidentlistener.demo;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

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
        runtimeService.startProcessInstanceByKey("TestOptimizeProcess");
    }
}
