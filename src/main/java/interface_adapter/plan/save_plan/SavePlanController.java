package interface_adapter.plan.save_plan;

import use_case.plan.save_plan.SavePlanInputBoundary;
import use_case.plan.save_plan.SavePlanInputData;
import use_case.subgoal.save_subgoal.SaveSubgoalInputData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SavePlanController collects plan and subgoal data from the UI,
 * converts it into input data objects, and passes them to the save plan interactor.
 */
public class SavePlanController {

    private final SavePlanInputBoundary savePlanInteractor;

    /**
     * Creates a new SavePlanController with the provided interactor.
     * @param savePlanInteractor the interactor responsible for saving plans
     */
    public SavePlanController(SavePlanInputBoundary savePlanInteractor) {
        this.savePlanInteractor = savePlanInteractor;
    }

    /**
     * Executes the plan-saving use case by converting raw user input into structured input data.
     * Each subgoal is mapped to a SaveSubgoalInputData object, and the whole plan is wrapped
     * in a SavePlanInputData object before being passed to the interactor.
     * @param name the name of the plan
     * @param description the description of the plan
     * @param username the username of the plan owner
     * @param subgoals a list of maps representing subgoals, each containing name, description, and deadline
     */
    public void execute(String name,
                        String description,
                        String username,
                        List<HashMap<String, String>> subgoals) {
        final ArrayList<SaveSubgoalInputData> saveSubgoalInputDataList = new ArrayList<>();
        for (HashMap<String, String> subgoal : subgoals) {
            final SaveSubgoalInputData saveSubgoalInputData = new SaveSubgoalInputData(
                    subgoal.get("name"),
                    subgoal.get("description"),
                    LocalDate.parse(subgoal.get("deadline"))
            );
            saveSubgoalInputDataList.add(saveSubgoalInputData);
        }
        final SavePlanInputData savePlanInputData = new SavePlanInputData(name,
                description, username, saveSubgoalInputDataList);
        savePlanInteractor.execute(savePlanInputData);
    }
}
