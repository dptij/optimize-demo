package at.jit.incidentlistener.demo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class VaryingDurationDelegate implements JavaDelegate {
    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        if (delEx.getVariable("slowDown") == Boolean.TRUE) {
            Thread.sleep(100000);
        }
    }
}
