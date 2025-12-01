package use_case.plan.show_plans;

import entity.plan.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShowPlansInteractor.
 * Tests User Story 6: As a user, I want to be able to see the list of all the plans
 * that I have created in the list format, so I can read the description of each plan,
 * as well as the list of subgoals corresponding to each plan separately.
 */
public class ShowPlansInteractorTest {

    private InMemoryPlanDAO planDAO;
    private TestPresenter presenter;
    private ShowPlansInteractor interactor;

    @BeforeEach
    void setUp() {
        planDAO = new InMemoryPlanDAO();
        presenter = new TestPresenter();
        interactor = new ShowPlansInteractor(planDAO, presenter);
    }

    /**
     * Happy path: User has multiple plans, retrieve first page.
     */
    @Test
    void execute_userHasMultiplePlans_returnsFirstPage() {
        // Arrange - Create 5 plans for user1
        for (int i = 1; i <= 5; i++) {
            Plan plan = new Plan("plan" + i, "Plan " + i, "Description for plan " + i, "user1");
            planDAO.save(plan);
        }

        ShowPlansInputData inputData = new ShowPlansInputData("user1", 0, 3);

        // Act
        interactor.execute(inputData);

        // Assert
        System.out.println("DEBUG TEST 1: Error message: " + presenter.errorMessage);
        System.out.println("DEBUG TEST 1: Output data null? " + (presenter.outputData == null));
        if (presenter.outputData != null) {
            System.out.println("DEBUG TEST 1: Plans returned: " + presenter.outputData.getPlans().size());
            for (Plan p : presenter.outputData.getPlans()) {
                System.out.println("DEBUG TEST 1: Plan - ID: " + p.getId() + ", Name: " + p.getName() + ", User: " + p.getUsername());
            }
        }

        assertNull(presenter.errorMessage, "No error should occur");
        assertNotNull(presenter.outputData, "Output data should be presented");
        assertEquals(3, presenter.outputData.getPlans().size(), "Should return 3 plans (page size)");
        assertEquals(0, presenter.outputData.getCurrentPage());
        assertEquals(2, presenter.outputData.getTotalPages(), "5 plans / 3 per page = 2 pages");
        assertTrue(presenter.outputData.hasNextPage(), "Should have next page");
        assertFalse(presenter.outputData.hasPreviousPage(), "First page has no previous");
        assertEquals("user1", presenter.outputData.getUsername());
    }

