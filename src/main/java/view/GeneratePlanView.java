package view;

import interface_adapter.message.SendMessageController;
import interface_adapter.message.SendMessageState;
import interface_adapter.message.SendMessageViewModel;
import interface_adapter.plan.generate_plan.GeneratePlanController;
import interface_adapter.plan.generate_plan.GeneratePlanState;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.plan.show_plan.ShowPlanController;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GeneratePlanView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "generate plan";
    private final SendMessageViewModel sendMessageViewModel;
    private GeneratePlanController generatePlanController;
    private SendMessageController sendMessageController;
    private ShowPlanController showPlanController;

    private final JTextField userMessageInputField = new JTextField(25);
    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane messagesContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JButton sendButton;

    public GeneratePlanView(GeneratePlanViewModel generatePlanViewModel, SendMessageViewModel sendMessageViewModel) {
        this.sendMessageViewModel = sendMessageViewModel;
        generatePlanViewModel.addPropertyChangeListener(this);
        sendMessageViewModel.addPropertyChangeListener(this);

        // Main layout & padding
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ----- Title -----
        final JLabel title = new JLabel(GeneratePlanViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT); // left-align ONLY the label
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f)); // match ShowPlanView title
        this.add(title);

        this.add(Box.createVerticalStrut(10));

        // ----- Messages area -----
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);
        messagesContainer.setViewportView(messagesPanel);
        messagesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        messagesContainer.getVerticalScrollBar().setUnitIncrement(16);
        this.add(messagesContainer);

        // ----- Input area -----
        final JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.X_AXIS));
        userInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sendButton = new JButton(GeneratePlanViewModel.SEND_BUTTON_LABEL);
        sendButton.setFont(sendButton.getFont().deriveFont(13f));

        userMessageInputField.setFont(userMessageInputField.getFont().deriveFont(13f));
        userMessageInputField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, userMessageInputField.getPreferredSize().height)
        );

        // Order: input field first, then button
        userInputPanel.add(userMessageInputField);
        userInputPanel.add(Box.createHorizontalStrut(8));
        userInputPanel.add(sendButton);

        this.add(userInputPanel);

        // Button listener
        sendButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        final SendMessageState sendMessageState = sendMessageViewModel.getState();
                        sendMessageController.execute(sendMessageState.getUserMessage());
                    }
                });

        userMessageInputFieldListener();
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
        final Object newState = evt.getNewValue();

        if (newState instanceof SendMessageState) {
            String userMessage = ((SendMessageState) newState).getUserMessage();

            sendButton.setEnabled(false);
            sendButton.setText("Loading...");

            JPanel textBox = createTextBox(userMessage);
            JPanel bubble = createMessageBox(textBox, true);            // true = user
            JPanel row = wrapInRow(bubble, true);                       // right side

            messagesPanel.add(row);
            messagesPanel.revalidate();
            messagesPanel.repaint();
            scrollToBottom();

            startGeneratePlanInBackground(userMessage);
        }

        if (newState instanceof GeneratePlanState) {
            sendButton.setEnabled(true);
            sendButton.setText("Send");
            GeneratePlanState newGeneratePlanState = (GeneratePlanState) newState;
            boolean success = newGeneratePlanState.isSuccess();
            String responseMessage = newGeneratePlanState.getResponseMessage();
            String userMessage = newGeneratePlanState.getUserMessage();

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);

            content.add(createTextBox(responseMessage));
            content.add(Box.createVerticalStrut(5));
            if (success) {
                JSONObject responseObject = newGeneratePlanState.getResponseObject();
                content.add(createShowPlanButton(responseObject));
            } else {
                content.add(createTryAgainButton(userMessage));
            }

            JPanel bubble = createMessageBox(content, false);           // false = response
            JPanel row = wrapInRow(bubble, false);                      // left side

            messagesPanel.add(row);
            messagesPanel.revalidate();
            messagesPanel.repaint();
            scrollToBottom();
        }
    }

    private void startGeneratePlanInBackground(String userMessage) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // heavy work here, off the EDT
                generatePlanController.execute(userMessage);
                return null;
            }
        };
        worker.execute();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = messagesContainer.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private JPanel wrapInRow(JPanel messageBubble, boolean isUser) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false); // background comes from messagesPanel

        // spacing around each message row (between bubbles)
        row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        // top, left, bottom, right

        if (isUser) {
            // push bubble to the right
            row.add(Box.createHorizontalGlue());
            row.add(messageBubble);
        } else {
            // bubble on the left
            row.add(messageBubble);
            row.add(Box.createHorizontalGlue());
        }

        return row;
    }

    private JPanel createMessageBox(JPanel messageBoxContent, boolean isUser) {
        JPanel messageBox = new JPanel();
        messageBox.setLayout(new BoxLayout(messageBox, BoxLayout.Y_AXIS));

        // bubble color: slightly different for user vs response
        Color userBubbleColor = new Color(70, 130, 180);      // steel-blue-ish
        Color responseBubbleColor = new Color(60, 60, 60);    // dark gray

        messageBox.setBackground(isUser ? userBubbleColor : responseBubbleColor);

        // padding inside the bubble
        messageBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12) // top, left, bottom, right
        ));

        messageBox.add(messageBoxContent);

        // Limit width & height to fit content nicely
        int maxWidth = 400; // tweak as desired
        messageBoxContent.revalidate();
        Dimension preferred = messageBox.getPreferredSize();
        preferred.width = Math.min(preferred.width, maxWidth);

        // height = preferred, to avoid vertical stretching
        messageBox.setMaximumSize(new Dimension(preferred.width, preferred.height));

        return messageBox;
    }

    private JPanel createTextBox(String messageText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        JLabel message = new JLabel("<html>" + messageText + "</html>", SwingConstants.CENTER);
        message.setForeground(Color.WHITE);
        message.setFont(message.getFont().deriveFont(13f)); // similar to ShowPlanView text size

        panel.add(message);
        return panel;
    }

    private JPanel createShowPlanButton(JSONObject planObject) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        JButton showPlanButton = new JButton("Show Plan");
        showPlanButton.setFont(showPlanButton.getFont().deriveFont(13f));
        showPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showPlanController.execute(planObject);
            }
        });
        panel.add(showPlanButton);
        return panel;
    }

    private JPanel createTryAgainButton(String userMessage) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        JButton button = new JButton("Try Again");
        button.setFont(button.getFont().deriveFont(13f));
        button.addActionListener(evt -> generatePlanController.execute(userMessage));
        panel.add(button);
        return panel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setGeneratePlanController(GeneratePlanController generatePlanController) {
        this.generatePlanController = generatePlanController;
    }

    public void setSendMessageController(SendMessageController sendMessageController) {
        this.sendMessageController = sendMessageController;
    }

    public void setShowPlanController(ShowPlanController showPlanController) {
        this.showPlanController = showPlanController;
    }
}
