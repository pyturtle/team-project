package view.plan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import org.json.JSONObject;

import interface_adapter.plan.generate_plan.GeneratePlanController;
import interface_adapter.plan.generate_plan.GeneratePlanState;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.plan.show_plan.ShowPlanController;
import view.ui_elements.Message;

/**
 * GeneratePlanView is a view that lets the user enter a prompt to generate a plan,
 * displays the conversation messages, and provides actions to show the generated plan
 * or try generating it again.
 */
public class GeneratePlanView extends JPanel implements PropertyChangeListener {

    private static final int USER_MESSAGE_COLUMNS = 25;
    private static final int BORDER_SIZE = 15;
    private static final int MESSAGE_BORDER_SIZE = 5;
    private static final int MESSAGE_TOP_PADDING = 5;
    private static final int MESSAGE_BOTTOM_PADDING = 10;
    private static final int MESSAGE_SCROLL_UNIT_INCREMENT = 16;
    private static final int MESSAGE_VERTICAL_STRUT = 10;
    private static final int INPUT_HORIZONTAL_STRUT = 8;
    private static final int CONTENT_VERTICAL_STRUT = 5;

    private static final float TITLE_FONT_SIZE = 18.0f;
    private static final float CONTROL_FONT_SIZE = 13.0f;

    private static final Color MESSAGE_BORDER_COLOR = new Color(200, 200, 200);

    private final JTextField userMessageInputField = new JTextField(USER_MESSAGE_COLUMNS);
    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane messagesContainer = new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JButton sendButton = new JButton();
    private GeneratePlanController generatePlanController;
    private ShowPlanController showPlanController;

