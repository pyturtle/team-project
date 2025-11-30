package use_case.filter_subgoals;

import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;

import java.util.List;

public class FilterSubgoalsInteractor implements FilterSubgoalsInputBoundary {

    private final SubgoalDataAccessInterface subgoalDAO;  // Use this interface
    private final FilterSubgoalsOutputBoundary presenter;

    public FilterSubgoalsInteractor(SubgoalDataAccessInterface subgoalDAO,
                                    FilterSubgoalsOutputBoundary presenter) {
        this.subgoalDAO = subgoalDAO;
        this.presenter = presenter;
    }

    @Override
    public void filter(FilterSubgoalsInputData inputData) {
        List<Subgoal> result;

        if (inputData.isPriorityOnly()) {
            result = subgoalDAO.getPrioritySubgoals(inputData.getUserId());
        } else if (inputData.getPlanId() != null) {
            result = subgoalDAO.getSubgoalsByPlan(inputData.getPlanId(), inputData.getUserId());
        } else if (inputData.getSubgoalName() != null) {
            result = subgoalDAO.getSubgoalsByName(inputData.getSubgoalName(), inputData.getUserId());
        } else {
            result = subgoalDAO.getAllSubgoalsForUser(inputData.getUserId());
        }

        presenter.present(new FilterSubgoalsOutputData(result, inputData.getPlanId(),
                inputData.getSubgoalName(), inputData.isPriorityOnly()));
    }
}