package app;

import interface_adapter.subgoal_qa.SubgoalQAState;
import interface_adapter.subgoal_qa.SubgoalQAViewModel;
import interface_adapter.message.SendMessageState;
import interface_adapter.message.SendMessageViewModel;
import view.subgoal_qa.SubgoalQAView;

import javax.swing.*;
import java.util.Collections;

/**
 * Tiny launcher to open the Subgoal Q/A chat view with fake data.
 * This is just for visually testing SubgoalQAView, not the full Gemini pipeline.
 */
public class SubgoalQATestMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Basic frame
            JFrame frame = new JFrame("Subgoal Q/A Test");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // ---- Subgoal Q/A ViewModel with fake state ----
            SubgoalQAViewModel qaViewModel = new SubgoalQAViewModel();
            SubgoalQAState qaState = qaViewModel.getState();
            qaState.setSubgoalId("Test Subgoal");
            // empty history for now (no previous Q/A)
            qaState.setHistory(Collections.emptyList());
            qaState.setErrorMessage("");
            qaViewModel.setState(qaState);
            qaViewModel.firePropertyChange();

            // ---- SendMessage ViewModel with empty state ----
            SendMessageViewModel sendMessageViewModel = new SendMessageViewModel();
            SendMessageState sendState = new SendMessageState();
            sendMessageViewModel.setState(sendState);
            sendMessageViewModel.firePropertyChange();

            // ---- Build the Q/A view ----
            SubgoalQAView qaView = new SubgoalQAView(qaViewModel, sendMessageViewModel);

            // Put the Q/A view in the frame
            frame.setContentPane(qaView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
