package view;

import interface_adapter.PartialViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.plan.show_plans.ShowPlansController;
import interface_adapter.plan.show_plans.ShowPlansState;
import interface_adapter.plan.show_plans.ShowPlansViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainPageView extends JPanel implements ActionListener, PropertyChangeListener {
    private static final String viewName = "main page";

    private static final int PLANS_PER_PAGE = 6;

    private final JButton calendarButton;
    private final JButton myPlansButton;
    private final JButton createPlanButton;
    private final JButton logoutButton;

    private ShowPlansController showPlansController;
    private LogoutController logoutController;

    private final JPanel contentPane;

    public MainPageView(ViewManagerModel viewManagerModel,
                        PartialViewModel partialViewModel,
                        ShowPlansViewModel showPlansViewModel,
                        PartialViewManager partialViewManager) {

        viewManagerModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());


        final JPanel topPanel = new JPanel(new BorderLayout());


        final JPanel navButtons = new JPanel();
        calendarButton = new JButton("Calendar");
        myPlansButton = new JButton("My Plans");
        createPlanButton = new JButton("Create Plan");

        calendarButton.addActionListener(e -> {
            partialViewModel.setState("CalendarView");
            partialViewModel.firePropertyChange();
        });

        myPlansButton.addActionListener(e -> {

            final ShowPlansState state = showPlansViewModel.getState();
            final String currentUsername = state.getUsername();
            if (currentUsername != null && !currentUsername.isEmpty() && showPlansController != null) {
                showPlansController.execute(currentUsername, 0, PLANS_PER_PAGE);
            }
            partialViewModel.setState("ShowPlansView");
            partialViewModel.firePropertyChange();
        });

        createPlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                partialViewModel.setState("generate plan");
                partialViewModel.firePropertyChange();
            }
        });

        navButtons.add(calendarButton);
        navButtons.add(myPlansButton);
        navButtons.add(createPlanButton);
        topPanel.add(navButtons, BorderLayout.WEST);


        final JPanel logoutPanel = new JPanel();
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.NORTH);

        this.contentPane = new JPanel();
        this.contentPane.setLayout(new BorderLayout());
        partialViewManager.setContentPane(this.contentPane);
        this.add(contentPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void setShowPlansController(ShowPlansController showPlansController) {
        this.showPlansController = showPlansController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public String getViewName() {
        return viewName;
    }

}
