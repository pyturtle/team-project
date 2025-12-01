package use_case.subgoal.qna;

/**
 * Output boundary for Subgoal Q/A.
 * We separate initial-open vs update so only the first call opens the dialog.
 */
public interface SubgoalQnaOutputBoundary {

    void presentInitial(SubgoalQnaOutputData outputData);

    void presentUpdate(SubgoalQnaOutputData outputData);

    void presentError(String message);
}
