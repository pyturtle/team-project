package view;

import interface_adapter.DialogManagerModel;

import javax.swing.*;
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
        JFrame dialog = new JFrame();
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);   // center on screen
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setVisible(true);
    }
}
