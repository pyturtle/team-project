package view.plan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import interface_adapter.plan.save_plan.SavePlanController;
import interface_adapter.plan.show_plan.ShowPlanState;
import interface_adapter.plan.show_plan.ShowPlanViewModel;

/**
 * ShowPlanView is a view that displays a generated plan,
 * including its name, description, and list of subgoals, and allows
 * the user to save the plan if it does not already exist.
 */
public class ShowPlanView extends JPanel implements PropertyChangeListener {

    private static final int MAIN_BORDER_PADDING = 15;
    private static final float TITLE_FONT_SIZE = 18f;
    private static final float PLAN_NAME_FONT_SIZE = 16f;
    private static final int TITLE_VERTICAL_STRUT = 15;
    private static final int PLAN_INFO_VERTICAL_STRUT = 5;
    private static final int SUBGOAL_SEPARATOR_VERTICAL_STRUT = 10;
    private static final int SUBGOAL_PANEL_BORDER_PADDING = 10;
    private static final int SUBGOAL_NAME_VERTICAL_STRUT = 4;
    private static final float SUBGOAL_DESCRIPTION_FONT_SIZE = 13f;
    private static final int SUBGOALS_RIGHT_PADDING = 10;
    private static final int SUBGOAL_BACKGROUND_COLOR_COMPONENT = 245;
    private static final int DIVIDER_HEIGHT = 1;

    private final String viewName = "show plan";
    private final JScrollPane subgoalsContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JLabel planNameLabel = new JLabel();
    private final JLabel planDescriptionLabel = new JLabel();
    private final JPanel subgoalsPanel = new JPanel();
    private final JButton createPlanButton = new JButton(ShowPlanViewModel.CREATE_BUTTON_LABEL);
    private SavePlanController savePlanController;

    private final ShowPlanViewModel showPlanViewModel;

    /**
     * Creates a new ShowPlanView and registers it as a listener
     * to the provided ShowPlanViewModel.
     * The view displays plan information and subgoals and provides
     * a button to create the plan in persistent storage.
     *
     * @param showPlanViewModel the view model whose state changes this view observes
     */
    public ShowPlanView(ShowPlanViewModel showPlanViewModel) {
        this.showPlanViewModel = showPlanViewModel;
        showPlanViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(
                MAIN_BORDER_PADDING, MAIN_BORDER_PADDING, MAIN_BORDER_PADDING, MAIN_BORDER_PADDING));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel title = new JLabel(ShowPlanViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(TITLE_VERTICAL_STRUT));

        setUpPlanInfoPanel();

        add(createDivider());
        add(Box.createVerticalStrut(SUBGOAL_SEPARATOR_VERTICAL_STRUT));

        setUpSubgoalsContainer();

