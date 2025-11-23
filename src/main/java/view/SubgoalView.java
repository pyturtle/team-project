package view;

import interface_adapter.show_subgoal.ShowSubgoalController;
import interface_adapter.show_subgoal.ShowSubgoalViewModel;
import interface_adapter.subgoal_qa.SubgoalQAController;

import javax.swing.*;
import java.awt.*;

/**
 * Popup dialog that shows a single subgoal's details and provides a Q/A button.
 */
public class SubgoalView extends JDialog {

    private final ShowSubgoalViewModel viewModel;
    private ShowSubgoalController showSubgoalController;

    // Q/A controller (set from AppBuilder)
    private SubgoalQAController subgoalQAController;

    // Used as key for Q/A history (we use subgoal name as id)
    private String currentSubgoalId;

    // UI components
    private final JLabel nameLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea();
    private final JCheckBox priorityCheckBox = new JCheckBox("Priority");
    private final JCheckBox completedCheckBox = new JCheckBox("Completed");
    private final JButton qaButton = new JButton("Q / A");

    public SubgoalView(JFrame owner,
                       ShowSubgoalViewModel viewModel,
                       ShowSubgoalController controller) {

        super(owner, "Subgoal Details", true);

        this.viewModel = viewModel;
        this.showSubgoalController = controller;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(owner);

        initUI();

        // Listen for ShowSubgoalViewModel updates – just refresh on any change
        viewModel.addPropertyChangeListener(evt -> updateView());
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));
        panel.add(nameLabel);

        // Description
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(descriptionArea);
        panel.add(scroll);

        // Priority + Completed checkboxes
        panel.add(priorityCheckBox);
        panel.add(completedCheckBox);

        // Q/A BUTTON LISTENER
        qaButton.addActionListener(e -> {
            // use the current subgoal "id" (we're using the name as key)
            if (subgoalQAController != null && currentSubgoalId != null) {
                subgoalQAController.loadHistory(currentSubgoalId);
            }
        });
        panel.add(qaButton);

        this.add(panel, BorderLayout.CENTER);
    }

    // Allow AppBuilder to swap controller if needed
    public void setShowSubgoalController(ShowSubgoalController controller) {
        this.showSubgoalController = controller;
    }

    // Called by AppBuilder: subgoalView.setSubgoalQAController(...)
    public void setSubgoalQAController(SubgoalQAController controller) {
        this.subgoalQAController = controller;
    }

    /**
     * Update UI from the ViewModel state.
     */
    private void updateView() {
        var state = viewModel.getState();
        nameLabel.setText(state.getName());
        descriptionArea.setText(state.getDescription());
        priorityCheckBox.setSelected(state.isPriority());
        completedCheckBox.setSelected(state.isCompleted());

        // store id for Q/A popup (using name as key in history file)
        currentSubgoalId = state.getName();
    }
}
