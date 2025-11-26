package data_access;

import entity.SubgoalQuestionAnswer;
import use_case.subgoal.qna.SubgoalQnaDataAccessInterface;

import java.util.*;

/**
 * Simple in-memory storage for Subgoal Q/A history.
 * No file I/O – history resets when the program restarts.
 */
public class InMemorySubgoalQnaDataAccessObject implements SubgoalQnaDataAccessInterface {

    private final Map<String, List<SubgoalQuestionAnswer>> historyBySubgoal = new HashMap<>();

    @Override
    public synchronized List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
        // Return a defensive copy so callers can't mutate our internal list.
        return new ArrayList<>(historyBySubgoal.getOrDefault(subgoalId, Collections.emptyList()));
    }

    @Override
    public synchronized SubgoalQuestionAnswer appendEntry(String subgoalId,
                                                          String questionMessage,
                                                          String responseMessage) {
        List<SubgoalQuestionAnswer> history =
                historyBySubgoal.computeIfAbsent(subgoalId, k -> new ArrayList<>());

        // Generate a unique String id for this Q/A entry.
        String entryId = UUID.randomUUID().toString();

        SubgoalQuestionAnswer entry =
                new SubgoalQuestionAnswer(entryId, subgoalId, questionMessage, responseMessage);

        history.add(entry);
        return entry;
    }
}
