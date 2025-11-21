package interface_adapter.delete_plan;

import interface_adapter.show_plans.ShowPlansViewModel;
import use_case.delete_plan.DeletePlanOutputBoundary;
import use_case.delete_plan.DeletePlanOutputData;

/**
 * The Presenter for the Delete Plan Use Case.
 */
public class DeletePlanPresenter implements DeletePlanOutputBoundary {

    private final ShowPlansViewModel showPlansViewModel;

    /**
     * Creates a new Delete Plan Presenter.
     * @param showPlansViewModel the show plans view model
     */
    public DeletePlanPresenter(ShowPlansViewModel showPlansViewModel) {
        this.showPlansViewModel = showPlansViewModel;
    }

    @Override
    public void prepareSuccessView(DeletePlanOutputData outputData) {
        // Trigger a refresh of the Show Plans view
        showPlansViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Trigger error notification (could display via state if needed)
        showPlansViewModel.firePropertyChange();
    }
}

