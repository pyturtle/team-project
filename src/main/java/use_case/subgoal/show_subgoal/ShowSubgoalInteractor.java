package use_case.subgoal.show_subgoal;

import entity.subgoal.Subgoal;

/**
 * Interactor for the ShowSubgoal use case.
 * Implements the input boundary and coordinates data access and presentation.
 */
public class ShowSubgoalInteractor implements ShowSubgoalInputBoundary {

    private final SubgoalDataAccessInterface subgoalDAO;
    private final ShowSubgoalOutputBoundary presenter;

    /**
     * Constructs a new ShowSubgoalInteractor.
     *
     * @param subgoalDAO the data access object for subgoals
     * @param presenter the output boundary to present results to
     */
    public ShowSubgoalInteractor(SubgoalDataAccessInterface subgoalDAO,
                                 ShowSubgoalOutputBoundary presenter) {
        this.subgoalDAO = subgoalDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(ShowSubgoalInputData inputData) {
        String id = inputData.getSubgoalId();
        Subgoal s = subgoalDAO.getSubgoalById(id);

        if (s == null) {
            presenter.presentError("Subgoal with id " + id + " was not found.");
            return;
        }

        ShowSubgoalOutputData out = new ShowSubgoalOutputData(
                id,
                s.getName(),
                s.getDescription(),
                s.isPriority(),
                s.isCompleted()
        );

        presenter.present(out);
    }

    @Override
    public void setPriority(SetPriorityInputData inputData) {
        String id = inputData.getSubgoalId();
        boolean priority = inputData.isPriority();

        subgoalDAO.updatePriority(id, priority);

        Subgoal s = subgoalDAO.getSubgoalById(id);
        if (s == null) {
            presenter.presentError("Subgoal with id " + id + " was not found after updating priority.");
        } else {
            // Present the updated subgoal to trigger view refresh
            ShowSubgoalOutputData out = new ShowSubgoalOutputData(
                    s.getId(),
                    s.getName(),
                    s.getDescription(),
                    s.isPriority(),
                    s.isCompleted()
            );
            // Use presentUpdate to avoid opening a new dialog
            if (presenter instanceof interface_adapter.show_subgoal.ShowSubgoalPresenter) {
                ((interface_adapter.show_subgoal.ShowSubgoalPresenter) presenter).presentUpdate(out);
            }
        }
    }

    @Override
    public void setCompleted(SetCompletedInputData inputData) {
        String id = inputData.getSubgoalId();
        boolean completed = inputData.isCompleted();

        subgoalDAO.updateCompleted(id, completed);

        Subgoal s = subgoalDAO.getSubgoalById(id);
        if (s == null) {
            presenter.presentError("Subgoal with id " + id + " was not found after updating completion.");
        } else {
            // Present the updated subgoal to trigger view refresh
            ShowSubgoalOutputData out = new ShowSubgoalOutputData(
                    s.getId(),
                    s.getName(),
                    s.getDescription(),
                    s.isPriority(),
                    s.isCompleted()
            );
            // Use presentUpdate to avoid opening a new dialog
            if (presenter instanceof interface_adapter.show_subgoal.ShowSubgoalPresenter) {
                ((interface_adapter.show_subgoal.ShowSubgoalPresenter) presenter).presentUpdate(out);
            }
        }
    }
}
