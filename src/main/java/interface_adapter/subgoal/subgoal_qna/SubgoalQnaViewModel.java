package interface_adapter.subgoal.subgoal_qna;

import interface_adapter.ViewModel;

public class SubgoalQnaViewModel extends ViewModel<SubgoalQnaState> {

    public static final String LOADING_LABEL = "Loading";
    public static final String SEND_BUTTON_LABEL = "Send";

    public SubgoalQnaViewModel() {
        super("");
        setState(new SubgoalQnaState());
    }
}
