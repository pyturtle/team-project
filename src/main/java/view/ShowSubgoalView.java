package view;

import entity.subgoal.Subgoal;
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
public class ShowSubgoalView extends JPanel implements PropertyChangeListener {
    private final String viewName = "show subgoal";

    private final ShowSubgoalViewModel viewModel;
    private ShowSubgoalController showSubgoalController;
    private SubgoalQnaController qnaController;   // controller for Q/A dialog


    private final JLabel nameLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea(5, 30);
    private final JCheckBox priorityCheckBox = new JCheckBox("Priority");
    private final JCheckBox completeCheckBox = new JCheckBox("Complete");
    private final JButton qaButton = new JButton("Q/A");

    /**
     * Constructs a SubgoalView.
     *
     * @param viewModel the view model providing subgoal state
     */
    public ShowSubgoalView(ShowSubgoalViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setupUI();
        setupListeners();
    }

    public void setQnaController(SubgoalQnaController qnaController) {
        this.qnaController = qnaController;
    }
    public void setShowSubgoalController(ShowSubgoalController showSubgoalController) {
        this.showSubgoalController = showSubgoalController;
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

        this.setLayout(new  BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(content);
    }

    /**
     * Sets up listeners for UI interactions.
     */
    private void setupListeners() {
        // Priority checkbox
        priorityCheckBox.addActionListener(e -> {
            if (viewModel.getState().getId() != "") {
                showSubgoalController.setPriority(viewModel.getState().getId(), priorityCheckBox.isSelected());
            }
        });

        // Complete checkbox
        completeCheckBox.addActionListener(e -> {
            if (viewModel.getState().getId() != "") {
                showSubgoalController.setCompleted(viewModel.getState().getId(), completeCheckBox.isSelected());
            }
        });

        // Q/A button – open the Q/A dialog for this subgoal
        qaButton.addActionListener(e -> {
            // assume SubgoalView already tracks the currently opened subgoal id
            // (e.g. a field currentSubgoalId set in propertyChange / update method)
            if (qnaController != null && viewModel.getState().getId() != null && !viewModel.getState().getId().isEmpty()) {
                qnaController.open(viewModel.getState().getId());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Q/A chat is not available for this subgoal.",
                        "Q/A",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
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

    public String getViewName() {
        return viewName;
    }

}
