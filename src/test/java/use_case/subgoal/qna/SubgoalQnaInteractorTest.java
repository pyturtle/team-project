package use_case.subgoal.qna;

import entity.subgoal.SubgoalQuestionAnswer;
import entity.subgoal.Subgoal;
import org.junit.jupiter.api.Test;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalQnaDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalQnaGeminiDataAccessInterface;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SubgoalQnaInteractor.
 */
public class SubgoalQnaInteractorTest {

    /**
     * Test opening Q/A: should load history from DAO and pass it to presenter.
     */
    @Test
    void open_loadsHistoryAndPresentsInitialData() {
        // Arrange
        InMemoryQnaDAO qnaDAO = new InMemoryQnaDAO();
        InMemorySubgoalDAO subgoalDAO = new InMemorySubgoalDAO();
        FakeGeminiGateway gemini = new FakeGeminiGateway();
        TestPresenter presenter = new TestPresenter();

        String subgoalId = "sg1";

        // seed one history entry
        qnaDAO.appendEntry(subgoalId, "Old question?", "Old answer.");

        SubgoalQnaInteractor interactor =
                new SubgoalQnaInteractor(qnaDAO, subgoalDAO, gemini, presenter);

        SubgoalQnaOpenInputData inputData = new SubgoalQnaOpenInputData(subgoalId);

        // Act
        interactor.open(inputData);

        // Assert
        assertNull(presenter.errorMessage);
        assertNotNull(presenter.initialData);
        assertNull(presenter.updateData);

        assertEquals(subgoalId, presenter.initialData.getSubgoalId());
        List<SubgoalQuestionAnswer> history = presenter.initialData.getHistory();
        assertEquals(1, history.size());
        assertEquals("Old question?", history.get(0).getQuestionMessage());
        assertEquals("Old answer.", history.get(0).getResponseMessage());
    }

    /**
     * Test asking with an empty question: should present error and not call Gemini.
     */
    @Test
    void ask_emptyQuestion_presentsErrorAndDoesNotCallGemini() {
        // Arrange
        InMemoryQnaDAO qnaDAO = new InMemoryQnaDAO();
        InMemorySubgoalDAO subgoalDAO = new InMemorySubgoalDAO();
        FakeGeminiGateway gemini = new FakeGeminiGateway();
        TestPresenter presenter = new TestPresenter();

        SubgoalQnaInteractor interactor =
                new SubgoalQnaInteractor(qnaDAO, subgoalDAO, gemini, presenter);

        SubgoalQnaAskInputData inputData =
                new SubgoalQnaAskInputData("sg1", "   "); // blank question

        // Act
        interactor.ask(inputData);

        // Assert
        assertNotNull(presenter.errorMessage);
        assertEquals("Question cannot be empty.", presenter.errorMessage);
        assertNull(presenter.updateData);
        assertNull(presenter.initialData);
        assertFalse(gemini.called);
        assertTrue(qnaDAO.getHistory("sg1").isEmpty());
    }

