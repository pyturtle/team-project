package view.plan;

import interface_adapter.plan.save_plan.SavePlanState;
import interface_adapter.plan.save_plan.SavePlanViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * SavePlanView is a view that displays the result message
 * of the save plan use case by observing the SavePlanViewModel state.
 */
public class SavePlanView extends JPanel implements PropertyChangeListener {
    private final String viewName = "save plan";
    private final JPanel messagePanel = new JPanel();
    private final JLabel messageLabel = new JLabel();

    /**
     * Creates a new SavePlanView and registers it as a listener
     * to the provided SavePlanViewModel.
     * @param savePlanViewModel the view model whose state changes this view observes
     */
    public SavePlanView(SavePlanViewModel savePlanViewModel) {
        savePlanViewModel.addPropertyChangeListener(this);

        messagePanel.setLayout(new BoxLayout(this.messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(messageLabel);
        add(messagePanel);
    }

    /**
     * Handles property change events from the SavePlanViewModel by updating
     * the displayed message label with the latest save result message.
     * @param evt the property change event containing the new SavePlanState
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SavePlanState savePlanState = (SavePlanState) evt.getNewValue();
        messageLabel.setText(savePlanState.getMessage());
        messagePanel.revalidate();
        messagePanel.repaint();
    }

    /**
     * Returns the logical name of this view used by the dialog manager.
     * @return the view name identifier
     */
    public String getViewName() {
        return viewName;
    }
}
