package view;

import entity.Plan;
import interface_adapter.delete_plan.DeletePlanController;
import interface_adapter.logout.LogoutController;
import interface_adapter.show_plans.ShowPlansController;
import interface_adapter.show_plans.ShowPlansState;
import interface_adapter.show_plans.ShowPlansViewModel;
import interface_adapter.edit_plan.EditPlanController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for the Show Plans Use Case.
 */
public class ShowPlansView extends JPanel implements PropertyChangeListener {

    private static final int PLANS_PER_PAGE = 6;
    private static final int GRID_ROWS = 2;
    private static final int GRID_COLS = 3;
    private static final int PANEL_WIDTH = 180;
    private static final int PANEL_HEIGHT = 120;

    private final String viewName = "ShowPlansView";
    private final ShowPlansViewModel showPlansViewModel;
    private ShowPlansController showPlansController;
    private DeletePlanController deletePlanController;
    private LogoutController logoutController;
    private EditPlanController editPlanController;

    // View navigation buttons
    private JButton calendarButton;
    private JButton myPlansButton;
    private JButton createPlanButton;
    private JButton logoutButton;

    private final JPanel plansGridPanel;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JLabel pageLabel;

    // For switching views
    private interface_adapter.ViewManagerModel viewManagerModel;

    public ShowPlansView(ShowPlansViewModel showPlansViewModel, interface_adapter.ViewManagerModel viewManagerModel) {
        this.showPlansViewModel = showPlansViewModel;
        this.viewManagerModel = viewManagerModel;
        this.showPlansViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // View navigation buttons (Calendar, My Plans, Create Plan)
        final JPanel topPanel = new JPanel(new BorderLayout());

        // Navigation buttons on the left
        final JPanel navButtons = new JPanel();
        calendarButton = new JButton("Calendar");
        myPlansButton = new JButton("My Plans");
        createPlanButton = new JButton("Create Plan");

        calendarButton.addActionListener(e -> {
            viewManagerModel.setState("CalendarView");
            viewManagerModel.firePropertyChange();
        });

        myPlansButton.addActionListener(e -> {
            // Reload plans for current user before switching view
            final ShowPlansState state = showPlansViewModel.getState();
            final String currentUsername = state.getUsername();
            if (currentUsername != null && !currentUsername.isEmpty() && showPlansController != null) {
                showPlansController.execute(currentUsername, 0, PLANS_PER_PAGE);
            }
            viewManagerModel.setState("ShowPlansView");
            viewManagerModel.firePropertyChange();
        });

        // Create Plan button handler will be added later when Create Plan feature is implemented
        createPlanButton.setEnabled(false);

        navButtons.add(calendarButton);
        navButtons.add(myPlansButton);
        navButtons.add(createPlanButton);
        topPanel.add(navButtons, BorderLayout.WEST);

        // Logout button on the right
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

        // Plans grid panel
        plansGridPanel = new JPanel();
        plansGridPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, 15, 15));
        plansGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        final JScrollPane scrollPane = new JScrollPane(plansGridPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        // Pagination panel at bottom
        final JPanel paginationPanel = new JPanel();
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        previousButton = new JButton("←");
        nextButton = new JButton("→");
        pageLabel = new JLabel("Page 1");

        previousButton.addActionListener(e -> {
            final ShowPlansState state = showPlansViewModel.getState();
            if (state.hasPreviousPage()) {
                loadPlans(state.getUsername(), state.getCurrentPage() - 1);
            }
        });

        nextButton.addActionListener(e -> {
            final ShowPlansState state = showPlansViewModel.getState();
            if (state.hasNextPage()) {
                loadPlans(state.getUsername(), state.getCurrentPage() + 1);
            }
        });

        paginationPanel.add(previousButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        this.add(paginationPanel, BorderLayout.SOUTH);

        // Initialize with empty state
        updateView();
    }

    /**
     * Loads plans for the given username and page.
     * @param username the username
     * @param page the page number (0-indexed)
     */
    private void loadPlans(String username, int page) {
        if (showPlansController != null) {
            showPlansController.execute(username, page, PLANS_PER_PAGE);
        }
    }

    /**
     * Updates the view based on the current state.
     */
    private void updateView() {
        final ShowPlansState state = showPlansViewModel.getState();

        // Clear existing plans
        plansGridPanel.removeAll();

        // Display plans or empty message
        final List<Plan> plans = state.getPlans();
        if (plans.isEmpty()) {
            final JLabel emptyLabel = new JLabel("You haven't created any plans yet");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            plansGridPanel.setLayout(new BorderLayout());
            plansGridPanel.add(emptyLabel, BorderLayout.CENTER);
        }
        else {
            plansGridPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, 15, 15));
            for (Plan plan : plans) {
                plansGridPanel.add(createPlanPanel(plan));
            }
        }

        // Update pagination controls
        pageLabel.setText("Page " + (state.getCurrentPage() + 1) + " / " +
                         Math.max(1, state.getTotalPages()));
        previousButton.setEnabled(state.hasPreviousPage());
        nextButton.setEnabled(state.hasNextPage());

        plansGridPanel.revalidate();
        plansGridPanel.repaint();
    }

    /**
     * Creates a panel for a single plan.
     * @param plan the plan to display
     * @return the plan panel
     */
    private JPanel createPlanPanel(Plan plan) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        // Plan title
        final JLabel titleLabel = new JLabel("<html><b># " + plan.getName() + "</b></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));

        final JButton subgoalsButton = new JButton(ShowPlansViewModel.SUBGOALS_BUTTON_LABEL);
        final JButton deleteButton = new JButton(ShowPlansViewModel.DELETE_BUTTON_LABEL);
        final JButton editButton = new JButton("Edit");

        // Subgoals button not implemented yet
        subgoalsButton.setEnabled(false);

        editButton.addActionListener(e -> {
            if (editPlanController == null) return;

            JTextField nameField = new JTextField(plan.getName(), 20);
            JTextField descField = new JTextField(plan.getDescription(), 20);
            JTextField colourField = new JTextField(plan.getColour(), 10);

            JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
            form.add(new JLabel("Title:"));
            form.add(nameField);
            form.add(new JLabel("Description:"));
            form.add(descField);
            form.add(new JLabel("Colour:"));
            form.add(colourField);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Edit Plan",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                String newDesc = descField.getText().trim();
                String newColour = colourField.getText().trim();

                if (!newName.isEmpty() && !newDesc.isEmpty() && !newColour.isEmpty()) {
                    editPlanController.execute(plan.getPlanId(), newName, newDesc, newColour);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "All fields must be non-empty.",
                            "Edit Plan Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });


        // Enable delete button and add action listener
        deleteButton.addActionListener(e -> {
            final int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete plan: " + plan.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION && deletePlanController != null) {
                final ShowPlansState state = showPlansViewModel.getState();
                final String username = state.getUsername();
                final int currentPage = state.getCurrentPage();

                // Delete the plan
                deletePlanController.execute(plan.getPlanId(), username);

                // Small delay to ensure deletion is processed
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                // Reload - go to page 0 to avoid issues with empty pages
                loadPlans(username, 0);
            }
        });

        buttonsPanel.add(subgoalsButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);


        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateView();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setShowPlansController(ShowPlansController controller) {
        this.showPlansController = controller;
    }

    public void setEditPlanController(EditPlanController controller) {
        this.editPlanController = controller;
    }

    public void setDeletePlanController(DeletePlanController controller) {
        this.deletePlanController = controller;
    }

    public void setLogoutController(LogoutController controller) {
        this.logoutController = controller;
    }
}
