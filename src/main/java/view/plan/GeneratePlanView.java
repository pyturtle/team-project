package view.plan;

import interface_adapter.plan.generate_plan.GeneratePlanController;
import interface_adapter.plan.generate_plan.GeneratePlanState;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.plan.show_plan.ShowPlanController;
import org.json.JSONObject;
import view.ui_elements.Message;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GeneratePlanView extends JPanel implements PropertyChangeListener {
    private final JTextField userMessageInputField = new JTextField(25);
    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane messagesContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final JButton sendButton;
    private GeneratePlanController generatePlanController;
    private ShowPlanController showPlanController;

    public GeneratePlanView(GeneratePlanViewModel generatePlanViewModel) {
        generatePlanViewModel.addPropertyChangeListener(this);


        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);


        final JLabel title = new JLabel(GeneratePlanViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        centerPanel.add(title);

        centerPanel.add(Box.createVerticalStrut(10));


        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);


        messagesPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                )
        );

        messagesContainer.setViewportView(messagesPanel);
        messagesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        messagesContainer.getVerticalScrollBar().setUnitIncrement(16);

        centerPanel.add(messagesContainer);


        this.add(centerPanel, BorderLayout.CENTER);


        final JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.X_AXIS));

        sendButton = new JButton(GeneratePlanViewModel.SEND_BUTTON_LABEL);
        sendButton.setFont(sendButton.getFont().deriveFont(13f));

        userMessageInputField.setFont(userMessageInputField.getFont().deriveFont(13f));
        userMessageInputField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, userMessageInputField.getPreferredSize().height)
        );

        userInputPanel.add(userMessageInputField);
        userInputPanel.add(Box.createHorizontalStrut(8));
        userInputPanel.add(sendButton);


        this.add(userInputPanel, BorderLayout.SOUTH);


        sendButton.addActionListener(
                evt -> {
                    String message = userMessageInputField.getText();
                    if (message == null || message.trim().isEmpty()) {
                        return;
                    }
                    messagesPanel.add(Message.createMessage(Message.createTextBox(message),
                            true));
                    messagesPanel.revalidate();
                    messagesPanel.repaint();
                    Message.scrollToBottom(messagesContainer);

                    userMessageInputField.setText("");
                    sendButton.setEnabled(false);
                    sendButton.setText(GeneratePlanViewModel.LOADING_LABEL);
                    startGeneratePlanInBackground(message);
                });
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newState = evt.getNewValue();

        if (newState instanceof GeneratePlanState) {
            sendButton.setEnabled(true);
            sendButton.setText(GeneratePlanViewModel.SEND_BUTTON_LABEL);
            GeneratePlanState newGeneratePlanState = (GeneratePlanState) newState;
            boolean success = newGeneratePlanState.isSuccess();
            String responseMessage = newGeneratePlanState.getResponseMessage();
            String userMessage = newGeneratePlanState.getUserMessage();

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);

            content.add(Message.createTextBox(responseMessage));
            content.add(Box.createVerticalStrut(5));
            if (success) {
                JSONObject responseObject = newGeneratePlanState.getResponseObject();
                content.add(createShowPlanButton(responseObject));
            } else {
                content.add(createTryAgainButton(userMessage));
            }
            messagesPanel.add(Message.createMessage(content, false));
            messagesPanel.revalidate();
            messagesPanel.repaint();
            Message.scrollToBottom(messagesContainer);
        }
    }

    private void startGeneratePlanInBackground(String userMessage) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {

                generatePlanController.execute(userMessage);
                return null;
            }
        };
        worker.execute();
    }

    private JPanel createShowPlanButton(JSONObject planObject) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        JButton showPlanButton = new JButton(GeneratePlanViewModel.SHOW_PLAN_BUTTON_LABEL);
        showPlanButton.setFont(showPlanButton.getFont().deriveFont(13f));
        showPlanButton.addActionListener(evt -> showPlanController.execute(planObject));
        panel.add(showPlanButton);
        return panel;
    }

    private JPanel createTryAgainButton(String userMessage) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        JButton button = new JButton(GeneratePlanViewModel.TRY_AGAIN_BUTTON_LABEL);
        button.setFont(button.getFont().deriveFont(13f));
        button.addActionListener(evt -> {
            button.setEnabled(false);
            sendButton.setEnabled(false);
            sendButton.setText(GeneratePlanViewModel.LOADING_LABEL);
            startGeneratePlanInBackground(userMessage);
        });
        panel.add(button);
        return panel;
    }

    public String getViewName() {
        return "generate plan";
    }

    public void setGeneratePlanController(GeneratePlanController generatePlanController) {
        this.generatePlanController = generatePlanController;
    }

    public void setShowPlanController(ShowPlanController showPlanController) {
        this.showPlanController = showPlanController;
    }
}
