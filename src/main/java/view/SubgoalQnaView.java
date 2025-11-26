package view;

import entity.SubgoalQuestionAnswer;
import interface_adapter.subgoal_qna.SubgoalQnaController;
import interface_adapter.subgoal_qna.SubgoalQnaState;
import interface_adapter.subgoal_qna.SubgoalQnaViewModel;

import javax.swing.*;
import java.awt.*;
import javax.swing.ScrollPaneConstants;
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

    private final SubgoalQnaViewModel viewModel;
    private final SubgoalQnaController controller;

    private final JPanel messagesPanel = new JPanel();
    private final JTextArea questionInput = new JTextArea(3, 30);
    private final JButton sendButton = new JButton("Send");

    private String currentSubgoalId = "";

    public SubgoalQnaView(SubgoalQnaViewModel viewModel,
                          SubgoalQnaController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Subgoal Q/A");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
//        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        questionInput.setLineWrap(true);
        questionInput.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(questionInput);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this);
    }

    /** Used by AppBuilder to register this view with DialogManager. */
    public String getViewName() {
        return viewModel.getViewName();   // "subgoal qna"
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!(evt.getNewValue() instanceof SubgoalQnaState)) {
            return;
        }

        SubgoalQnaState state = (SubgoalQnaState) evt.getNewValue();
        currentSubgoalId = state.getSubgoalId();

        // rebuild messages panel
        messagesPanel.removeAll();
        List<SubgoalQuestionAnswer> history = state.getHistory();
        for (SubgoalQuestionAnswer entry : history) {
            messagesPanel.add(createMessageArea("Q: " + entry.getQuestionMessage()));
            String response = entry.getResponseMessage();
            if (response != null && !response.isEmpty()) {
                messagesPanel.add(createMessageArea("A: " + response));
            }
            messagesPanel.add(Box.createVerticalStrut(8));
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();

        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Q/A Error",
                    JOptionPane.ERROR_MESSAGE);
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
        String question = questionInput.getText().trim();
        if (question.isEmpty()) {
            return;
        }
        controller.askQuestion(currentSubgoalId, question);
        questionInput.setText("");
    }

    private JTextArea createMessageArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false); // make it blend with background
        area.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }
}
