package use_case.filter_subgoals;

import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;  // Use this import
import entity.subgoal.Subgoal;
import java.util.List;

public class FilterSubgoalsInteractor implements FilterSubgoalsInputBoundary {

    private final use_case.subgoal.show_subgoal.SubgoalDataAccessInterface subgoalDAO;  // Use the correct interface
    private final FilterSubgoalsOutputBoundary presenter;

    public FilterSubgoalsInteractor(use_case.subgoal.show_subgoal.SubgoalDataAccessInterface subgoalDAO,
                                    FilterSubgoalsOutputBoundary presenter) {
        this.subgoalDAO = subgoalDAO;
        this.presenter = presenter;
    }

    @Override
    public void filter(FilterSubgoalsInputData inputData) {
        List<Subgoal> result;

        if (inputData.isPriorityOnly()) {
            result = subgoalDAO.getPrioritySubgoals(inputData.getUserId());
        }
        else if (inputData.getPlanId() != null) {
            result = subgoalDAO.getSubgoalsByPlan(inputData.getPlanId(), inputData.getUserId());
        }
        else {
            result = subgoalDAO.getAllSubgoalsForUser(inputData.getUserId());
        }

        presenter.present(new FilterSubgoalsOutputData(result));
    }
}