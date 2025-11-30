package use_case.plan.show_plan;

import data_access.interfaces.plan.ShowPlanDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ShowPlanInteractor implements the show plan use case.
 * It extracts plan details from the input data, transforms subgoals into
 * domain-friendly structures, checks whether the plan already exists,
 * and forwards the results to the presenter.
 */
public class ShowPlanInteractor implements ShowPlanInputBoundary {
    private final ShowPlanOutputBoundary showPlanPresenter;
    private final ShowPlanDataAccessInterface showPlanDataAccess;

    /**
     * Creates a new ShowPlanInteractor with the given presenter and data access object.
     * @param showPlanPresenter the presenter that will prepare the view
     * @param showPlanDataAccess the data access interface used to verify plan existence
     */
    public ShowPlanInteractor(ShowPlanOutputBoundary showPlanPresenter,
                              ShowPlanDataAccessInterface showPlanDataAccess) {
        this.showPlanPresenter = showPlanPresenter;
        this.showPlanDataAccess = showPlanDataAccess;
    }

    /**
     * Executes the show plan use case by extracting plan fields from the given input,
     * building a list of subgoals, checking whether the plan already exists in storage,
     * and passing the results to the presenter.
     * @param showPlanInputData the input data containing the plan JSON object
     */
    @Override
    public void execute(ShowPlanInputData showPlanInputData) {
        JSONObject planObject = showPlanInputData.getPlanObject();

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

        showPlanPresenter.prepareView(showPlanOutputData);
    }
}
