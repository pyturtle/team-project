package use_case.plan.show_plan;

import java.util.ArrayList;
import java.util.Map;

/**
 * ShowPlanOutputData contains the processed data required to display a plan,
 * including its name, description, subgoals, and a flag indicating whether
 * the plan already exists in persistent storage.
 */
public class ShowPlanOutputData {
    private final String planName;
    private final String planDescription;
    private final ArrayList<Map<String, String>> subgoalList;
    private final boolean isCreated;

    /**
     * Creates a new ShowPlanOutputData instance with all required fields.
     * @param planName the name of the plan
     * @param planDescription the description of the plan
     * @param subgoalList the list of subgoals associated with the plan
     * @param isCreated whether the plan already exists in storage
     */
    public ShowPlanOutputData(String planName, String planDescription,
                              ArrayList<Map<String, String>> subgoalList, boolean isCreated) {
        this.planName = planName;
        this.planDescription = planDescription;
        this.subgoalList = subgoalList;
        this.isCreated = isCreated;
    }

    /**
     * Returns the name of the plan.
     * @return the plan name
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * Returns the description of the plan.
     * @return the plan description
     */
    public String getPlanDescription() {
        return planDescription;
    }

    /**
     * Returns the list of subgoals associated with the plan.
     * Each subgoal is represented as a map of its attributes.
     * @return the subgoal list
     */
    public ArrayList<Map<String, String>> getSubgoalList() {
        return subgoalList;
    }

    /**
     * Returns whether the plan already exists.
     * @return true if the plan exists, false otherwise
     */
    public boolean isCreated() {
        return isCreated;
    }
}
