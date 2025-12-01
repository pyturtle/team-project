package view.subgoal;

import entity.subgoal.SubgoalQuestionAnswer;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.subgoal.subgoal_qna.SubgoalQnaController;
import interface_adapter.subgoal.subgoal_qna.SubgoalQnaState;
import interface_adapter.subgoal.subgoal_qna.SubgoalQnaViewModel;
import view.ui_elements.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Chat-style panel for Subgoal Q/A, shown in a JDialog via DialogManager.
 */
public class SubgoalQnaView extends JPanel
        implements PropertyChangeListener, ActionListener {

    private final SubgoalQnaViewModel subgoalQnaViewModel;
    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane messagesScrollPane = new JScrollPane(messagesPanel);
    private final JTextArea questionInput = new JTextArea(3, 30);
    private final JButton sendButton = new JButton(SubgoalQnaViewModel.SEND_BUTTON_LABEL);
    private SubgoalQnaController subgoalQnaController;
    private String currentSubgoalId = "";

    public SubgoalQnaView(SubgoalQnaViewModel subgoalQnaViewModel) {
        this.subgoalQnaViewModel = subgoalQnaViewModel;
        this.subgoalQnaViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        setPreferredSize(new Dimension(700, 600));

        JLabel title = new JLabel("Subgoal Q/A");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(messagesScrollPane, BorderLayout.CENTER);

        questionInput.setLineWrap(true);
        questionInput.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(questionInput);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this);
    }

    /**
     * Used by AppBuilder to register this view with DialogManager.
     */
    public String getViewName() {
        return "subgoal qna";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newState = evt.getNewValue();

        if (newState instanceof SubgoalQnaState) {
            SubgoalQnaState state = (SubgoalQnaState) evt.getNewValue();
            messagesPanel.removeAll();
            showLastMessages();
            messagesPanel.revalidate();
            messagesPanel.repaint();
            Message.scrollToBottom(messagesScrollPane);

            currentSubgoalId = state.getSubgoalId();
            sendButton.setEnabled(true);
            sendButton.setText(SubgoalQnaViewModel.SEND_BUTTON_LABEL);

            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        state.getErrorMessage(),
                        "Q/A Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentSubgoalId == null || currentSubgoalId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No subgoal selected.",
                    "Q/A",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        sendButton.setEnabled(false);
        sendButton.setText(GeneratePlanViewModel.LOADING_LABEL);
        String question = questionInput.getText();
        messagesPanel.add(Message.createMessage(Message.createTextBox(question), true));
        messagesPanel.revalidate();
        messagesPanel.repaint();
        Message.scrollToBottom(messagesScrollPane);

        if (question.isEmpty()) {
            return;
        }
        sendButton.setEnabled(false);
        sendButton.setText(SubgoalQnaViewModel.LOADING_LABEL);
        questionInput.setText("");
        startAskQuestionInBackground(question);
    }

    private void showLastMessages() {
        List<SubgoalQuestionAnswer> history = subgoalQnaViewModel.getState().getHistory();
        for (SubgoalQuestionAnswer entry : history) {
            messagesPanel.add(Message.createMessage(Message.createTextBox(entry.getQuestionMessage()),
                    true));
            messagesPanel.add(Message.createMessage(Message.createTextBox(entry.getResponseMessage()),
                    false));
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
        Message.scrollToBottom(messagesScrollPane);
    }


    private void startAskQuestionInBackground(String question) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                subgoalQnaController.askQuestion(currentSubgoalId, question);
                return null;
            }
        };
        worker.execute();
    }


    public void setSubgoalQnaController(SubgoalQnaController subgoalQnaController) {
        this.subgoalQnaController = subgoalQnaController;
    }
}
