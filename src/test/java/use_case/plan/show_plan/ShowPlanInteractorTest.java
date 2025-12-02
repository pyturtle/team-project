package use_case.plan.show_plan;

import data_access.file.FilePlanDataAccessObject;
import data_access.interfaces.plan.ShowPlanDataAccessInterface;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShowPlanInteractorTest {

    @Test
    void successTest() {
        ShowPlanDataAccessInterface showPlanDataAccessObject = new FilePlanDataAccessObject();
        ShowPlanOutputBoundary showPlanPresenter = new ShowPlanOutputBoundary() {
            @Override
            public void prepareView(ShowPlanOutputData showPlanOutputData) {
                assertEquals("Test", showPlanOutputData.getPlanName());
                assertEquals("Test", showPlanOutputData.getPlanDescription());
                assertEquals(0, showPlanOutputData.getSubgoalList().size());
                assertFalse(showPlanOutputData.isCreated());
            }
        };
        JSONObject jsonObject = new JSONObject("{\"name\": \"Test\", \"description\": \"Test\"," +
                " \"subgoals\": []}");
        ShowPlanInputData showPlanInputData = new ShowPlanInputData(jsonObject);
        ShowPlanInputBoundary showPlanInteractor = new ShowPlanInteractor(showPlanPresenter,
                showPlanDataAccessObject);
        showPlanInteractor.execute(showPlanInputData);

    }
}
