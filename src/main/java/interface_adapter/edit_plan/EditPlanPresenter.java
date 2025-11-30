package interface_adapter.edit_plan;

import entity.plan.Plan;
import interface_adapter.plan.show_plans.ShowPlansState;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import use_case.edit_plan.EditPlanOutputBoundary;
import use_case.edit_plan.EditPlanOutputData;

import java.util.List;
import java.util.stream.Collectors;

public class EditPlanPresenter implements EditPlanOutputBoundary {

    private final ShowPlansViewModel showPlansViewModel;

    public EditPlanPresenter(ShowPlansViewModel showPlansViewModel) {
        this.showPlansViewModel = showPlansViewModel;
    }

    @Override
    public void prepareSuccessView(EditPlanOutputData data) {
        ShowPlansState state = showPlansViewModel.getState();
        List<Plan> updatedPlans = state.getPlans().stream()
                .map(p -> {
                    if (p.getId().equals(data.getPlanId())) {
                        return new Plan(
                                p.getId(),
                                data.getName(),
                                data.getDescription(),
                                p.getUsername()
                        );
                    }
                    return p;
                })
                .collect(Collectors.toList());

        state.setPlans(updatedPlans);
        state.setError(null);

        showPlansViewModel.setState(state);
        showPlansViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ShowPlansState state = showPlansViewModel.getState();
        state.setError(errorMessage);
        showPlansViewModel.setState(state);
        showPlansViewModel.firePropertyChange();
    }
}
