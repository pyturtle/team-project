package use_case.subgoal.qna;

/**
 * Output boundary for Subgoal Q/A.
 * We separate initial-open vs update so only the first call opens the dialog.
 */
public interface SubgoalQnaOutputBoundary {

    void presentInitial(SubgoalQnaOutputData outputData); // open dialog + show history

    void presentUpdate(SubgoalQnaOutputData outputData);  // just update history

    void presentError(String message);
}
