package interface_adapter.plan.generate_plan;

import interface_adapter.ViewModel;

public class GeneratePlanViewModel extends ViewModel<GeneratePlanState> {
    public static final String TITLE_LABEL = "Generate Plan";
    public static final String SEND_BUTTON_LABEL = "Send";


    public GeneratePlanViewModel() {
        super("generate plan");
        setState(new GeneratePlanState());
    }
}
