package view;

import interface_adapter.show_subgoal.ShowSubgoalController;
import interface_adapter.show_subgoal.ShowSubgoalState;
import interface_adapter.show_subgoal.ShowSubgoalViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import interface_adapter.subgoal_qna.SubgoalQnaController;

/**
 * Popup view that shows a single subgoal's details:
 * name, description, priority checkbox, and a Q/A button.
 *
 * This dialog listens to the ShowSubgoalViewModel for state changes
 * and updates its components accordingly.
 */
public class SubgoalView extends JDialog implements PropertyChangeListener {

    private final ShowSubgoalViewModel viewModel;
    private final ShowSubgoalController controller;
    private SubgoalQnaController qnaController;   // controller for Q/A dialog


    private final JLabel nameLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea(5, 30);
    private final JCheckBox priorityCheckBox = new JCheckBox("Priority");
    private final JCheckBox completeCheckBox = new JCheckBox("Complete");
    private final JButton qaButton = new JButton("Q/A");

    // We need to remember which subgoal is currently being displayed
    private String currentSubgoalId = "";

    /**
     * Constructs a SubgoalView.
     *
     * @param owner     the parent frame of this dialog
     * @param viewModel the view model providing subgoal state
     * @param controller the controller to invoke the ShowSubgoal use case
     */
    public SubgoalView(JFrame owner,
                       ShowSubgoalViewModel viewModel,
                       ShowSubgoalController controller) {
        super(owner, "Subgoal", false); // modal dialog
        this.viewModel = viewModel;
        this.controller = controller;

        this.viewModel.addPropertyChangeListener(this);

        setupUI();
        setupListeners();
    }

    public void setQnaController(SubgoalQnaController qnaController) {
        this.qnaController = qnaController;
    }

    /**
     * Sets up the Swing components and layout.
     */
    private void setupUI() {
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: subgoal name
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        content.add(nameLabel, BorderLayout.NORTH);

        // Center: description
        JScrollPane scroll = new JScrollPane(descriptionArea);
        content.add(scroll, BorderLayout.CENTER);

        // Bottom: priority + Q/A button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(priorityCheckBox);
        bottomPanel.add(completeCheckBox);
        bottomPanel.add(qaButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Sets up listeners for UI interactions.
     */
    private void setupListeners() {
        // Priority checkbox
        priorityCheckBox.addActionListener(e -> {
            if (currentSubgoalId != "") {
                controller.setPriority(currentSubgoalId, priorityCheckBox.isSelected());
            }
        });

        // Complete checkbox
        completeCheckBox.addActionListener(e -> {
            if (currentSubgoalId != "") {
                controller.setCompleted(currentSubgoalId, completeCheckBox.isSelected());
            }
        });

        // Q/A button – open the Q/A dialog for this subgoal
        qaButton.addActionListener(e -> {
            // assume SubgoalView already tracks the currently opened subgoal id
            // (e.g. a field currentSubgoalId set in propertyChange / update method)
            if (qnaController != null && currentSubgoalId != null && !currentSubgoalId.isEmpty()) {
                qnaController.open(currentSubgoalId);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Q/A chat is not available for this subgoal.",
                        "Q/A",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Opens the dialog for a specific subgoal.
     * This triggers the ShowSubgoal use case and then shows the popup.
     *
     * @param subgoalId the ID of the subgoal to display
     */
    public void openForSubgoal(String subgoalId) {
        this.currentSubgoalId = subgoalId;
        controller.execute(subgoalId);
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    /**
     * Called when the ViewModel's state changes.
     * Updates the UI components.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ShowSubgoalState state = viewModel.getState();

        nameLabel.setText(state.getName());
        descriptionArea.setText(state.getDescription());
        priorityCheckBox.setSelected(state.isPriority());
        completeCheckBox.setSelected(state.isCompleted());

        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
