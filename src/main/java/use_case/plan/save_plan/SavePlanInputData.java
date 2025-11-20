package use_case.plan.save_plan;

import entity.plan.Plan;
import use_case.plan.save_subgoal.SaveSubgoalInputData;

import java.util.ArrayList;

public class SavePlanInputData {
    private final String name;
    private final String description;
    private final String username;
    private ArrayList<SaveSubgoalInputData> subgoals;
    public SavePlanInputData(String name,
                             String description, String username,
                             ArrayList<SaveSubgoalInputData> subgoals) {
        this.name = name;
        this.description = description;
        this.username = username;
        this.subgoals = subgoals;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<SaveSubgoalInputData> getSubgoals() {
        return subgoals;
    }

    public String getUsername() {
        return username;
    }
}
