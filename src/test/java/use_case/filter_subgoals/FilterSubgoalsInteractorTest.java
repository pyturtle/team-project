package use_case.filter_subgoals;

import data_access.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterSubgoalsInteractorTest {

    private TestSubgoalDataAccess subgoalDAO;
    private TestPresenter presenter;
    private FilterSubgoalsInteractor interactor;

    private static final String USER_ID = "user123";
    private static final String PLAN_ID = "plan456";
    private static final String SUBGOAL_NAME = "Test Subgoal";

    @BeforeEach
    void setUp() {
        subgoalDAO = new TestSubgoalDataAccess();
        presenter = new TestPresenter();
        interactor = new FilterSubgoalsInteractor(subgoalDAO, presenter);
    }

    @Test
    void testFilterByPriorityOnly() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, null, null, true
        );

        Subgoal mockSubgoal1 = createMockSubgoal("subgoal1", "Priority Subgoal");
        Subgoal mockSubgoal2 = createMockSubgoal("subgoal2", "Another Priority");
        subgoalDAO.setPrioritySubgoals(Arrays.asList(mockSubgoal1, mockSubgoal2));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getPrioritySubgoals", subgoalDAO.lastCalledMethod);
        assertEquals(USER_ID, subgoalDAO.lastUserId);
        assertEquals(2, presenter.lastOutputData.getFilteredSubgoals().size());
        assertTrue(presenter.lastOutputData.getPriorityOnly());
    }

    @Test
    void testFilterByPlanId() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, PLAN_ID, null, false
        );

        Subgoal mockSubgoal = createMockSubgoal("subgoal1", "Plan Subgoal");
        subgoalDAO.setSubgoalsByPlanId(Arrays.asList(mockSubgoal));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getSubgoalsByPlanId", subgoalDAO.lastCalledMethod);
        assertEquals(PLAN_ID, subgoalDAO.lastPlanId);
        assertEquals(1, presenter.lastOutputData.getFilteredSubgoals().size());
        assertEquals(PLAN_ID, presenter.lastOutputData.getPlanId());
        assertFalse(presenter.lastOutputData.getPriorityOnly());
    }

    @Test
    void testFilterBySubgoalName() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, null, SUBGOAL_NAME, false
        );

        Subgoal mockSubgoal = createMockSubgoal("subgoal1", SUBGOAL_NAME);
        subgoalDAO.setSubgoalsByName(Arrays.asList(mockSubgoal));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getSubgoalsByName", subgoalDAO.lastCalledMethod);
        assertEquals(SUBGOAL_NAME, subgoalDAO.lastSubgoalName);
        assertEquals(USER_ID, subgoalDAO.lastUserId);
        assertEquals(1, presenter.lastOutputData.getFilteredSubgoals().size());
        assertEquals(SUBGOAL_NAME, presenter.lastOutputData.getSubgoalName());
        assertFalse(presenter.lastOutputData.getPriorityOnly());
    }

    @Test
    void testFilterAllSubgoals() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, null, null, false
        );

        Subgoal mockSubgoal1 = createMockSubgoal("subgoal1", "Subgoal 1");
        Subgoal mockSubgoal2 = createMockSubgoal("subgoal2", "Subgoal 2");
        subgoalDAO.setAllSubgoals(Arrays.asList(mockSubgoal1, mockSubgoal2));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getAllSubgoalsForUser", subgoalDAO.lastCalledMethod);
        assertEquals(USER_ID, subgoalDAO.lastUserId);
        assertEquals(2, presenter.lastOutputData.getFilteredSubgoals().size());
        assertNull(presenter.lastOutputData.getPlanId());
        assertNull(presenter.lastOutputData.getSubgoalName());
        assertFalse(presenter.lastOutputData.getPriorityOnly());
    }

    @Test
    void testPriorityTakesPrecedenceOverPlanId() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, PLAN_ID, null, true
        );

        Subgoal mockSubgoal = createMockSubgoal("subgoal1", "Priority Subgoal");
        subgoalDAO.setPrioritySubgoals(Arrays.asList(mockSubgoal));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getPrioritySubgoals", subgoalDAO.lastCalledMethod);
        // Verify plan-based method was NOT called
        assertNotEquals("getSubgoalsByPlanId", subgoalDAO.lastCalledMethod);
        assertTrue(presenter.lastOutputData.getPriorityOnly());
    }

    @Test
    void testEmptyResults() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, null, null, true
        );
        subgoalDAO.setPrioritySubgoals(Arrays.asList());

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getPrioritySubgoals", subgoalDAO.lastCalledMethod);
        assertTrue(presenter.lastOutputData.getFilteredSubgoals().isEmpty());
        assertTrue(presenter.lastOutputData.getPriorityOnly());
    }

    // Helper method to create subgoals with the correct constructor
    private Subgoal createMockSubgoal(String id, String name) {
        return new Subgoal(
                id,                    // id
                name,                  // name
                "Test description",    // description
                "test-plan",           // planId
                USER_ID,               // userId
                LocalDate.now(),       // dueDate
                false,                 // isCompleted
                false                  // isPriority
        );
    }



    // Test Double Implementations
    static class TestSubgoalDataAccess implements SubgoalDataAccessInterface {
        public String lastCalledMethod;
        public String lastUserId;
        public String lastPlanId;
        public String lastSubgoalName;

        private List<Subgoal> prioritySubgoals;
        private List<Subgoal> subgoalsByPlanId;
        private List<Subgoal> subgoalsByName;
        private List<Subgoal> allSubgoals;

        public void setPrioritySubgoals(List<Subgoal> subgoals) { this.prioritySubgoals = subgoals; }
        public void setSubgoalsByPlanId(List<Subgoal> subgoals) { this.subgoalsByPlanId = subgoals; }
        public void setSubgoalsByName(List<Subgoal> subgoals) { this.subgoalsByName = subgoals; }
        public void setAllSubgoals(List<Subgoal> subgoals) { this.allSubgoals = subgoals; }


        @Override
        public List<Subgoal> getPrioritySubgoals(String userId) {
            lastCalledMethod = "getPrioritySubgoals";
            lastUserId = userId;
            return prioritySubgoals;
        }
        @Override
        public Subgoal getSubgoalById(String id) {
            lastCalledMethod = "getSubgoalById";
            return null;
        }


        @Override
        public void saveUpdatedSubgoal(Subgoal subgoal) {
            // Implement this method - it can be empty for testing
            lastCalledMethod = "saveUpdatedSubgoal";
        }

        @Override
        public List<Subgoal> getSubgoalsByPlanId(String planId) {
            lastCalledMethod = "getSubgoalsByPlanId";
            lastPlanId = planId;
            return subgoalsByPlanId;
        }

        @Override
        public List<Subgoal> getSubgoalsByName(String subgoalName, String userId) {
            lastCalledMethod = "getSubgoalsByName";
            lastSubgoalName = subgoalName;
            lastUserId = userId;
            return subgoalsByName;
        }

        @Override
        public List<Subgoal> getAllSubgoalsForUser(String userId) {
            lastCalledMethod = "getAllSubgoalsForUser";
            lastUserId = userId;
            return allSubgoals;
        }
    }

    static class TestPresenter implements FilterSubgoalsOutputBoundary {
        public FilterSubgoalsOutputData lastOutputData;

        @Override
        public void present(FilterSubgoalsOutputData outputData) {
            this.lastOutputData = outputData;
        }
    }


}