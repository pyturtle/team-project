package view.subgoal_qa;

import entity.SubgoalQuestionAnswer;
import interface_adapter.message.SendMessageController;
import interface_adapter.message.SendMessageViewModel;
import interface_adapter.subgoal_qa.SubgoalQAState;
import interface_adapter.subgoal_qa.SubgoalQAViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Chat view for Subgoal Q/A.
 * Shown inside a dialog by DialogManager.
 */
public class SubgoalQAView extends JPanel {

    private final SubgoalQAViewModel qaViewModel;
    private final SendMessageViewModel sendMessageViewModel;

    // Controller for sending a new message (wired in AppBuilder).
    private SendMessageController sendMessageController;

    // UI components
    private final JLabel titleLabel = new JLabel("Subgoal Q/A Chat");
    private final JTextArea historyArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private final JButton sendButton = new JButton("Send");

    public SubgoalQAView(SubgoalQAViewModel qaViewModel,
                         SendMessageViewModel sendMessageViewModel) {

        this.qaViewModel = qaViewModel;
        this.sendMessageViewModel = sendMessageViewModel;

        setLayout(new BorderLayout());
        buildUI();

        // Listen for history changes
        qaViewModel.addPropertyChangeListener(evt -> refreshHistory());

        // Initial render
        refreshHistory();
    }

    private void buildUI() {
        // Top: title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(titleLabel, BorderLayout.NORTH);

        // Center: history area
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: input + send button
        JPanel bottom = new JPanel(new BorderLayout(4, 4));
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        // Send button behaviour (NULL-SAFE so tests don't crash)
        sendButton.addActionListener(e -> onSendClicked());
    }

    /**
     * Called from AppBuilder so the view can actually send messages.
     */
    public void setSendMessageController(SendMessageController controller) {
        this.sendMessageController = controller;
    }

    /**
     * Update chat history from the ViewModel state.
     */
    private void refreshHistory() {
        SubgoalQAState state = qaViewModel.getState();
        // state.getHistory() returns List<SubgoalQuestionAnswer>, not List<String>
        List<SubgoalQuestionAnswer> history = state.getHistory();

        if (history == null || history.isEmpty()) {
            historyArea.setText("");
        } else {
            StringBuilder sb = new StringBuilder();
            for (SubgoalQuestionAnswer qa : history) {
                // You can tweak this formatting if you like
                sb.append("Q: ").append(qa.getQuestionMessage()).append("\n");
                sb.append("A: ").append(qa.getResponseMessage()).append("\n\n");
            }
            historyArea.setText(sb.toString());
            historyArea.setCaretPosition(historyArea.getDocument().getLength());
        }

        // Optional: show error message somewhere if you want
        // String error = state.getErrorMessage();
    }

    /**
     * Handler for the Send button.
     * Guarded so test mains without a controller don't crash.
     */
    private void onSendClicked() {
        String message = inputField.getText();
        if (message == null || message.isBlank()) {
            return; // nothing to send
        }

        if (sendMessageController != null) {
            // Normal app path: controller is wired in AppBuilder
            sendMessageController.execute(message);
            inputField.setText("");
        } else {
            // Test path (SubgoalQATestMain): no controller wired, just avoid NPE
            System.out.println(
                    "SubgoalQAView: Send clicked but sendMessageController is null (test mode).");
        }
    }
}