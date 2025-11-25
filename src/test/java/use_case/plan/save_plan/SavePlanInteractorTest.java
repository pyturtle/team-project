package use_case.plan.save_plan;

import data_access.FilePlanDataAccessObject;
import data_access.FileSubgoalDataAccessObject;
import interface_adapter.DialogManagerModel;
import interface_adapter.plan.save_plan.SavePlanPresenter;
import interface_adapter.plan.save_plan.SavePlanViewModel;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import use_case.subgoal.save_subgoal.SaveSubgoalInputData;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SavePlanInteractorTest {
    @Test
    public void successTest() {
        SavePlanInputData savePlanInputData = new SavePlanInputData("Test",
                "Test",
                "Test",
                new ArrayList<>());
        SavePlanDataAccessInterface savePlanDataAccessObject = new FilePlanDataAccessObject();
        SubgoalDataAccessInterface subgoalDataAccessInterface = new FileSubgoalDataAccessObject();
        SavePlanOutputBoundary savePlanPresenter = new SavePlanPresenter(new SavePlanViewModel(),
                new DialogManagerModel()) {
            @Override
            public void prepareView(SavePlanOutputData savePlanOutputData) {
                assertTrue(savePlanOutputData.getSuccess());
            }
        };
        SavePlanInteractor savePlanInteractor = new SavePlanInteractor(savePlanPresenter,
                savePlanDataAccessObject,
                subgoalDataAccessInterface);
        savePlanInteractor.execute(savePlanInputData);
    }
}
