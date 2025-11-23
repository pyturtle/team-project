package use_case.complete_subgoal;

import data_access.SubgoalDataAccessInterface;
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

        // 1. Retrieve the existing subgoal - FIXED: Use String parameter
        Subgoal s = subgoalDAO.getSubgoalById(String.valueOf(inputData.getSubgoalId()));

        if (s == null) {
            presenter.presentFailure(
                    "Subgoal with ID " + inputData.getSubgoalId() + " does not exist.");
            return;
        }

        // 2. Create a new updated Subgoal (immutable entity -> must reconstruct)
        Subgoal updated = new Subgoal(
                s.getId(),
                s.getPlanId(),
                s.getUsername(),  // FIXED: Use getUsername() not getUserId()
                s.getName(),
                s.getDescription(),
                s.getDeadline(),
                true,            // <- SET COMPLETED = TRUE
                s.isPriority()
        );

        // 3. Save the updated subgoal into DAO
        subgoalDAO.saveUpdatedSubgoal(updated);

        // 4. Return successful output - FIXED: Pass String ID
        presenter.present(new CompleteSubgoalOutputData(updated.getId()));
    }
}