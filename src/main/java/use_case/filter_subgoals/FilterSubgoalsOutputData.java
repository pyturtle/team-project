package use_case.filter_subgoals;

import entity.Subgoal;
import java.util.List;

public class FilterSubgoalsOutputData {

    private final List<Subgoal> filteredSubgoals;

    public FilterSubgoalsOutputData(List<Subgoal> filteredSubgoals) {
        this.filteredSubgoals = filteredSubgoals;
    }

    public List<Subgoal> getFilteredSubgoals() {
        return filteredSubgoals;
    }
}
