package view;

import interface_adapter.DialogManagerModel;
import interface_adapter.ViewManagerModel;

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

    private void createDialog(JPanel view)
    {
        JDialog dialog = new JDialog();
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);   // center on screen
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setVisible(true);
    }
}
