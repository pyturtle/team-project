package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CalendarView extends JPanel implements ActionListener, PropertyChangeListener {
    int jan = 31;
    int feb1 = 28;
    int feb2 = 29;
    int mar = 31;
    int apr = 30;
    int may = 31;
    int jun = 30;
    int jul = 31;
    int aug = 31;
    int sep = 30;
    int oct = 31;
    int nov = 30;
    int dec = 31;

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
