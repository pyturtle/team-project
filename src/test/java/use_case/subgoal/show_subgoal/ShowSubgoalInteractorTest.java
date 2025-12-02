package use_case.subgoal.show_subgoal;

import entity.subgoal.Subgoal;
import org.junit.jupiter.api.Test;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.subgoal.show_subgoal.ShowSubgoalPresenter;
import interface_adapter.subgoal.show_subgoal.ShowSubgoalViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Test
    void setPriority_existingSubgoal_updatesPriorityAndPresentsUpdate() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        Subgoal subgoal = new Subgoal(
                "s1",
                "plan1",
                "user1",
                "Task",
                "Desc",
                LocalDate.of(2025, 1, 1),
                false,
                false   // initial priority = false
        );
        dao.save(subgoal);

        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);

        SetPriorityInputData input = new SetPriorityInputData("s1", true);

        // Act
        interactor.setPriority(input);

        // Assert
        assertNull(presenter.errorMessage);

        Subgoal updated = dao.getSubgoalById("s1");
        assertTrue(updated.isPriority(), "DAO should store the updated priority.");
    }

    @Test
    void setCompleted_existingSubgoal_updatesCompletedAndPresentsUpdate() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        Subgoal subgoal = new Subgoal(
                "s2",
                "plan1",
                "user1",
                "Another task",
                "Desc",
                LocalDate.of(2025, 1, 2),
                false,   // initial completed = false
                true
        );
        dao.save(subgoal);

        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);

        SetCompletedInputData input = new SetCompletedInputData("s2", true);

        // Act
        interactor.setCompleted(input);

        // Assert
        assertNull(presenter.errorMessage);

        Subgoal updated = dao.getSubgoalById("s2");
        assertTrue(updated.isCompleted(), "DAO should store the updated completion state.");
    }

    @Test
    void getSubgoalsByPlan_returnsOnlyMatchingSubgoals() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        Subgoal s1 = new Subgoal(
                "a",
                "planX",
                "user1",
                "Task A",
                "Desc A",
                LocalDate.of(2025, 1, 1),
                false,
                false
        );
        Subgoal s2 = new Subgoal(
                "b",
                "planX",
                "user1",
                "Task B",
                "Desc B",
                LocalDate.of(2025, 1, 2),
                false,
                true
        );
        Subgoal s3 = new Subgoal(
                "c",
                "planY",          // different plan
                "user1",
                "Task C",
                "Desc C",
                LocalDate.of(2025, 1, 3),
                false,
                false
        );

        dao.save(s1);
        dao.save(s2);
        dao.save(s3);

        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);

        // Act
        List<Subgoal> result = interactor.getSubgoalsByPlan("planX", "user1");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(s -> s.getId().equals("a")));
        assertTrue(result.stream().anyMatch(s -> s.getId().equals("b")));
    }

    @Test
    void setPriority_missingSubgoal_presentsError() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();
        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);

        // We never save "missing" into dao, so getSubgoalById("missing") returns null.
        SetPriorityInputData input = new SetPriorityInputData("missing", true);

        interactor.setPriority(input);

        assertEquals(
                "Subgoal with id missing was not found after updating priority.",
                presenter.errorMessage
        );
    }

    @Test
    void setCompleted_missingSubgoal_presentsError() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();
        TestPresenter presenter = new TestPresenter();
        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);

        SetCompletedInputData input = new SetCompletedInputData("missing", true);

        interactor.setCompleted(input);

        assertEquals(
                "Subgoal with id missing was not found after updating completion.",
                presenter.errorMessage
        );
    }

    /**
     * Happy-path for setPriority using the real ShowSubgoalPresenter so that
     * the instanceof ShowSubgoalPresenter branch is exercised.
     */
    @Test
    void setPriority_withShowSubgoalPresenter_triggersPresenterUpdate() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        Subgoal subgoal = new Subgoal(
                "sp1",
                "plan-presenter",
                "user1",
                "Presenter test subgoal",
                "Presenter branch description",
                LocalDate.of(2025, 1, 10),
                false,
                false
        );
        dao.save(subgoal);

        ShowSubgoalViewModel viewModel = new ShowSubgoalViewModel();
        DialogManagerModel dialogManagerModel = new DialogManagerModel();
        CalendarViewModel calendarViewModel = new CalendarViewModel();
        ShowSubgoalPresenter presenter =
                new ShowSubgoalPresenter(viewModel, dialogManagerModel, calendarViewModel);

        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
        SetPriorityInputData inputData = new SetPriorityInputData("sp1", true);

        // Act
        interactor.setPriority(inputData);

        // Assert: the DAO should now store the updated priority.
        Subgoal updated = dao.getSubgoalById("sp1");
        assertNotNull(updated);
        assertTrue(updated.isPriority());
    }

    /**
     * Uses the real ShowSubgoalPresenter so that the instanceof
     * ShowSubgoalPresenter branch in setCompleted is exercised.
     */
    @Test
    void setCompleted_withShowSubgoalPresenter_triggersPresenterUpdate() {
        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();

        // Existing subgoal so s != null
        Subgoal subgoal = new Subgoal(
                "sc1",
                "plan-presenter",
                "user1",
                "Presenter completed test subgoal",
                "Presenter branch description for completed",
                LocalDate.of(2025, 1, 15),
                false,   // completed = false initially
                false    // priority
        );
        dao.save(subgoal);

        // Real presenter → instanceof ShowSubgoalPresenter is true
        ShowSubgoalViewModel viewModel = new ShowSubgoalViewModel();
        DialogManagerModel dialogManagerModel = new DialogManagerModel();
        CalendarViewModel calendarViewModel = new CalendarViewModel();
        ShowSubgoalPresenter presenter =
                new ShowSubgoalPresenter(viewModel, dialogManagerModel, calendarViewModel);

        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
        SetCompletedInputData inputData = new SetCompletedInputData("sc1", true);

        // Act
        interactor.setCompleted(inputData);

        // Assert: DAO reflects the updated completion state
        Subgoal updated = dao.getSubgoalById("sc1");
        assertNotNull(updated);
        assertTrue(updated.isCompleted());
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory DAO used only for this test.
     * It implements ALL methods from SubgoalDataAccessInterface,
     * but only getSubgoalById and saveSubgoal actually do work.
     */
    private static class InMemorySubgoalDAO implements SubgoalDataAccessInterface {

        private final Map<String, Subgoal> storage = new HashMap<>();

        // Helper method for test setup (not part of interface)
        void save(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        // --- methods we actually use in the test / interactor ---

        @Override
        public Subgoal getSubgoalById(String id) {
            return storage.get(id);
        }

        @Override
        public void saveSubgoal(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        // --- methods required by the interface but not used in this test ---

        @Override
        public void updatePriority(String id, boolean priority) {
            Subgoal existing = storage.get(id);
            if (existing != null) {
                storage.put(id, new Subgoal(
                        existing.getId(),
                        existing.getPlanId(),
                        existing.getUsername(),
                        existing.getName(),
                        existing.getDescription(),
                        existing.getDeadline(),
                        existing.isCompleted(),
                        priority
                ));
            }
        }

        @Override
        public void updateCompleted(String id, boolean completed) {
            Subgoal existing = storage.get(id);
            if (existing != null) {
                storage.put(id, new Subgoal(
                        existing.getId(),
                        existing.getPlanId(),
                        existing.getUsername(),
                        existing.getName(),
                        existing.getDescription(),
                        existing.getDeadline(),
                        completed,
                        existing.isPriority()
                ));
            }
        }

        @Override
        public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
            List<Subgoal> result = new ArrayList<>();
            for (Subgoal s : storage.values()) {
                if (s.getPlanId().equals(planId) && s.getUsername().equals(userId)) {
                    result.add(s);
                }
            }
            return result;
        }

        @Override
        public List<Subgoal> getSubgoalsByName(String name, String userId) {
            return List.of();  // stub
        }

        @Override
        public List<Subgoal> getCompletedSubgoals(String userId) {
            return List.of();  // stub
        }

        @Override
        public List<Subgoal> getIncompleteSubgoals(String userId) {
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