    /**
     * Test asking a valid question: should build prompt, call Gemini, append entry,
     * and present updated history.
     */
    @Test
    void ask_validQuestion_savesNewEntryAndPresentsUpdate() {
        // Arrange
        InMemoryQnaDAO qnaDAO = new InMemoryQnaDAO();
        InMemorySubgoalDAO subgoalDAO = new InMemorySubgoalDAO();
        FakeGeminiGateway gemini = new FakeGeminiGateway();
        TestPresenter presenter = new TestPresenter();

        String subgoalId = "sg1";
        String planId = "plan1";

        // seed some subgoals in the same plan
        Subgoal current = new Subgoal(
                subgoalId, planId, "user1",
                "Current subgoal",
                "Do something important.",
                LocalDate.of(2025, 1, 1),
                false,
                true
        );
        subgoalDAO.save(current);

        Subgoal other = new Subgoal(
                "sg2", planId, "user1",
                "Another subgoal",
                "Another thing.",
                LocalDate.of(2025, 1, 2),
                false,
                false
        );
        subgoalDAO.save(other);

        // seed previous history
        qnaDAO.appendEntry(subgoalId, "Old question?", "Old answer.");

        SubgoalQnaInteractor interactor =
                new SubgoalQnaInteractor(qnaDAO, subgoalDAO, gemini, presenter);

        String newQuestion = "What should I do next?";
        SubgoalQnaAskInputData inputData =
                new SubgoalQnaAskInputData(subgoalId, newQuestion);

        // Act
        interactor.ask(inputData);

        // Assert
        assertNull(presenter.errorMessage);
        assertNotNull(presenter.updateData);
        assertNull(presenter.initialData);

        assertTrue(gemini.called, "Gemini gateway should have been called.");
        assertNotNull(gemini.lastPrompt);
        assertTrue(gemini.lastPrompt.contains(newQuestion),
                "Prompt passed to Gemini should include the new question.");

        assertEquals(subgoalId, presenter.updateData.getSubgoalId());
        List<SubgoalQuestionAnswer> history = presenter.updateData.getHistory();
        assertEquals(2, history.size(), "History should contain old and new entries.");

        SubgoalQuestionAnswer latest = history.get(history.size() - 1);
        assertEquals(newQuestion, latest.getQuestionMessage());
        assertEquals("FAKE_ANSWER", latest.getResponseMessage());
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory implementation of SubgoalQnaDataAccessInterface.
     */
    private static class InMemoryQnaDAO implements SubgoalQnaDataAccessInterface {

        private final Map<String, List<SubgoalQuestionAnswer>> storage = new HashMap<>();

        @Override
        public List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
            List<SubgoalQuestionAnswer> list = storage.get(subgoalId);
            if (list == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(list); // return a copy
        }

        @Override
        public SubgoalQuestionAnswer appendEntry(String subgoalId,
                                                 String questionMessage,
                                                 String responseMessage) {
            List<SubgoalQuestionAnswer> list =
                    storage.computeIfAbsent(subgoalId, k -> new ArrayList<>());

            String id = subgoalId + "#" + (list.size() + 1);
            SubgoalQuestionAnswer entry =
                    new SubgoalQuestionAnswer(id, subgoalId, questionMessage, responseMessage);
            list.add(entry);
            return entry;
        }
    }

    /**
     * In-memory implementation of SubgoalDataAccessInterface for tests.
     * Only getSubgoalById and getSubgoalsByPlanId are actually used.
     */
    private static class InMemorySubgoalDAO implements SubgoalDataAccessInterface {

        private final Map<String, Subgoal> storage = new HashMap<>();

        void save(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        @Override
        public Subgoal getSubgoalById(String id) {
            return storage.get(id);
        }

        @Override
        public void saveSubgoal(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        @Override
        public void updatePriority(String id, boolean priority) {
            // not needed for tests
        }

        @Override
        public void updateCompleted(String id, boolean completed) {
            // not needed for tests
        }

        @Override
        public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
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
        public List<Subgoal> getPrioritySubgoals(String userId) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getSubgoalsByUsername(String username) {
            return new ArrayList<>();
        }

        @Override
        public List<Subgoal> getAllSubgoalsForUser(String userId) {
            return new ArrayList<>();
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
    }

    /**
     * Fake Gemini gateway that records whether it was called and what prompt it saw.
     */
    private static class FakeGeminiGateway implements SubgoalQnaGeminiDataAccessInterface {

        boolean called = false;
        String lastPrompt = null;

        @Override
        public String getAnswerForQuestion(String question) {
            this.called = true;
            this.lastPrompt = question;
            return "FAKE_ANSWER";
        }
    }

    /**
     * Presenter test double that records what is presented.
     */
    private static class TestPresenter implements SubgoalQnaOutputBoundary {

        SubgoalQnaOutputData initialData;
        SubgoalQnaOutputData updateData;
        String errorMessage;

        @Override
        public void presentInitial(SubgoalQnaOutputData outputData) {
            this.initialData = outputData;
        }

        @Override
        public void presentUpdate(SubgoalQnaOutputData outputData) {
            this.updateData = outputData;
        }

        @Override
        public void presentError(String message) {
            this.errorMessage = message;
        }
    }
}
