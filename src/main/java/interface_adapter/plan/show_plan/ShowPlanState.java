package interface_adapter.plan.show_plan;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ShowPlanState stores the details of a selected plan, including its name,
 * description, subgoals, owner, and a flag indicating whether the plan exists.
 */
public class ShowPlanState {
    private String planName;
    private String planDescription;
    private ArrayList<HashMap<String, String>> subgoalList;
    private String username;
    private boolean planExists;

    /**
     * Returns the name of the selected plan.
     * @return the plan name
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * Sets the name of the selected plan.
     * @param planName the plan name to store
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * Returns the description of the selected plan.
     * @return the plan description
     */
    public String getPlanDescription() {
        return planDescription;
    }

    /**
     * Sets the description of the selected plan.
     * @param planDescription the plan description to store
     */
    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    /**
     * Returns the list of subgoals associated with the plan.
     * Each subgoal is represented as a map of its fields.
     * @return a list of subgoal maps
     */
    public ArrayList<HashMap<String, String>> getSubgoalList() {
        return subgoalList;
    }

    /**
     * Sets the list of subgoals associated with the plan.
     * @param subgoalList the list of subgoals to store
     */
    public void setSubgoalList(ArrayList<HashMap<String, String>> subgoalList) {
        this.subgoalList = subgoalList;
    }

    /**
     * Returns whether the selected plan exists.
     * @return true if the plan exists, false otherwise
     */
    public boolean isPlanExists() {
        return planExists;
    }

    /**
     * Sets whether the selected plan exists.
     * @param planExists true if the plan exists, false otherwise
     */
    public void setPlanExists(boolean planExists) {
        this.planExists = planExists;
    }

    /**
     * Returns the username of the plan owner.
     * @return the plan owner's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the plan owner.
     * @param username the username to store
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
