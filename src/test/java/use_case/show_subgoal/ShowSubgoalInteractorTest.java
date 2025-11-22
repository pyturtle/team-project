//package use_case.show_subgoal;
//
//import entity.subgoal.Subgoal;
//import org.junit.jupiter.api.Test;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ShowSubgoalInteractorTest {
//
//    /**
//     * A simple in-memory fake DAO for testing.
//     */
//    static class InMemorySubgoalDAO implements SubgoalDataAccessInterface {
//        Map<Integer, Subgoal> data = new HashMap<>();
//        boolean priorityUpdated = false;
//
//        @Override
//        public Subgoal getSubgoalById(int id) {
//            return data.get(id);
//        }
//
//        @Override
//        public void updatePriority(int id, boolean priority) {
//            priorityUpdated = true;
//            // Make new immutable Subgoal
//            data.compute(id, (k, old) -> new Subgoal(old.getId(),
//                    old.getPlanId(),
//                    old.getusername(),
//                    old.getName(),
//                    old.getDescription(),
//                    old.getDeadline(),
//                    old.isCompleted(),
//                    priority));
//        }
//    }
//
//    /**
//     * A presenter spy to capture output.
//     */
//    static class PresenterSpy implements ShowSubgoalOutputBoundary {
//        ShowSubgoalOutputData lastOutput;
//        String lastError = null;
//
//        @Override
//        public void present(ShowSubgoalOutputData outputData) {
//            lastOutput = outputData;
//        }
//
//        @Override
//        public void presentError(String message) {
//            lastError = message;
//        }
//    }
//
//    @Test
//    void testInteractorLoadsSubgoal() {
//        // Arrange
//        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();
//
//        Subgoal s = new Subgoal(
//                1, 1, 1,
//                "Test Subgoal",
//                "Description here",
//                LocalDate.now(),
//                false,
//                true
//        );
//
//        dao.data.put(1, s);
//
//        PresenterSpy presenter = new PresenterSpy();
//        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
//
//        // Act
//        interactor.execute(new ShowSubgoalInputData(1));
//
//        // Assert
//        assertNotNull(presenter.lastOutput);
//        assertEquals("Test Subgoal", presenter.lastOutput.getName());
//        assertEquals("Description here", presenter.lastOutput.getDescription());
//        assertTrue(presenter.lastOutput.isPriority());
//        assertNull(presenter.lastError);
//    }
//
//    @Test
//    void testSetPriorityUpdatesDAO() {
//        // Arrange
//        InMemorySubgoalDAO dao = new InMemorySubgoalDAO();
//
//        Subgoal s = new Subgoal(
//                1, 1, 1,
//                "Study",
//                "Study hard",
//                LocalDate.now().plusDays(1),
//                false,
//                false
//        );
//
//        dao.data.put(1, s);
//
//        PresenterSpy presenter = new PresenterSpy();
//        ShowSubgoalInteractor interactor = new ShowSubgoalInteractor(dao, presenter);
//
//        // Act
//        interactor.setPriority(new SetPriorityInputData(1, true));
//
//        // Assert
//        assertTrue(dao.priorityUpdated);
//        assertTrue(presenter.lastOutput.isPriority());
//        assertEquals("Study", presenter.lastOutput.getName());
//    }
//}
