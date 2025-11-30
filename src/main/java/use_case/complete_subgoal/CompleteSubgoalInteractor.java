package use_case.complete_subgoal;

import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;

public class CompleteSubgoalInteractor implements CompleteSubgoalInputBoundary {

    private final SubgoalDataAccessInterface subgoalDAO;
    private final CompleteSubgoalOutputBoundary presenter;

    public CompleteSubgoalInteractor(SubgoalDataAccessInterface subgoalDAO,
                                     CompleteSubgoalOutputBoundary presenter) {
        this.subgoalDAO = subgoalDAO;
        this.presenter = presenter;
    }

    @Override
    public void complete(CompleteSubgoalInputData inputData) {


        Subgoal s = subgoalDAO.getSubgoalById(String.valueOf(inputData.getSubgoalId()));

        if (s == null) {
            presenter.presentFailure(
                    "Subgoal with ID " + inputData.getSubgoalId() + " does not exist.");
            return;
        }


        Subgoal updated = new Subgoal(
                s.getId(),
                s.getPlanId(),
                s.getUsername(),
                s.getName(),
                s.getDescription(),
                s.getDeadline(),
                true,
                s.isPriority()
        );


        subgoalDAO.saveUpdatedSubgoal(updated);


        presenter.present(new CompleteSubgoalOutputData(updated.getId()));
    }
}