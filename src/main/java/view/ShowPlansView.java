package view;

import entity.Plan;
import interface_adapter.show_plans.ShowPlansController;
import interface_adapter.show_plans.ShowPlansState;
import interface_adapter.show_plans.ShowPlansViewModel;

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

    private final String viewName = "show plans";
    private final ShowPlansViewModel showPlansViewModel;
    private ShowPlansController showPlansController;

    private final JPanel plansGridPanel;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JLabel pageLabel;

    public ShowPlansView(ShowPlansViewModel showPlansViewModel) {
        this.showPlansViewModel = showPlansViewModel;
        this.showPlansViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // Title
        final JLabel title = new JLabel(ShowPlansViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);

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

        // Buttons don't need to work yet - they're just for display
        subgoalsButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonsPanel.add(subgoalsButton);
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

    /**
     * Initializes the view with a username.
     * @param username the username whose plans to show
     */
    public void initializeWithUsername(String username) {
        final ShowPlansState state = showPlansViewModel.getState();
        state.setUsername(username);
        showPlansViewModel.setState(state);
        loadPlans(username, 0);
    }
}

