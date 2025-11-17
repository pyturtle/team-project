package use_case.plan.show_plan;

import entity.Subgoal;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowPlanOutputData {
    private String planName;
    private String planDescription;
    private ArrayList<Subgoal> subgoalList;

    public ShowPlanOutputData(String planName, String planDescription, ArrayList<Subgoal> subgoalList) {
        this.planName = planName;
        this.planDescription = planDescription;
        this.subgoalList = subgoalList;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public ArrayList<Subgoal> getSubgoalList() {
        return subgoalList;
    }
}
