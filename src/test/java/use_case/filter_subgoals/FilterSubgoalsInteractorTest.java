package use_case.filter_subgoals;

import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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

    // ==================== PRIORITY FILTER TEST ====================
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

    // ==================== PLAN FILTER TEST ====================
    @Test
    void testFilterByPlanId() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                USER_ID, PLAN_ID, null, false
        );

        Subgoal mockSubgoal = createMockSubgoal("subgoal1", "Plan Subgoal");
        subgoalDAO.setSubgoalsByPlanTestData(Arrays.asList(mockSubgoal));

        // Act
        interactor.filter(inputData);

        // Assert
        assertEquals("getSubgoalsByPlan", subgoalDAO.lastCalledMethod);
        assertEquals(PLAN_ID, subgoalDAO.lastPlanId);
        assertEquals(USER_ID, subgoalDAO.lastUserId);
        assertEquals(1, presenter.lastOutputData.getFilteredSubgoals().size());
        assertEquals(PLAN_ID, presenter.lastOutputData.getPlanId());
        assertFalse(presenter.lastOutputData.getPriorityOnly());
    }

    // ==================== NAME FILTER TEST ====================
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

    // ==================== ALL SUBGOALS TEST ====================
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

    // ==================== PRIORITY PRECEDENCE TEST ====================
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
        assertNotEquals("getSubgoalsByPlan", subgoalDAO.lastCalledMethod);
        assertTrue(presenter.lastOutputData.getPriorityOnly());
    }

    // ==================== EMPTY RESULTS TEST ====================
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

    // ==================== EDGE CASE: ALL NULL/EMPTY ====================
    @Test
    void testAllFiltersNullReturnsAllSubgoals() {
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
        assertEquals(2, presenter.lastOutputData.getFilteredSubgoals().size());
    }

    // ==================== EDGE CASE: NULL USER ID ====================
    @Test
    void testNullUserIdHandledGracefully() {
        // Arrange
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                null, PLAN_ID, null, false
        );

        Subgoal mockSubgoal = createMockSubgoal("subgoal1", "Plan Subgoal");
        subgoalDAO.setSubgoalsByPlanTestData(Arrays.asList(mockSubgoal));

        // Act
        interactor.filter(inputData);

        // Assert - Should still call the method with null userId
        assertEquals("getSubgoalsByPlan", subgoalDAO.lastCalledMethod);
        assertEquals(PLAN_ID, subgoalDAO.lastPlanId);
        assertNull(subgoalDAO.lastUserId);
    }

    // ==================== HELPER METHOD ====================
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

    // ==================== TEST DOUBLE IMPLEMENTATIONS ====================
    static class TestSubgoalDataAccess implements SubgoalDataAccessInterface {
        public String lastCalledMethod;
        public String lastUserId;
        public String lastPlanId;
        public String lastSubgoalName;

        private List<Subgoal> prioritySubgoals;
        private List<Subgoal> subgoalsByPlanTestData;
        private List<Subgoal> subgoalsByName;
        private List<Subgoal> allSubgoals;
        private List<Subgoal> subgoalsByPlanId;

        public void setPrioritySubgoals(List<Subgoal> subgoals) { this.prioritySubgoals = subgoals; }
        public void setSubgoalsByPlanTestData(List<Subgoal> subgoals) { this.subgoalsByPlanTestData = subgoals; }
        public void setSubgoalsByName(List<Subgoal> subgoals) { this.subgoalsByName = subgoals; }
        public void setAllSubgoals(List<Subgoal> subgoals) { this.allSubgoals = subgoals; }
        public void setSubgoalsByPlanId(List<Subgoal> subgoals) { this.subgoalsByPlanId = subgoals; }

        @Override
        public List<Subgoal> getPrioritySubgoals(String userId) {
            lastCalledMethod = "getPrioritySubgoals";
            lastUserId = userId;
            return prioritySubgoals;
        }

        @Override
        public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
            lastCalledMethod = "getSubgoalsByPlan";
            lastPlanId = planId;
            lastUserId = userId;
            return subgoalsByPlanTestData != null ? subgoalsByPlanTestData : new ArrayList<>();
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

        @Override
        public List<Subgoal> getSubgoalsByPlanId(String planId) {
            lastCalledMethod = "getSubgoalsByPlanId";
            lastPlanId = planId;
            return subgoalsByPlanId;
        }

        // ===== UNUSED METHODS (required by interface but not by interactor) =====
        @Override
        public List<Subgoal> getIncompleteSubgoals(String parentGoalId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getCompletedSubgoals(String parentGoalId) {
            return new ArrayList<>();
        }

        @Override
        public Subgoal getSubgoalById(String id) {
            lastCalledMethod = "getSubgoalById";
            return null;
        }

        @Override
        public void saveSubgoal(Subgoal subgoal) {
            lastCalledMethod = "saveSubgoal";
        }

        @Override
        public void deleteSubgoal(String id) {
            lastCalledMethod = "deleteSubgoal";
        }

        @Override
        public void updatePriority(String id, boolean priority) {
            lastCalledMethod = "updatePriority";
        }

        @Override
        public void updateCompleted(String id, boolean completed) {
            lastCalledMethod = "updateCompleted";
        }

        @Override
        public List<Subgoal> getSubgoalsByUsername(String username) {
            lastCalledMethod = "getSubgoalsByUsername";
            lastUserId = username;
            return new ArrayList<>();
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