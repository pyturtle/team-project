package use_case.subgoal.show_subgoal;

import entity.subgoal.Subgoal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShowSubgoalInteractor.
 */
public class ShowSubgoalInteractorTest {

    /**
     * Happy-path: subgoal exists.
     */
    @Test
    void execute_subgoalExists_presentsCorrectData() {
        // Arrange
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        Subgoal subgoal = new Subgoal(
                "s1",                         // id
                "plan1",                      // planId
                "user1",                      // username
                "Read chapter 1",             // name
                "Read the first chapter.",    // description
                LocalDate.of(2025, 1, 1),     // deadline
                false,                        // completed
                true                          // priority
        );
        dao.save(subgoal);

        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
        ShowSubgoalInputData inputData = new ShowSubgoalInputData("s1");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(presenter.errorMessage,
                "No error should be presented on success.");
        assertNotNull(presenter.outputData,
                "Output data should be presented for an existing subgoal.");

        assertEquals("s1", presenter.outputData.getId());
        assertEquals("Read chapter 1", presenter.outputData.getName());
        assertEquals("Read the first chapter.", presenter.outputData.getDescription());
        assertTrue(presenter.outputData.isPriority());
        assertFalse(presenter.outputData.isCompleted());
    }

    /**
     * Error-path: subgoal does not exist.
     */
    @Test
    void execute_subgoalDoesNotExist_presentsError() {
        // Arrange
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();
        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
        ShowSubgoalInputData inputData = new ShowSubgoalInputData("missing-id");

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(presenter.outputData,
                "No success output should be sent when the subgoal is missing.");
        assertNotNull(presenter.errorMessage,
                "An error message should be presented for a missing subgoal.");
        assertTrue(presenter.errorMessage.contains("missing-id"));
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory DAO used only for this test.
     * It implements ALL methods from SubgoalDataAccessInterface,
     * but only getSubgoalById / save / saveUpdatedSubgoal actually do work.
     */
    private static class InMemorySubgoalDAO implements SubgoalDataAccessInterface {

        private final Map<String, Subgoal> storage = new HashMap<>();

        // --- methods we actually use in the test / interactor ---

        @Override
        public void save(Subgoal subgoal) {
            // assumes Subgoal has getId(); if not, change accordingly
            storage.put(subgoal.getId(), subgoal);
        }

        @Override
        public Subgoal getSubgoalById(String id) {
            return storage.get(id);
        }

        @Override
        public void saveUpdatedSubgoal(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        // --- methods required by the interface but not used in this test ---

        @Override
        public void save() {
            // no-op
        }

        @Override
        public void updatePriority(String id, boolean priority) {
            // no-op for this test
        }

        @Override
        public void updateCompleted(String id, boolean completed) {
            // no-op for this test
        }

        @Override
        public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
            return List.of();  // stub
        }

        @Override
        public List<Subgoal> getPrioritySubgoals(String userId) {
            return List.of();  // stub
        }

        @Override
        public List<Subgoal> getSubgoalsByUsername(String username) {
            return List.of();  // stub
        }

        @Override
        public List<Subgoal> getAllSubgoalsForUser(String userId) {
            return List.of();  // stub
        }

        @Override
        public void deleteSubgoal(String id) {
            storage.remove(id);
        }

        @Override
        public List<Subgoal> getSubgoalsByPlanId(String planId) {
            return List.of();  // stub
        }
    }

    /**
     * Presenter that just stores whatever the interactor sends.
     */
    private static class TestPresenter implements ShowSubgoalOutputBoundary {

        ShowSubgoalOutputData outputData;
        String errorMessage;

        @Override
        public void present(ShowSubgoalOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void presentError(String message) {
            this.errorMessage = message;
        }
    }
}