        setUpActionListeners();
        add(createPlanButton);
    }

    /**
     * Sets up the panel that displays the plan name and description,
     * including layout, padding, and font styling.
     */
    private void setUpPlanInfoPanel() {
        final JPanel planInfoPanel = new JPanel();
        planInfoPanel.setLayout(new BoxLayout(planInfoPanel, BoxLayout.Y_AXIS));
        planInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        planInfoPanel.setBorder(BorderFactory.createEmptyBorder(
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING));

        planNameLabel.setFont(planNameLabel.getFont().deriveFont(Font.BOLD, PLAN_NAME_FONT_SIZE));
        planNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        planDescriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        planInfoPanel.add(planNameLabel);
        planInfoPanel.add(Box.createVerticalStrut(PLAN_INFO_VERTICAL_STRUT));
        planInfoPanel.add(planDescriptionLabel);

        add(planInfoPanel);
    }

    /**
     * Configures the scrollable container and panel used to display the list of subgoals.
     * It sets layout, alignment, padding, and attaches the panel to the view.
     */
    private void setUpSubgoalsContainer() {
        subgoalsPanel.setLayout(new BoxLayout(subgoalsPanel, BoxLayout.Y_AXIS));
        subgoalsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subgoalsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, SUBGOALS_RIGHT_PADDING));

        subgoalsContainer.setViewportView(subgoalsPanel);
        subgoalsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        subgoalsContainer.setBorder(null);

        add(subgoalsContainer);
    }

    /**
     * Sets up action listeners for the create plan button so that,
     * when clicked, the current plan state is sent to the save plan controller
     * and the button is disabled to prevent duplicate submissions.
     */
    private void setUpActionListeners() {
        createPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                final ShowPlanState currentState = showPlanViewModel.getState();
                savePlanController.execute(currentState.getPlanName(),
                        currentState.getPlanDescription(),
                        currentState.getUsername(),
                        currentState.getSubgoalList());
                createPlanButton.setEnabled(false);
            }
        });
    }

    /**
     * Handles property change events from the ShowPlanViewModel.
     * Updates the labels, subgoals list, and create button enabled state
     * based on the new ShowPlanState.
     *
     * @param evt the property change event containing the new state
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ShowPlanState newState = (ShowPlanState) evt.getNewValue();
        createPlanButton.setEnabled(!newState.isPlanExists());
        planNameLabel.setText(newState.getPlanName());
        planDescriptionLabel.setText("<html>" + newState.getPlanDescription() + "</html>");

        subgoalsPanel.removeAll();

        final ArrayList<Map<String, String>> subgoals = newState.getSubgoalList();
        for (int i = 0; i < subgoals.size(); i++) {
            final Map<String, String> subgoal = subgoals.get(i);
            final JPanel subgoalPanel = createSubgoalPanel(
                    subgoal.get("name"),
                    subgoal.get("description"),
                    LocalDate.parse(subgoal.get("deadline"))
            );

            subgoalsPanel.add(subgoalPanel);

            if (i < subgoals.size() - 1) {
                subgoalsPanel.add(createDivider());
                subgoalsPanel.add(Box.createVerticalStrut(SUBGOAL_SEPARATOR_VERTICAL_STRUT));
            }
        }

        subgoalsPanel.revalidate();
        subgoalsPanel.repaint();
    }

    /**
     * Creates a panel showing a single subgoal with its name,
     * description, and deadline.
     *
     * @param name the subgoal name
     * @param description the subgoal description
     * @param deadline the subgoal deadline date
     * @return a panel displaying the subgoal information
     */
    private JPanel createSubgoalPanel(String name, String description, LocalDate deadline) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING,
                SUBGOAL_PANEL_BORDER_PADDING));
        panel.setBackground(new Color(
                SUBGOAL_BACKGROUND_COLOR_COMPONENT,
                SUBGOAL_BACKGROUND_COLOR_COMPONENT,
                SUBGOAL_BACKGROUND_COLOR_COMPONENT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, PLAN_NAME_FONT_SIZE - 2));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(descLabel.getFont().deriveFont(SUBGOAL_DESCRIPTION_FONT_SIZE));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
                SUBGOALS_RIGHT_PADDING - SUBGOAL_PANEL_BORDER_PADDING));

        final JLabel deadlineLabel = new JLabel("Due: " + deadline);
        deadlineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(SUBGOAL_NAME_VERTICAL_STRUT));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(SUBGOAL_NAME_VERTICAL_STRUT));
        panel.add(deadlineLabel);

        return panel;
    }

    /**
     * Creates a horizontal separator used to visually divide subgoals
     * and sections within the view.
     *
     * @return a configured JSeparator instance
     */
    private JSeparator createDivider() {
        final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, DIVIDER_HEIGHT));
        separator.setForeground(Color.GRAY);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        return separator;
    }

    /**
     * Sets the controller used to save the displayed plan when the create button is pressed.
     *
     * @param savePlanController the controller responsible for the save plan use case
     */
    public void setSavePlanController(SavePlanController savePlanController) {
        this.savePlanController = savePlanController;
    }

    /**
     * Returns the logical name of this view used by the dialog manager.
     *
     * @return the view name identifier
     */
    public String getViewName() {
        return viewName;
    }
}
