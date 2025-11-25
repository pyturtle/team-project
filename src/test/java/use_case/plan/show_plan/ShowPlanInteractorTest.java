package use_case.plan.show_plan;

import data_access.FilePlanDataAccessObject;
import interface_adapter.plan.show_plan.ShowPlanPresenter;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ShowPlanInteractorTest {

    @Test
    void successTest() {
        String testString = "{\"name\": \"Test\", \"description\": \"Test\", \"subgoals\": []}";
        JSONObject testData = new JSONObject(testString);
        ShowPlanDataAccessInterface showPlanDataAccessObject = new FilePlanDataAccessObject();
        ShowPlanInputData inputData = new ShowPlanInputData(testData);

        ShowPlanOutputBoundary showPlanPresenter = new ShowPlanPresenter(null, null) {
            @Override
            public void prepareView(ShowPlanOutputData showPlanOutputData)
            {
                assertEquals("Test", showPlanOutputData.getPlanName());
                assertEquals("Test", showPlanOutputData.getPlanDescription());
                assertEquals(new ArrayList<>(), showPlanOutputData.getSubgoalList());
            }
        };
        ShowPlanInputBoundary showPlanInteractor = new ShowPlanInteractor(showPlanPresenter,
                showPlanDataAccessObject);
        showPlanInteractor.execute(inputData);
    }
}
