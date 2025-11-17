package use_case.plan.show_plan;

import entity.Subgoal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class ShowPlanInteractor implements ShowPlanInputBoundary {
    ShowPlanOutputBoundary showPlanPresenter;

    public ShowPlanInteractor(ShowPlanOutputBoundary showPlanPresenter) {
        this.showPlanPresenter = showPlanPresenter;
    }

    @Override
    public void execute(ShowPlanInputData showPlanInputData) {
        JSONObject planObject = showPlanInputData.getPlanObject();
        String planName = planObject.getString("name");
        String planDescription = planObject.getString("description");
        ArrayList<Subgoal> subgoalsList = new ArrayList<>();
        JSONArray subgoals = planObject.getJSONArray("subgoals");
        for (int i = 0; i < subgoals.length(); i++) {
            Subgoal subgoal = new Subgoal(
                    subgoals.getJSONObject(i).getString("id"),
                    planObject.getString("id"),
                    planObject.getString("user_email"),
                    subgoals.getJSONObject(i).getString("name"),
                    subgoals.getJSONObject(i).getString("description"),
                    LocalDate.parse(subgoals.getJSONObject(i).getString("deadline")),
                    false,
                    false);
            subgoalsList.add(subgoal);
        }
        final ShowPlanOutputData showPlanOutputData =
                new ShowPlanOutputData(planName, planDescription, subgoalsList);
        showPlanPresenter.prepareView(showPlanOutputData);
    }
}
