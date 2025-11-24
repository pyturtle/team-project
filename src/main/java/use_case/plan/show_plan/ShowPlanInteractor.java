package use_case.plan.show_plan;

import entity.subgoal.Subgoal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowPlanInteractor implements ShowPlanInputBoundary {
    ShowPlanOutputBoundary showPlanPresenter;
    ShowPlanDataAccessInterface showPlanDataAccess;

    public ShowPlanInteractor(ShowPlanOutputBoundary showPlanPresenter,
                              ShowPlanDataAccessInterface showPlanDataAccess) {
        this.showPlanPresenter = showPlanPresenter;
        this.showPlanDataAccess = showPlanDataAccess;
    }

    @Override
    public void execute(ShowPlanInputData showPlanInputData) {
        JSONObject planObject = showPlanInputData.getPlanObject();
        try {
            String planName = planObject.getString("name");
            String planDescription = planObject.getString("description");
            ArrayList<HashMap<String, String>> subgoalsList = new ArrayList<>();
            JSONArray subgoals = planObject.getJSONArray("subgoals");
            for (int i = 0; i < subgoals.length(); i++) {
                HashMap<String, String> subgoal = new HashMap<>();
                subgoal.put("name", subgoals.getJSONObject(i).getString("name"));
                subgoal.put("description", subgoals.getJSONObject(i).getString("description"));
                subgoal.put("deadline", subgoals.getJSONObject(i).getString("deadline"));
                subgoalsList.add(subgoal);
            }
            boolean planExists = showPlanDataAccess.planExists(planName);
            final ShowPlanOutputData showPlanOutputData =
                    new ShowPlanOutputData(planName, planDescription, subgoalsList, planExists);
            showPlanPresenter.prepareSuccessView(showPlanOutputData);
        }
        catch (Exception e) {
            showPlanPresenter.prepareFailureView();
        }
    }
}
