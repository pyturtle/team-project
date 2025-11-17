package interface_adapter.plan.show_plan;

import entity.Subgoal;

import java.util.ArrayList;

public class ShowPlanState {
    private String planName;
    private String planDescription;
    private ArrayList<Subgoal> subgoalList;

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

    public ArrayList<Subgoal> getSubgoalList() {
        return subgoalList;
    }

    public void setSubgoalList(ArrayList<Subgoal> subgoalList) {
        this.subgoalList = subgoalList;
    }
}
