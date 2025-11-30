package view.plan;

import interface_adapter.plan.save_plan.SavePlanController;
import interface_adapter.plan.show_plan.ShowPlanState;
import interface_adapter.plan.show_plan.ShowPlanViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ShowPlanView is a view that displays a generated plan,
 * including its name, description, and list of subgoals, and allows
 * the user to save the plan if it does not already exist.
 */
public class ShowPlanView extends JPanel implements PropertyChangeListener {

    private final String viewName = "show plan";
    private final JScrollPane subgoalsContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JLabel planNameLabel = new JLabel();
    private final JLabel planDescriptionLabel = new JLabel();
    private final JPanel subgoalsPanel = new JPanel();
    private final JButton createPlanButton = new JButton(ShowPlanViewModel.CREATE_BUTTON_LABEL);
    private SavePlanController savePlanController;

    /**
     * Creates a new ShowPlanView and registers it as a listener
     * to the provided ShowPlanViewModel.
     * The view displays plan information and subgoals and provides
     * a button to create the plan in persistent storage.
     * @param showPlanViewModel the view model whose state changes this view observes
     */
    public ShowPlanView(ShowPlanViewModel showPlanViewModel) {
        showPlanViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(ShowPlanViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(15));

        JPanel planInfoPanel = new JPanel();
        planInfoPanel.setLayout(new BoxLayout(planInfoPanel, BoxLayout.Y_AXIS));
        planInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        planInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        planNameLabel.setFont(planNameLabel.getFont().deriveFont(Font.BOLD, 16f));
        planNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        planDescriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        planInfoPanel.add(planNameLabel);
        planInfoPanel.add(Box.createVerticalStrut(5));
        planInfoPanel.add(planDescriptionLabel);

        add(planInfoPanel);

        add(createDivider());
        add(Box.createVerticalStrut(10));

        subgoalsPanel.setLayout(new BoxLayout(subgoalsPanel, BoxLayout.Y_AXIS));
        subgoalsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subgoalsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        subgoalsContainer.setViewportView(subgoalsPanel);
        subgoalsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        subgoalsContainer.setBorder(null);

        add(subgoalsContainer);

        createPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ShowPlanState currentState = showPlanViewModel.getState();
                savePlanController.execute(currentState.getPlanName(),
                        currentState.getPlanDescription(),
                        currentState.getUsername(),
                        currentState.getSubgoalList());
                createPlanButton.setEnabled(false);
            }
        });
        add(createPlanButton);
    }

    /**
     * Handles property change events from the ShowPlanViewModel.
     * Updates the labels, subgoals list, and create button enabled state
     * based on the new ShowPlanState.
     * @param evt the property change event containing the new state
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ShowPlanState newState = (ShowPlanState) evt.getNewValue();
        createPlanButton.setEnabled(!newState.isPlanExists());
        planNameLabel.setText(newState.getPlanName());
        planDescriptionLabel.setText("<html>" + newState.getPlanDescription() + "</html>");

        subgoalsPanel.removeAll();

        ArrayList<HashMap<String, String>> subgoals = newState.getSubgoalList();
        for (int i = 0; i < subgoals.size(); i++) {
            HashMap<String, String> subgoal = subgoals.get(i);
            JPanel subgoalPanel = createSubgoalPanel(
                    subgoal.get("name"),
                    subgoal.get("description"),
                    LocalDate.parse(subgoal.get("deadline"))
            );

            subgoalsPanel.add(subgoalPanel);

            if (i < subgoals.size() - 1) {
                subgoalsPanel.add(createDivider());
                subgoalsPanel.add(Box.createVerticalStrut(10));
            }
        }

        subgoalsPanel.revalidate();
        subgoalsPanel.repaint();
    }

    /**
     * Creates a panel showing a single subgoal with its name,
     * description, and deadline.
     * @param name the subgoal name
     * @param description the subgoal description
     * @param deadline the subgoal deadline date
     * @return a panel displaying the subgoal information
     */
    private JPanel createSubgoalPanel(String name, String description, LocalDate deadline) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(descLabel.getFont().deriveFont(13f));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JLabel deadlineLabel = new JLabel("Due: " + deadline);
        deadlineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(deadlineLabel);

        return panel;
    }

    /**
     * Creates a horizontal separator used to visually divide subgoals
     * and sections within the view.
     * @return a configured JSeparator instance
     */
    private JSeparator createDivider() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(Color.GRAY);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        return separator;
    }

    /**
     * Sets the controller used to save the displayed plan when the create button is pressed.
     * @param savePlanController the controller responsible for the save plan use case
     */
    public void setSavePlanController(SavePlanController savePlanController) {
        this.savePlanController = savePlanController;
    }

    /**
     * Returns the logical name of this view used by the dialog manager.
     * @return the view name identifier
     */
    public String getViewName() {
        return viewName;
    }
}
