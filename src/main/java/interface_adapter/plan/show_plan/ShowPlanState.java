package interface_adapter.plan.show_plan;

import entity.subgoal.Subgoal;
import interface_adapter.show_subgoal.ShowSubgoalState;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowPlanState {
    private String planName;
    private String planDescription;
    private ArrayList<HashMap<String, String>> subgoalList;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public ArrayList<HashMap<String, String>> getSubgoalList() {
        return subgoalList;
    }

    public void setSubgoalList(ArrayList<HashMap<String, String>> subgoalList) {
        this.subgoalList = subgoalList;
    }
}
