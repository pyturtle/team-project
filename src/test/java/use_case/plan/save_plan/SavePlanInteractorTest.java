package use_case.plan.save_plan;

import data_access.file.FilePlanDataAccessObject;
import data_access.file.FileSubgoalDataAccessObject;
import data_access.interfaces.plan.SavePlanDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.plan.save_plan.SavePlanPresenter;
import interface_adapter.plan.save_plan.SavePlanViewModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SavePlanInteractorTest {

    @Test
    void successTest() {
        SavePlanViewModel savePlanViewModel = new SavePlanViewModel();
        DialogManagerModel dialogManagerModel = new DialogManagerModel();
        CalendarViewModel calendarViewModel = new CalendarViewModel();
        SavePlanOutputBoundary savePlanPresenter = new SavePlanOutputBoundary() {
            @Override
            public void prepareView(SavePlanOutputData outputData) {
                assertTrue(outputData.getSuccess());
            }
        };
        SavePlanDataAccessInterface savePlanDataAccessObject = new FilePlanDataAccessObject();
        SubgoalDataAccessInterface subgoalDataAccessObject = new FileSubgoalDataAccessObject();
        SavePlanInputData savePlanInputData = new SavePlanInputData(
                "Test",
                "Test",
                "Test",
                new ArrayList<>());
        SavePlanInputBoundary savePlanInteractor = new SavePlanInteractor(savePlanPresenter,
                savePlanDataAccessObject, subgoalDataAccessObject);
        savePlanInteractor.execute(savePlanInputData);
    }
}
