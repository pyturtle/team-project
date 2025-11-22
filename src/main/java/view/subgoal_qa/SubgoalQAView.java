package view.subgoal_qa;

import interface_adapter.message.SendMessageController;
import interface_adapter.message.SendMessageState;
import interface_adapter.message.SendMessageViewModel;
import interface_adapter.subgoal_qa.SubgoalQAController;
import interface_adapter.subgoal_qa.SubgoalQAState;
import interface_adapter.subgoal_qa.SubgoalQAViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A popup panel that shows the Q/A chat for a specific subgoal.
 * Reuses the team's SendMessage pipeline for input UI.
 */
public class SubgoalQAView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "subgoal q/a";

    private final SubgoalQAViewModel subgoalQAViewModel;
    private final SendMessageViewModel sendMessageViewModel;
    private SubgoalQAController subgoalQAController;
    private SendMessageController sendMessageController;

    private final JTextField userMessageInputField = new JTextField(25);
    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane messagesContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    private final JButton sendButton;

    // current subgoal being chatted about
    private String currentSubgoalId = "";

    // avoid re-rendering entire history every time
    private int renderedHistoryCount = 0;

    public SubgoalQAView(SubgoalQAViewModel subgoalQAViewModel,
                         SendMessageViewModel sendMessageViewModel) {
        this.subgoalQAViewModel = subgoalQAViewModel;
        this.sendMessageViewModel = sendMessageViewModel;

        subgoalQAViewModel.addPropertyChangeListener(this);
        sendMessageViewModel.addPropertyChangeListener(this);

        // layout like GeneratePlanView
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel(SubgoalQAViewModel.TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        this.add(title);
        this.add(Box.createVerticalStrut(10));

        // messages area
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);
        messagesContainer.setViewportView(messagesPanel);
        messagesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        messagesContainer.getVerticalScrollBar().setUnitIncrement(16);
        this.add(messagesContainer);

        // input area
        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.X_AXIS));
        userInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sendButton = new JButton("Send");
        sendButton.setFont(sendButton.getFont().deriveFont(13f));

        userMessageInputField.setFont(userMessageInputField.getFont().deriveFont(13f));
        userMessageInputField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, userMessageInputField.getPreferredSize().height));

        userInputPanel.add(userMessageInputField);
        userInputPanel.add(Box.createHorizontalStrut(8));
        userInputPanel.add(sendButton);

        this.add(userInputPanel);

        // click send = trigger SendMessage use case
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                final SendMessageState sendMessageState = sendMessageViewModel.getState();
                sendMessageController.execute(sendMessageState.getUserMessage());
                userMessageInputField.setText("");
            }
        });

        userMessageInputFieldListener();
    }

    /**
     * Call this BEFORE opening the dialog, to load correct history.
     */
    public void setSubgoalId(String subgoalId) {
        this.currentSubgoalId = subgoalId;
        this.renderedHistoryCount = 0;
        this.messagesPanel.removeAll();
        this.messagesPanel.revalidate();
        this.messagesPanel.repaint();
        subgoalQAController.loadHistory(subgoalId);
    }

    private void userMessageInputFieldListener() {
        userMessageInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SendMessageState sendMessageState = sendMessageViewModel.getState();
                sendMessageState.setUserMessage(userMessageInputField.getText());
                sendMessageViewModel.setState(sendMessageState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // unused
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newState = evt.getNewValue();

        // 1) When SendMessageState updates, treat it as "user pressed send"
        if (newState instanceof SendMessageState) {
            String userMessage = ((SendMessageState) newState).getUserMessage();

            if (userMessage == null || userMessage.trim().isEmpty()) {
                return;
            }

            sendButton.setEnabled(false);
            sendButton.setText("Loading...");

            addBubble(userMessage, true);

            // trigger YOUR Q/A use case
            subgoalQAController.askQuestion(currentSubgoalId, userMessage);
        }

        // 2) When SubgoalQAState updates, render any new history items
        if (newState instanceof SubgoalQAState) {
            SubgoalQAState state = (SubgoalQAState) newState;

            // show new answers (only those not rendered yet)
            for (int i = renderedHistoryCount; i < state.getHistory().size(); i++) {
                String q = state.getHistory().get(i).getQuestionMessage();
                String a = state.getHistory().get(i).getResponseMessage();

                // question already shown as user bubble at send-time,
                // so only add AI response bubble here
                if (a != null && !a.isEmpty()) {
                    addBubble(a, false);
                }
            }
            renderedHistoryCount = state.getHistory().size();

            sendButton.setEnabled(true);
            sendButton.setText("Send");

            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        state.getErrorMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------- Bubble helpers (copied style from GeneratePlanView) ----------

    private void addBubble(String messageText, boolean isUser) {
        JPanel textBox = createTextBox(messageText);
        JPanel bubble = createMessageBox(textBox, isUser);
        JPanel row = wrapInRow(bubble, isUser);

        messagesPanel.add(row);
        messagesPanel.revalidate();
        messagesPanel.repaint();
        scrollToBottom();
    }

    private JPanel wrapInRow(JPanel bubble, boolean isUser) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (isUser) {
            row.add(Box.createHorizontalGlue());
            row.add(bubble);
        } else {
            row.add(bubble);
            row.add(Box.createHorizontalGlue());
        }
        return row;
    }

    private JPanel createMessageBox(JPanel content, boolean isUser) {
        JPanel messageBox = new JPanel();
        messageBox.setLayout(new BoxLayout(messageBox, BoxLayout.Y_AXIS));

        Color userBubbleColor = new Color(70, 130, 180);
        Color responseBubbleColor = new Color(60, 60, 60);

        messageBox.setBackground(isUser ? userBubbleColor : responseBubbleColor);
        messageBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        messageBox.add(content);
        return messageBox;
    }

    private JPanel createTextBox(String messageText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        JLabel message = new JLabel("<html>" + messageText + "</html>", SwingConstants.CENTER);
        message.setForeground(Color.WHITE);
        message.setFont(message.getFont().deriveFont(13f));

        panel.add(message);
        return panel;
    }

    private void scrollToBottom() {
        JScrollBar vertical = messagesContainer.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public String getViewName() {
        return viewName;
    }

    public void setSubgoalQAController(SubgoalQAController controller) {
        this.subgoalQAController = controller;
    }

    public void setSendMessageController(SendMessageController controller) {
        this.sendMessageController = controller;
    }
}
