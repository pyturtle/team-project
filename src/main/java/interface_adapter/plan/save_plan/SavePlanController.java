package interface_adapter.plan.save_plan;

import use_case.plan.save_plan.SavePlanInputBoundary;
import use_case.plan.save_plan.SavePlanInputData;
import use_case.subgoal.save_subgoal.SaveSubgoalInputData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SavePlanController {

    private final SavePlanInputBoundary savePlanInteractor;

    public SavePlanController(SavePlanInputBoundary savePlanInteractor) {
        this.savePlanInteractor = savePlanInteractor;
    }

    public void execute(String name,
                        String description,
                        String username,
                        List<HashMap<String, String>> subgoals) {
        final ArrayList<SaveSubgoalInputData> saveSubgoalInputDataList = new ArrayList<>();
        for  (HashMap<String,String> subgoal : subgoals) {
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
