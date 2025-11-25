package interface_adapter.subgoal_qa;

import interface_adapter.ViewModel;

public class SubgoalQAViewModel extends ViewModel<SubgoalQAState> {

    public static final String TITLE = "Subgoal Q/A Chat";

    public SubgoalQAViewModel() {
        super(TITLE);
        setState(new SubgoalQAState());
    }
}
