package view.view_manager;

import interface_adapter.PartialViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

public class PartialViewManager implements PropertyChangeListener {
    private final HashMap<String, JPanel> views;
    private final PartialViewModel partialViewModel;
    private JPanel contentPane;

    public PartialViewManager(HashMap<String, JPanel> views,
                              PartialViewModel partialViewModel) {
        this.views = views;
        this.partialViewModel = partialViewModel;

        partialViewModel.addPropertyChangeListener(this);
    }

    public void setContentPane(JPanel contentPane) {
        this.contentPane = contentPane;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String viewModelName = (String) evt.getNewValue();
            contentPane.removeAll();
            contentPane.add(views.get(viewModelName), BorderLayout.CENTER);
            contentPane.revalidate();
            contentPane.repaint();
        }
    }
}
