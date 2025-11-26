package use_case.subgoal.qna;

/**
 * Input boundary for the Subgoal Q/A use case.
 */
public interface SubgoalQnaInputBoundary {

    /**
     * Open the Q/A chat for a specific subgoal and load its history.
     */
    void open(SubgoalQnaOpenInputData inputData);

    /**
     * Ask a new question about a specific subgoal.
     */
    void ask(SubgoalQnaAskInputData inputData);
}
