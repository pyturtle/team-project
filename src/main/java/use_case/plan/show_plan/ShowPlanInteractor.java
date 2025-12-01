package use_case.plan.show_plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import data_access.interfaces.plan.ShowPlanDataAccessInterface;

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
     *
     * @param showPlanPresenter  the presenter that will prepare the view
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
     *
     * @param showPlanInputData the input data containing the plan JSON object
     */
    @Override
    public void execute(ShowPlanInputData showPlanInputData) {
        final JSONObject planObject = showPlanInputData.getPlanObject();

        final String nameKey = "name";
        final String descriptionKey = "description";
        final String subgoalsKey = "subgoals";
        final String deadlineKey = "deadline";

        final String planName = planObject.getString(nameKey);
        final String planDescription = planObject.getString(descriptionKey);

        final ArrayList<Map<String, String>> subgoalsList = new ArrayList<>();
        final JSONArray subgoals = planObject.getJSONArray(subgoalsKey);

        for (int i = 0; i < subgoals.length(); i++) {
            final Map<String, String> subgoal = new HashMap<>();
            subgoal.put(nameKey,
                    subgoals.getJSONObject(i).getString(nameKey));
            subgoal.put(descriptionKey,
                    subgoals.getJSONObject(i).getString(descriptionKey));
            subgoal.put(deadlineKey,
                    subgoals.getJSONObject(i).getString(deadlineKey));
            subgoalsList.add(subgoal);
        }

        final boolean planExists = showPlanDataAccess.planExists(planName);

        final ShowPlanOutputData showPlanOutputData =
                new ShowPlanOutputData(planName, planDescription, subgoalsList, planExists);

        showPlanPresenter.prepareView(showPlanOutputData);
    }
}