    /**
     * Test pagination: Navigate to second page.
     */
    @Test
    void execute_requestSecondPage_returnsRemainingPlans() {
        // Arrange - Create 5 plans
        for (int i = 1; i <= 5; i++) {
            Plan plan = new Plan("plan" + i, "Plan " + i, "Description " + i, "user1");
            planDAO.save(plan);
        }

        ShowPlansInputData inputData = new ShowPlansInputData("user1", 1, 3);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.outputData);
        assertEquals(2, presenter.outputData.getPlans().size(), "Second page should have 2 remaining plans");
        assertEquals(1, presenter.outputData.getCurrentPage());
        assertFalse(presenter.outputData.hasNextPage(), "No more pages after this");
        assertTrue(presenter.outputData.hasPreviousPage(), "Should have previous page");
    }

    /**
     * Happy path: User has no plans yet.
     */
    @Test
    void execute_userHasNoPlans_returnsEmptyList() {
        // Arrange
        ShowPlansInputData inputData = new ShowPlansInputData("newuser", 0, 10);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);
        assertEquals(0, presenter.outputData.getPlans().size(), "Should return empty list");
        assertEquals(0, presenter.outputData.getTotalPages());
        assertFalse(presenter.outputData.hasNextPage());
        assertFalse(presenter.outputData.hasPreviousPage());
    }

    /**
     * Verify only user's own plans are returned, not other users' plans.
     */
    @Test
    void execute_multipleUsers_returnsOnlyCurrentUserPlans() {
        // Arrange - Create plans for different users
        Plan user1Plan1 = new Plan("u1p1", "User1 Plan1", "Description", "user1");
        Plan user1Plan2 = new Plan("u1p2", "User1 Plan2", "Description", "user1");
        Plan user2Plan1 = new Plan("u2p1", "User2 Plan1", "Description", "user2");
        Plan user2Plan2 = new Plan("u2p2", "User2 Plan2", "Description", "user2");

        planDAO.save(user1Plan1);
        planDAO.save(user1Plan2);
        planDAO.save(user2Plan1);
        planDAO.save(user2Plan2);

        ShowPlansInputData inputData = new ShowPlansInputData("user1", 0, 10);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.outputData);
        assertEquals(2, presenter.outputData.getPlans().size(), "Should only get user1's plans");

        // Verify all returned plans belong to user1
        for (Plan plan : presenter.outputData.getPlans()) {
            assertEquals("user1", plan.getUsername(), "All plans should belong to user1");
        }
    }

    /**
     * Test that plans contain all necessary information (name and description).
     */
    @Test
    void execute_plansReturned_containCompleteInformation() {
        // Arrange
        Plan plan = new Plan("plan1", "My Fitness Plan",
                "A comprehensive plan to improve physical fitness", "user1");
        planDAO.save(plan);

        ShowPlansInputData inputData = new ShowPlansInputData("user1", 0, 10);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.outputData);
        assertEquals(1, presenter.outputData.getPlans().size());

        Plan returnedPlan = presenter.outputData.getPlans().get(0);
        assertEquals("plan1", returnedPlan.getId());
        assertEquals("user1", returnedPlan.getUsername());
        assertEquals("My Fitness Plan", returnedPlan.getName());
        assertEquals("A comprehensive plan to improve physical fitness", returnedPlan.getDescription());
    }

    /**
     * Test pagination boundary: Exact multiple of page size.
     */
    @Test
    void execute_exactMultipleOfPageSize_calculatesPageCorrectly() {
        // Arrange - Create exactly 9 plans (3 pages of 3)
        for (int i = 1; i <= 9; i++) {
            Plan plan = new Plan("plan" + i, "Plan " + i, "Description " + i, "user1");
            planDAO.save(plan);
        }

        ShowPlansInputData inputData = new ShowPlansInputData("user1", 0, 3);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.outputData);
        assertEquals(3, presenter.outputData.getTotalPages(), "9 plans / 3 per page = exactly 3 pages");
        assertTrue(presenter.outputData.hasNextPage());
    }

    /**
     * Test last page: Should not have next page.
     */
    @Test
    void execute_lastPage_hasNoNextPage() {
        // Arrange - Create 10 plans
        for (int i = 1; i <= 10; i++) {
            Plan plan = new Plan("plan" + i, "Plan " + i, "Description " + i, "user1");
            planDAO.save(plan);
        }

        // Request page 2 (last page) with page size 5
        ShowPlansInputData inputData = new ShowPlansInputData("user1", 1, 5);

        // Act
        interactor.execute(inputData);

        // Assert
        assertNotNull(presenter.outputData);
        assertEquals(1, presenter.outputData.getCurrentPage());
        assertFalse(presenter.outputData.hasNextPage(), "Last page should have no next page");
        assertTrue(presenter.outputData.hasPreviousPage(), "Last page should have previous page");
    }

    /**
     * Test error handling when data access throws exception.
     */
    @Test
    void execute_dataAccessThrowsException_presentsError() {
        // Arrange
        FailingPlanDAO failingDAO = new FailingPlanDAO();
        ShowPlansInteractor failingInteractor = new ShowPlansInteractor(failingDAO, presenter);
        ShowPlansInputData inputData = new ShowPlansInputData("user1", 0, 10);

        // Act
        failingInteractor.execute(inputData);

        // Assert
        assertNotNull(presenter.errorMessage, "Error should be presented");
        assertTrue(presenter.errorMessage.contains("Failed to load plans"));
        assertNull(presenter.outputData, "No success output on error");
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory plan DAO for testing.
     * Uses LinkedHashMap to maintain insertion order for consistent pagination.
     */
    private static class InMemoryPlanDAO implements data_access.interfaces.plan.ShowPlansDataAccessInterface {
        private final Map<String, Plan> storage = new java.util.LinkedHashMap<>();

        void save(Plan plan) {
            storage.put(plan.getId(), plan);
        }

        @Override
        public List<Plan> getPlansByUsername(String username) {
            return storage.values().stream()
                    .filter(plan -> plan.getUsername().equals(username))
                    .collect(Collectors.toList());
        }

        @Override
        public List<Plan> getPlansByUsername(String username, int page, int pageSize) {
            List<Plan> userPlans = storage.values().stream()
                    .filter(plan -> plan.getUsername().equals(username))
                    .collect(Collectors.toList());

            int start = page * pageSize;
            int end = Math.min(start + pageSize, userPlans.size());

            if (start >= userPlans.size()) {
                return java.util.Collections.emptyList();
            }

            return userPlans.subList(start, end);
        }

        @Override
        public int getPlansCount(String username) {
            return (int) storage.values().stream()
                    .filter(plan -> plan.getUsername().equals(username))
                    .count();
        }
    }

    /**
     * Failing DAO to test error handling.
     */
    private static class FailingPlanDAO implements data_access.interfaces.plan.ShowPlansDataAccessInterface {
        @Override
        public List<Plan> getPlansByUsername(String username) {
            throw new RuntimeException("Database connection failed");
        }

        @Override
        public List<Plan> getPlansByUsername(String username, int page, int pageSize) {
            throw new RuntimeException("Database connection failed");
        }

        @Override
        public int getPlansCount(String username) {
            throw new RuntimeException("Database connection failed");
        }
    }

    /**
     * Test presenter that captures output.
     */
    private static class TestPresenter implements ShowPlansOutputBoundary {
        ShowPlansOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(ShowPlansOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToShowPlansView() {
            // No-op for testing
        }
    }
}

