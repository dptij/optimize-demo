package at.jit.incidentlistener.demo;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
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
    public static final int PATH_1_1_INSTANCE_COUNT = 20;
    public static final int PATH_1_2_INSTANCE_COUNT = 30;
    public static final int PATH_2_INSTANCE_COUNT = 150;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public static void main(String... args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @EventListener
    private void processPostDeploy(final PostDeployEvent evt) {
        setupOptimizeAuthorization(evt);
        createTestData();
    }

    private void setupOptimizeAuthorization(PostDeployEvent evt) {
        final AuthorizationService authService = evt.getProcessEngine().getAuthorizationService();
        final Authorization auth = authService.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
        auth.setUserId("*");
        auth.setResource(Resources.APPLICATION);
        auth.setResourceId("optimize");
        auth.addPermission(Permissions.ALL);
        authService.saveAuthorization(auth);
    }

    private void createTestData() {
        for (int i = 0; i < PATH_1_1_INSTANCE_COUNT; i++) {
            startProcess("1", "1.1",
                    String.format("Path 1.1, instance %d/%d", i+1, PATH_1_1_INSTANCE_COUNT));
        }
        for (int i = 0; i < PATH_1_2_INSTANCE_COUNT; i++) {
            startProcess("1", "1.2",
                    String.format("Path 1.2, instance %d/%d", i + 1, PATH_1_2_INSTANCE_COUNT));
        }
        for (int i = 0; i < PATH_2_INSTANCE_COUNT; i++) {
            startProcess("2", null,
                    String.format("Path 2, instance %d/%d", i + 1, PATH_2_INSTANCE_COUNT));
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
