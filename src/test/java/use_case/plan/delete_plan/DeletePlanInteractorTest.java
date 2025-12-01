package use_case.plan.delete_plan;

import entity.plan.Plan;
import entity.subgoal.Subgoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DeletePlanInteractor.
 * Tests User Story 8: As a user, I want to be able to delete the existing plan.
 */
public class DeletePlanInteractorTest {

    private InMemoryPlanDAO planDAO;
    private InMemorySubgoalDAO subgoalDAO;
    private TestPresenter presenter;
    private DeletePlanInteractor interactor;

    @BeforeEach
    void setUp() {
        planDAO = new InMemoryPlanDAO();
        subgoalDAO = new InMemorySubgoalDAO();
        presenter = new TestPresenter();
        interactor = new DeletePlanInteractor(planDAO, presenter, subgoalDAO);
    }

    /**
     * Happy path: Delete a plan that exists with no subgoals.
     */
    @Test
    void execute_planExistsWithNoSubgoals_deletesSuccessfully() {
        // Arrange
        Plan plan = new Plan("plan1", "user1", "My Plan", "Description");
        planDAO.save(plan);

        DeletePlanInputData inputData = new DeletePlanInputData("plan1", "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(presenter.errorMessage, "No error should occur for valid deletion");
        assertNotNull(presenter.outputData, "Output data should be presented");
        assertEquals("plan1", presenter.outputData.getDeletedPlanId());
        assertTrue(presenter.outputData.isSuccess());
        assertNull(planDAO.getPlanById("plan1"), "Plan should be deleted from storage");
    }

    /**
     * Happy path: Delete a plan with multiple related subgoals.
     * Should cascade delete all related subgoals.
     */
    @Test
    void execute_planExistsWithSubgoals_deletesAllRelatedSubgoals() {
        // Arrange
        Plan plan = new Plan("plan1", "user1", "My Plan", "Description");
        planDAO.save(plan);

        // Add 3 subgoals related to this plan
        Subgoal subgoal1 = new Subgoal("sub1", "plan1", "user1", "Task 1",
                "Description 1", LocalDate.of(2025, 1, 1), false, false);
        Subgoal subgoal2 = new Subgoal("sub2", "plan1", "user1", "Task 2",
                "Description 2", LocalDate.of(2025, 1, 2), false, true);
        Subgoal subgoal3 = new Subgoal("sub3", "plan1", "user1", "Task 3",
                "Description 3", LocalDate.of(2025, 1, 3), true, false);

        subgoalDAO.save(subgoal1);
        subgoalDAO.save(subgoal2);
        subgoalDAO.save(subgoal3);

        DeletePlanInputData inputData = new DeletePlanInputData("plan1", "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(presenter.errorMessage, "No error should occur");
        assertTrue(presenter.outputData.isSuccess());
        assertNull(planDAO.getPlanById("plan1"), "Plan should be deleted");
        assertNull(subgoalDAO.getSubgoalByIdTest("sub1"), "Subgoal 1 should be deleted");
        assertNull(subgoalDAO.getSubgoalByIdTest("sub2"), "Subgoal 2 should be deleted");
        assertNull(subgoalDAO.getSubgoalByIdTest("sub3"), "Subgoal 3 should be deleted");
    }

    /**
     * Verify cascade delete only removes subgoals for the deleted plan.
     */
    @Test
    void execute_deleteOnePlan_doesNotDeleteOtherPlansSubgoals() {
        // Arrange
        Plan plan1 = new Plan("plan1", "user1", "Plan 1", "Description 1");
        Plan plan2 = new Plan("plan2", "user1", "Plan 2", "Description 2");
        planDAO.save(plan1);
        planDAO.save(plan2);

        Subgoal subgoalForPlan1 = new Subgoal("sub1", "plan1", "user1", "Task for Plan 1",
                "Description", LocalDate.of(2025, 1, 1), false, false);
        Subgoal subgoalForPlan2 = new Subgoal("sub2", "plan2", "user1", "Task for Plan 2",
                "Description", LocalDate.of(2025, 1, 2), false, false);

        subgoalDAO.save(subgoalForPlan1);
        subgoalDAO.save(subgoalForPlan2);

        DeletePlanInputData inputData = new DeletePlanInputData("plan1", "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(planDAO.getPlanById("plan1"), "Plan 1 should be deleted");
        assertNotNull(planDAO.getPlanById("plan2"), "Plan 2 should still exist");
        assertNull(subgoalDAO.getSubgoalByIdTest("sub1"), "Subgoal for plan1 should be deleted");
        assertNotNull(subgoalDAO.getSubgoalByIdTest("sub2"), "Subgoal for plan2 should still exist");
    }

    /**
     * Error path: Try to delete a plan that doesn't exist.
     */
    @Test
    void execute_planDoesNotExist_presentsError() {
        // Arrange
        DeletePlanInputData inputData = new DeletePlanInputData("nonexistent", "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.errorMessage, "Error message should be presented");
        assertTrue(presenter.errorMessage.contains("Plan not found"));
        assertNull(presenter.outputData, "No success output should be sent");
    }

    /**
     * Error path: Try to delete with null or empty plan ID.
     */
    @Test
    void execute_nullPlanId_presentsError() {
        // Arrange
        DeletePlanInputData inputData = new DeletePlanInputData(null, "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.errorMessage, "Error message should be presented");
        assertTrue(presenter.errorMessage.contains("Plan ID cannot be empty"));
    }

    /**
     * Error path: Try to delete with empty plan ID.
     */
    @Test
    void execute_emptyPlanId_presentsError() {
        // Arrange
        DeletePlanInputData inputData = new DeletePlanInputData("   ", "user1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.errorMessage, "Error message should be presented");
        assertTrue(presenter.errorMessage.contains("Plan ID cannot be empty"));
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory plan DAO for testing.
     */
    private static class InMemoryPlanDAO implements data_access.interfaces.plan.DeletePlanDataAccessInterface {
        private final Map<String, Plan> storage = new HashMap<>();

        void save(Plan plan) {
            storage.put(plan.getId(), plan);
        }

        Plan getPlanById(String id) {
            return storage.get(id);
        }

        @Override
        public boolean deletePlan(String planId) {
            return storage.remove(planId) != null;
        }
    }

    /**
     * In-memory subgoal DAO for testing cascade delete.
     */
    private static class InMemorySubgoalDAO implements data_access.interfaces.subgoal.SubgoalDataAccessInterface {
        private final Map<String, Subgoal> storage = new HashMap<>();

        void save(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        public Subgoal getSubgoalByIdTest(String id) {
            return storage.get(id);
        }

        @Override
        public void deleteSubgoal(String id) {
            storage.remove(id);
        }

        @Override
        public List<Subgoal> getSubgoalsByPlanId(String planId) {
            List<Subgoal> result = new ArrayList<>();
            for (Subgoal s : storage.values()) {
                if (s.getPlanId().equals(planId)) {
                    result.add(s);
                }
            }
            return result;
        }

        // Stub methods required by interface but not used in this test
        @Override
        public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getPrioritySubgoals(String userId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getSubgoalsByName(String name, String userId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getCompletedSubgoals(String userId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getIncompleteSubgoals(String userId) {
            return new ArrayList<>();
        }

        @Override
        public Subgoal getSubgoalById(String subgoalId) {
            return storage.get(subgoalId);
        }

        @Override
        public void saveSubgoal(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        @Override
        public void updatePriority(String id, boolean priority) {
        }

        @Override
        public void updateCompleted(String id, boolean completed) {
        }

        @Override
        public List<Subgoal> getSubgoalsByUsername(String username) {
            List<Subgoal> result = new ArrayList<>();
            for (Subgoal s : storage.values()) {
                if (s.getUsername().equals(username)) {
                    result.add(s);
                }
            }
            return result;
        }

        @Override
        public List<Subgoal> getAllSubgoalsForUser(String userId) {
            return getSubgoalsByUsername(userId);
        }
    }

    /**
     * Test presenter that captures output.
     */
    private static class TestPresenter implements DeletePlanOutputBoundary {
        DeletePlanOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(DeletePlanOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