    /**
     * Creates a new GeneratePlanView and binds it to the given view model.
     * The view sets up all UI components and registers as a property change listener.
     *
     * @param generatePlanViewModel the view model that provides state for this view
     */
    public GeneratePlanView(GeneratePlanViewModel generatePlanViewModel) {
        generatePlanViewModel.addPropertyChangeListener(this);

        this.setLayout(new java.awt.BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(
                BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        final JLabel title = new JLabel(GeneratePlanViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        centerPanel.add(title);

        centerPanel.add(Box.createVerticalStrut(MESSAGE_VERTICAL_STRUT));

        setUpMessagesPanel();
        setUpMessagesContainer();
        centerPanel.add(messagesContainer);
        setUpUserInputPanel();
        this.add(centerPanel, java.awt.BorderLayout.CENTER);
        setUpActionListeners();
    }

    /**
     * Sets up the send button action listener that validates the user message,
     * appends it to the messages panel, updates the UI, and triggers plan generation
     * in the background when a non-empty message is submitted.
     */
    private void setUpActionListeners() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                final String message = userMessageInputField.getText();
                if (message != null && !message.trim().isEmpty()) {
                    messagesPanel.add(
                            Message.createMessage(Message.createTextBox(message), true));
                    messagesPanel.revalidate();
                    messagesPanel.repaint();
                    Message.scrollToBottom(messagesContainer);

                    userMessageInputField.setText("");
                    sendButton.setEnabled(false);
                    sendButton.setText(GeneratePlanViewModel.LOADING_LABEL);
                    startGeneratePlanInBackground(message);
                }
            }
        });
    }

    /**
     * Configures the messages panel layout, transparency, and border styling
     * for displaying conversation messages.
     */
    private void setUpMessagesPanel() {
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setOpaque(false);

        messagesPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(MESSAGE_BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(
                                MESSAGE_BORDER_SIZE,
                                MESSAGE_BORDER_SIZE,
                                MESSAGE_BORDER_SIZE,
                                MESSAGE_BORDER_SIZE
                        )
                )
        );
    }

    /**
     * Configures the scrollable container that wraps the messages panel,
     * including padding, alignment, and scroll speed.
     */
    private void setUpMessagesContainer() {
        messagesContainer.setViewportView(messagesPanel);
        messagesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesContainer.setBorder(BorderFactory.createEmptyBorder(
                MESSAGE_TOP_PADDING, 0, MESSAGE_BOTTOM_PADDING, 0));
        messagesContainer.getVerticalScrollBar()
                .setUnitIncrement(MESSAGE_SCROLL_UNIT_INCREMENT);
    }

    /**
     * Configures the user input panel containing the text field and send button,
     * sets fonts and sizing, and adds it to the bottom of the view.
     */
    private void setUpUserInputPanel() {
        final JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.X_AXIS));

        sendButton = new JButton(GeneratePlanViewModel.SEND_BUTTON_LABEL);
        sendButton.setFont(sendButton.getFont().deriveFont(CONTROL_FONT_SIZE));

        userMessageInputField.setFont(
                userMessageInputField.getFont().deriveFont(CONTROL_FONT_SIZE));
        userMessageInputField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        userMessageInputField.getPreferredSize().height
                )
        );

        userInputPanel.add(userMessageInputField);
        userInputPanel.add(Box.createHorizontalStrut(INPUT_HORIZONTAL_STRUT));
        userInputPanel.add(sendButton);

        this.add(userInputPanel, java.awt.BorderLayout.SOUTH);
    }

    /**
     * Handles property change events from the GeneratePlanViewModel.
     * When the state changes, this method updates the UI with the response message,
     * and either shows a button to view the plan or to try again.
     *
     * @param evt the property change event containing the new state
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newState = evt.getNewValue();

        if (newState instanceof GeneratePlanState) {
            sendButton.setEnabled(true);
            sendButton.setText(GeneratePlanViewModel.SEND_BUTTON_LABEL);
            final GeneratePlanState newGeneratePlanState = (GeneratePlanState) newState;
            final boolean success = newGeneratePlanState.isSuccess();
            final String responseMessage = newGeneratePlanState.getResponseMessage();
            final String userMessage = newGeneratePlanState.getUserMessage();

            final JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);

            content.add(Message.createTextBox(responseMessage));
            content.add(Box.createVerticalStrut(CONTENT_VERTICAL_STRUT));
            if (success) {
                final JSONObject responseObject = newGeneratePlanState.getResponseObject();
                content.add(createShowPlanButton(responseObject));
            }
            else {
                content.add(createTryAgainButton(userMessage));
            }
            messagesPanel.add(Message.createMessage(content, false));
            messagesPanel.revalidate();
            messagesPanel.repaint();
            Message.scrollToBottom(messagesContainer);
        }
    }

    /**
     * Starts the generate plan use case in a background thread using SwingWorker.
     * This keeps the UI responsive while the plan is being generated.
     *
     * @param userMessage the message entered by the user to generate a plan
     */
    private void startGeneratePlanInBackground(String userMessage) {
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                generatePlanController.execute(userMessage);
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Creates a panel containing a button that, when clicked,
     * triggers showing the generated plan.
     *
     * @param planObject the JSON object representing the generated plan
     * @return a panel containing the Show Plan button
     */
    private JPanel createShowPlanButton(JSONObject planObject) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        final JButton showPlanButton = new JButton(
                GeneratePlanViewModel.SHOW_PLAN_BUTTON_LABEL);
        showPlanButton.setFont(showPlanButton.getFont().deriveFont(CONTROL_FONT_SIZE));
        showPlanButton.addActionListener(evt -> showPlanController.execute(planObject));
        panel.add(showPlanButton);
        return panel;
    }

    /**
     * Creates a panel containing a button that retries plan generation
     * with the given message. When clicked, the button is disabled and
     * the generate plan process is started again.
     *
     * @param userMessage the message to reuse when retrying plan generation
     * @return a panel containing the Try Again button
     */
    private JPanel createTryAgainButton(String userMessage) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        final JButton button = new JButton(GeneratePlanViewModel.TRY_AGAIN_BUTTON_LABEL);
        button.setFont(button.getFont().deriveFont(CONTROL_FONT_SIZE));
        button.addActionListener(evt -> {
            button.setEnabled(false);
            sendButton.setEnabled(false);
            sendButton.setText(GeneratePlanViewModel.LOADING_LABEL);
            startGeneratePlanInBackground(userMessage);
        });
        panel.add(button);
        return panel;
    }

    /**
     * Returns the logical name of this view used by the view manager.
     *
     * @return the view name identifier
     */
    public String getViewName() {
        return "generate plan";
    }

    /**
     * Sets the controller used to trigger the generate plan use case.
     *
     * @param generatePlanController the controller to use for plan generation
     */
    public void setGeneratePlanController(GeneratePlanController generatePlanController) {
        this.generatePlanController = generatePlanController;
    }

    /**
     * Sets the controller used to trigger the show plan use case.
     *
     * @param showPlanController the controller to use for showing the generated plan
     */
    public void setShowPlanController(ShowPlanController showPlanController) {
        this.showPlanController = showPlanController;
    }
}
