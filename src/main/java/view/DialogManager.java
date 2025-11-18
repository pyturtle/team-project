package view;

import interface_adapter.DialogManagerModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DialogManager implements PropertyChangeListener {
    private final JDialog dialog;
    private final CardLayout cardLayout;
    private final JPanel views;
    private final DialogManagerModel dialogManagerModel;

    public DialogManager(JPanel views, CardLayout cardLayout, DialogManagerModel dialogManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.dialogManagerModel = dialogManagerModel;
        this.dialogManagerModel.addPropertyChangeListener(this);

        dialog = new JDialog();
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);   // center on screen
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(cardLayout);
        dialog.setContentPane(views);
        dialog.setVisible(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String dialogViewModelName = (String) evt.getNewValue();
            cardLayout.show(views, dialogViewModelName);
            dialog.pack();
            dialog.setVisible(true);
        }
    }
}
