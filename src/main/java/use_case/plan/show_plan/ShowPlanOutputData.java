package use_case.plan.show_plan;

import entity.subgoal.Subgoal;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowPlanOutputData {
    private String planName;
    private String planDescription;
    private ArrayList<HashMap<String, String>> subgoalList;
    private boolean isCreated;

    public ShowPlanOutputData(String planName, String planDescription,
                              ArrayList<HashMap<String, String>> subgoalList, boolean isCreated) {
        this.planName = planName;
        this.planDescription = planDescription;
        this.subgoalList = subgoalList;
        this.isCreated = isCreated;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public ArrayList<HashMap<String, String>> getSubgoalList() {
        return subgoalList;
    }

    public boolean isCreated() {
        return isCreated;
    }
}
