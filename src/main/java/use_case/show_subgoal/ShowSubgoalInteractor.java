package use_case.show_subgoal;

import entity.Subgoal;

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

    /**
     * Loads a subgoal by ID and passes its information to the Presenter.
     *
     * @param inputData the input data containing the subgoal ID
     */
    @Override
    public void execute(ShowSubgoalInputData inputData) {
        int id = inputData.getSubgoalId();
        Subgoal s = subgoalDAO.getSubgoalById(id);

        if (s == null) {
            presenter.presentError("Subgoal with id " + id + " was not found.");
            return;
        }

        ShowSubgoalOutputData out = new ShowSubgoalOutputData(
                s.getName(),
                s.getDescription(),
                s.isPriority()
        );

        presenter.present(out);
    }

    /**
     * Updates the priority flag of a subgoal and refreshes its displayed data.
     *
     * @param inputData the input data containing the subgoal ID and new priority value
     */
    @Override
    public void setPriority(SetPriorityInputData inputData) {
        int id = inputData.getSubgoalId();
        boolean priority = inputData.isPriority();

        subgoalDAO.updatePriority(id, priority);

        // Reload the subgoal to keep UI in sync
        Subgoal s = subgoalDAO.getSubgoalById(id);
        if (s == null) {
            presenter.presentError("Subgoal with id " + id + " was not found after updating priority.");
            return;
        }

        ShowSubgoalOutputData out = new ShowSubgoalOutputData(
                s.getName(),
                s.getDescription(),
                s.isPriority()
        );

        presenter.present(out);
    }
}
