package use_case.subgoal.qna;

import entity.SubgoalQuestionAnswer;
import entity.subgoal.Subgoal;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Interactor for Subgoal Q/A.
 * Provides Gemini with subgoal context, plan subgoals, and previous Q/A history.
 */
public class SubgoalQnaInteractor implements SubgoalQnaInputBoundary {

    private final SubgoalQnaDataAccessInterface qnaDAO;
    private final SubgoalDataAccessInterface subgoalDAO;
    private final SubgoalQnaGeminiDataAccessInterface geminiGateway;
    private final SubgoalQnaOutputBoundary presenter;

    public SubgoalQnaInteractor(SubgoalQnaDataAccessInterface qnaDAO,
                                SubgoalDataAccessInterface subgoalDAO,
                                SubgoalQnaGeminiDataAccessInterface geminiGateway,
                                SubgoalQnaOutputBoundary presenter) {
        this.qnaDAO = qnaDAO;
        this.subgoalDAO = subgoalDAO;
        this.geminiGateway = geminiGateway;
        this.presenter = presenter;
    }

    @Override
    public void open(SubgoalQnaOpenInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        List<SubgoalQuestionAnswer> history = qnaDAO.getHistory(subgoalId);
        SubgoalQnaOutputData out = new SubgoalQnaOutputData(subgoalId, history);
        presenter.presentInitial(out);
    }

    @Override
    public void ask(SubgoalQnaAskInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        String question = inputData.getQuestionMessage();

        if (question == null || question.trim().isEmpty()) {
            presenter.presentError("Question cannot be empty.");
            return;
        }

        // 1. Load current subgoal
        Subgoal subgoal = subgoalDAO.getSubgoalById(subgoalId);
        String subgoalName = subgoal != null ? subgoal.getName() : "(unknown)";
        String subgoalDescription = subgoal != null ? subgoal.getDescription() : "";
        String planId = subgoal != null ? subgoal.getPlanId() : "";
        String username = subgoal != null ? subgoal.getUsername() : "";

        // 2. Get all subgoals in the same plan (regardless of user)
        List<Subgoal> planSubgoals = new ArrayList<>();
        if (subgoal != null && planId != null && !planId.isEmpty()) {
            planSubgoals = subgoalDAO.getSubgoalsByPlanId(planId);
            // Sort subgoals: by deadline (if any), then by name
            Collections.sort(planSubgoals, new Comparator<Subgoal>() {
                @Override
                public int compare(Subgoal a, Subgoal b) {
                    if (a.getDeadline() == null && b.getDeadline() == null) {
                        return a.getName().compareToIgnoreCase(b.getName());
                    } else if (a.getDeadline() == null) {
                        return 1;
                    } else if (b.getDeadline() == null) {
                        return -1;
                    } else {
                        int cmp = a.getDeadline().compareTo(b.getDeadline());
                        if (cmp != 0) {
                            return cmp;
                        }
                        return a.getName().compareToIgnoreCase(b.getName());
                    }
                }
            });
        }

        // Find index of current subgoal in that plan list
        int currentIndex = -1;
        for (int i = 0; i < planSubgoals.size(); i++) {
            if (planSubgoals.get(i).getId().equals(subgoalId)) {
                currentIndex = i;
                break;
            }
        }

        // 3. Load previous Q/A history for this subgoal
        List<SubgoalQuestionAnswer> history = qnaDAO.getHistory(subgoalId);
        int maxPairs = 5;
        int startIndex = Math.max(0, history.size() - maxPairs);

        // 4. Build a context-rich prompt for Gemini
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are helping a user with a specific subgoal in their planning app.\n");
        prompt.append("Here is the subgoal context:\n");
        if (!username.isEmpty()) {
            prompt.append("- User: ").append(username).append("\n");
        }
        if (!planId.isEmpty()) {
            prompt.append("- Plan ID: ").append(planId).append("\n");
        }
        prompt.append("- Subgoal name: ").append(subgoalName).append("\n");
        if (!subgoalDescription.isEmpty()) {
            prompt.append("- Subgoal description: ").append(subgoalDescription).append("\n");
        }
        prompt.append("\n");

        // Plan-level context: all subgoals, and which is current / before / after
        if (!planSubgoals.isEmpty()) {
            prompt.append("This subgoal is part of a plan with the following subgoals in order (each with description):\n");
            for (int i = 0; i < planSubgoals.size(); i++) {
                Subgoal sg = planSubgoals.get(i);
                String marker = sg.getId().equals(subgoalId) ? " (CURRENT Q&A SUBGOAL)" : "";
                prompt.append("  ").append(i + 1).append(". ")
                        .append(sg.getName())
                        .append(marker)
                        .append("\n     Description: ")
                        .append(sg.getDescription() != null ? sg.getDescription() : "(none)")
                        .append("\n");
            }
            prompt.append("\n");

            if (currentIndex != -1) {
                if (currentIndex > 0) {
                    Subgoal prev = planSubgoals.get(currentIndex - 1);
                    prompt.append("The previous subgoal in this plan is: ")
                            .append(prev.getName())
                            .append(".\n");
                }
                if (currentIndex < planSubgoals.size() - 1) {
                    Subgoal next = planSubgoals.get(currentIndex + 1);
                    prompt.append("The next subgoal in this plan is: ")
                            .append(next.getName())
                            .append(".\n");
                }
                prompt.append("\n");
            }
        }

        if (!history.isEmpty()) {
            prompt.append("Previous conversation about this subgoal:\n");
            for (int i = startIndex; i < history.size(); i++) {
                SubgoalQuestionAnswer entry = history.get(i);
                prompt.append("Q: ").append(entry.getQuestionMessage()).append("\n");
                String resp = entry.getResponseMessage();
                if (resp != null && !resp.isEmpty()) {
                    prompt.append("A: ").append(resp).append("\n");
                }
            }
            prompt.append("\n");
        }

        prompt.append("Now the user asks this new question about the same subgoal:\n");
        prompt.append(question).append("\n");
        prompt.append("Please answer based on the subgoal context, the plan's subgoals, and the previous conversation if helpful.\n");

        // 5. Ask Gemini with the full prompt
        String answer = geminiGateway.getAnswerForQuestion(prompt.toString());
        if (answer == null || answer.trim().isEmpty()) {
            answer = "Sorry, I couldn't get an answer from Gemini.";
        }

        // 6. Save the new Q/A pair
        qnaDAO.appendEntry(subgoalId, question, answer);

        // 7. Reload updated history and present
        List<SubgoalQuestionAnswer> updatedHistory = qnaDAO.getHistory(subgoalId);
        SubgoalQnaOutputData out = new SubgoalQnaOutputData(subgoalId, updatedHistory);
        presenter.presentUpdate(out);
    }
}
