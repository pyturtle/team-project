package view;

import entity.Subgoal;
import interface_adapter.plan.show_plan.ShowPlanState;
import interface_adapter.plan.show_plan.ShowPlanViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class ShowPlanView extends JPanel implements PropertyChangeListener {

    private final String viewName = "show plan";

    private final ShowPlanViewModel showPlanViewModel;

    private final JScrollPane subgoalsContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    private final JLabel planNameLabel = new JLabel();
    private final JLabel planDescriptionLabel = new JLabel();
    private final JPanel subgoalsPanel = new JPanel();

    public ShowPlanView(ShowPlanViewModel showPlanViewModel) {
        this.showPlanViewModel = showPlanViewModel;
        showPlanViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // outer padding
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // ---------- Title ----------
        JLabel title = new JLabel(ShowPlanViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(15));

        // ---------- Plan Info ----------
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

        // ---------- Subgoals List ----------
        subgoalsPanel.setLayout(new BoxLayout(subgoalsPanel, BoxLayout.Y_AXIS));
        subgoalsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subgoalsContainer.setViewportView(subgoalsPanel);
        subgoalsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        subgoalsContainer.setBorder(null);

        add(subgoalsContainer);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ShowPlanState newState = (ShowPlanState) evt.getNewValue();

        planNameLabel.setText(newState.getPlanName());
        planDescriptionLabel.setText("<html>" + newState.getPlanDescription() + "</html>");

        // Clear old subgoals before reloading
        subgoalsPanel.removeAll();

        ArrayList<Subgoal> subgoals = newState.getSubgoalList();
        for (int i = 0; i < subgoals.size(); i++) {
            Subgoal subgoal = subgoals.get(i);
            JPanel subgoalPanel = createSubgoalPanel(
                    subgoal.getName(),
                    subgoal.getDescription(),
                    subgoal.getDeadline()
            );

            subgoalsPanel.add(subgoalPanel);

            // Add divider between subgoals
            if (i < subgoals.size() - 1) {
                subgoalsPanel.add(createDivider());
                subgoalsPanel.add(Box.createVerticalStrut(10));
            }
        }

        subgoalsPanel.revalidate();
        subgoalsPanel.repaint();
    }

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

        JLabel deadlineLabel = new JLabel("Due: " + deadline);
        deadlineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(deadlineLabel);

        return panel;
    }

    private JSeparator createDivider() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(Color.GRAY);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        return separator;
    }

    public String getViewName() {
        return viewName;
    }
}
