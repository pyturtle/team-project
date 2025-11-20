package view.plan;

import interface_adapter.plan.save_plan.SavePlanState;
import interface_adapter.plan.save_plan.SavePlanViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SavePlanView extends JPanel implements PropertyChangeListener {
    private final String viewName = "save plan";

    private final JPanel messagePanel = new JPanel();
    private final JLabel messageLabel = new JLabel();

    public SavePlanView(SavePlanViewModel savePlanViewModel) {
        savePlanViewModel.addPropertyChangeListener(this);

        messagePanel.setLayout(new BoxLayout(this.messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(messageLabel);
        add(messagePanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SavePlanState savePlanState = (SavePlanState) evt.getNewValue();
        messageLabel.setText(savePlanState.getMessage());
        messagePanel.revalidate();
        messagePanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
