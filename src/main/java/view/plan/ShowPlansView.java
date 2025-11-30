package view.plan;

import entity.plan.Plan;
import entity.subgoal.Subgoal;
import interface_adapter.plan.delete_plan.DeletePlanController;
import interface_adapter.plan.save_plan.SavePlanState;
import interface_adapter.plan.save_plan.SavePlanViewModel;
import interface_adapter.plan.show_plans.ShowPlansController;
import interface_adapter.plan.show_plans.ShowPlansState;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import interface_adapter.show_subgoal.ShowSubgoalController;
import interface_adapter.show_subgoal.ShowSubgoalViewModel;
import view.ShowSubgoalView;

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
    private final SavePlanViewModel savePlanViewModel;
    private final ShowSubgoalViewModel showSubgoalViewModel = new ShowSubgoalViewModel();

    private ShowPlansController showPlansController;
    private DeletePlanController deletePlanController;

    private ShowSubgoalView showSubgoalView;

    private final JPanel plansGridPanel;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JLabel pageLabel;


    public ShowPlansView(ShowPlansViewModel showPlansViewModel,
                         SavePlanViewModel savePlanViewModel) {
        this.showPlansViewModel = showPlansViewModel;
        this.savePlanViewModel = savePlanViewModel;

        this.showPlansViewModel.addPropertyChangeListener(this);
        this.savePlanViewModel.addPropertyChangeListener(this);
        showSubgoalView = new ShowSubgoalView(showSubgoalViewModel);

        this.setLayout(new BorderLayout());

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

    public void setShowSubgoalView(ShowSubgoalView view) {
        this.showSubgoalView = view;
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
        subgoalsButton.setActionCommand(plan.getId());
        final JButton deleteButton = new JButton(ShowPlansViewModel.DELETE_BUTTON_LABEL);

        // ENABLING THE BUTTON!!!!
        subgoalsButton.setEnabled(true);

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
                deletePlanController.execute(plan.getId(), username);

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
        // subgoals BUTTON actionLISTENR!
        subgoalsButton.addActionListener(e -> {
            if (showSubgoalController != null) {
                String planId = e.getActionCommand();
                String username = plan.getUsername();
                List<Subgoal> subgoals = showSubgoalController.getSubgoalsForPlan(planId, username);

                JDialog subgoalDialog = new JDialog((Frame) null, "Subgoals for " + plan.getName(), true);
                subgoalDialog.setLayout(new BorderLayout());
                subgoalDialog.setSize(400, 400);
                subgoalDialog.setLocationRelativeTo(null);

                JLabel planTitle = new JLabel(plan.getName(), SwingConstants.CENTER);
                planTitle.setFont(new Font("Arial", Font.BOLD, 18));
                planTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                subgoalDialog.add(planTitle, BorderLayout.NORTH);

                JPanel subgoalsPanel = new JPanel();
                subgoalsPanel.setLayout(new BoxLayout(subgoalsPanel, BoxLayout.Y_AXIS));
                JScrollPane scrollPane = new JScrollPane(subgoalsPanel);
                subgoalDialog.add(scrollPane, BorderLayout.CENTER);

                subgoals.sort(java.util.Comparator.comparing(Subgoal::getDeadline));
                for (Subgoal s : subgoals) {
                    JPanel box = new JPanel();
                    box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
                    box.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.GRAY, 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                    JLabel nameLabel = new JLabel(s.getName());
                    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    box.add(nameLabel);

                    JLabel descLabel = new JLabel("<html><i>" + s.getDescription() + "</i></html>");
                    box.add(descLabel);

                    JLabel dueLabel = new JLabel("Due: " + s.getDeadline());
                    box.add(dueLabel);

                    // ithink this lets me click it now.
                    box.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            if (showSubgoalController != null && showSubgoalView != null) {
                                showSubgoalController.execute(s.getId());  //this uses the same viewon calendarvew
                            }

                        }
                    });




                    subgoalsPanel.add(Box.createVerticalStrut(5));
                    subgoalsPanel.add(box);
                }



                subgoalDialog.setVisible(true);
            }
        });




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

        if (evt.getNewValue() instanceof SavePlanState) {
            final ShowPlansState state = showPlansViewModel.getState();
            loadPlans(state.getUsername(), state.getCurrentPage());
            updateView();
        }
    }

    public String getViewName() {
        return viewName;
    }

    private ShowSubgoalController showSubgoalController;

    public void setShowSubgoalController(ShowSubgoalController controller) {
        this.showSubgoalController = controller;
    }


    public void setShowPlansController(ShowPlansController controller) {
        this.showPlansController = controller;
    }

    public void setDeletePlanController(DeletePlanController controller) {
        this.deletePlanController = controller;
    }

    public JButton getSubgoalsButton(JPanel planPanel) {
        for (Component comp : planPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel buttonsPanel = (JPanel) comp;
                for (Component btn : buttonsPanel.getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        if (ShowPlansViewModel.SUBGOALS_BUTTON_LABEL.equals(button.getText())) {
                            return button;
                        }
                    }
                }
            }
        }
        return null;
    }


}
