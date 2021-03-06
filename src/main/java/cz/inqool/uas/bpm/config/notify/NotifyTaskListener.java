package cz.inqool.uas.bpm.config.notify;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Creation task event listener for directly assigned tasks.
 */
@ConditionalOnProperty(prefix = "bpm", name = "enabled", havingValue = "true")
@Component
public class NotifyTaskListener implements TaskListener {
    private NotificationCenter center;

    @Inject
    public NotifyTaskListener(NotificationCenter center) {
        this.center = center;
    }

    /**
     * Delegates the notification to {@link NotificationCenter}
     * @param task task to notify about
     */
    @Override
    public void notify(DelegateTask task) {
        center.notifyAssign(task);
    }

}
