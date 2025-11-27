package view;

import interface_adapter.DialogManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

public class DialogManager implements PropertyChangeListener {
    private final HashMap<String, JPanel> dialogViews;
    private final DialogManagerModel dialogManagerModel;

    public DialogManager(HashMap<String, JPanel> dialogViews,
                         DialogManagerModel dialogManagerModel) {
        this.dialogViews = dialogViews;
        this.dialogManagerModel = dialogManagerModel;
        this.dialogManagerModel.addPropertyChangeListener(this);


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String dialogViewModelName = (String) evt.getNewValue();
            createDialog(dialogViews.get(dialogViewModelName));
        }
    }

    private void createDialog(JPanel view) {
        Window parentWindow = SwingUtilities.getWindowAncestor(view);
        if (parentWindow == null) {
            parentWindow = new JFrame();
        }

        JDialog dialog = new JDialog(parentWindow, "Subgoal Details", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }


}
