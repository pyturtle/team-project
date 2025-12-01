package use_case.plan.save_plan;

import java.util.ArrayList;

import use_case.subgoal.save_subgoal.SaveSubgoalInputData;

/**
 * SavePlanInputData represents the data required to save a plan,
 * including its name, description, owner, and associated subgoals.
 */
public class SavePlanInputData {

    private final String name;
    private final String description;
    private final String username;
    private final ArrayList<SaveSubgoalInputData> subgoals;

    /**
     * Creates a new SavePlanInputData instance with all required fields.
     *
     * @param name        the name of the plan
     * @param description the description of the plan
     * @param username    the username of the plan owner
     * @param subgoals    the list of subgoals to save along with the plan
     */
    public SavePlanInputData(String name,
                             String description,
                             String username,
                             ArrayList<SaveSubgoalInputData> subgoals) {
        this.name = name;
        this.description = description;
        this.username = username;
        this.subgoals = subgoals;
    }

    /**
     * Returns the name of the plan.
     *
     * @return the plan name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the plan.
     *
     * @return the plan description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the list of subgoals associated with the plan.
     *
     * @return a list of SaveSubgoalInputData objects
     */
    public ArrayList<SaveSubgoalInputData> getSubgoals() {
        return subgoals;
    }

    /**
     * Returns the username of the plan owner.
     *
     * @return the owner's username
     */
    public String getUsername() {
        return username;
    }
}
