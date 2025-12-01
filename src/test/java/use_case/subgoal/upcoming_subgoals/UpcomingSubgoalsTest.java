package use_case.subgoal.upcoming_subgoals;

import entity.subgoal.Subgoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Upcoming Subgoals functionality.
 * Tests User Story 5: As a user, I want to be able to see the list of upcoming subgoals
 * in the list format, so that I don't forget about them and easily mark them as completed.
 *
 * This test validates the data access layer's ability to retrieve and filter subgoals
 * for the CalendarView's upcoming subgoals display.
 */
public class UpcomingSubgoalsTest {

    private InMemorySubgoalDAO subgoalDAO;

    @BeforeEach
    void setUp() {
        subgoalDAO = new InMemorySubgoalDAO();
    }

    /**
     * Happy path: User has upcoming subgoals that are not completed.
     */
    @Test
    void getUpcomingSubgoals_userHasIncompleteUpcomingSubgoals_returnsAll() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);

        Subgoal subgoal1 = new Subgoal("sub1", "plan1", "user1", "Study for exam",
                "Review chapters 1-5", tomorrow, false, true);
        Subgoal subgoal2 = new Subgoal("sub2", "plan1", "user1", "Submit assignment",
                "Complete CS homework", nextWeek, false, false);
        Subgoal subgoal3 = new Subgoal("sub3", "plan2", "user1", "Gym workout",
                "Upper body day", today, false, true);

        subgoalDAO.save(subgoal1);
        subgoalDAO.save(subgoal2);
        subgoalDAO.save(subgoal3);

        // Act
        List<Subgoal> upcoming = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(3, upcoming.size(), "Should return all 3 incomplete upcoming subgoals");

        // Verify they are sorted by deadline
        assertEquals("sub3", upcoming.get(0).getId(), "Today's subgoal should be first");
        assertEquals("sub1", upcoming.get(1).getId(), "Tomorrow's subgoal should be second");
        assertEquals("sub2", upcoming.get(2).getId(), "Next week's subgoal should be last");
    }

    /**
     * Verify completed subgoals are NOT shown in upcoming list (can be included with flag).
     */
    @Test
    void getUpcomingSubgoals_hasCompletedSubgoals_canBeFilteredOrIncluded() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Subgoal incomplete = new Subgoal("sub1", "plan1", "user1", "Incomplete task",
                "Not done yet", tomorrow, false, false);
        Subgoal completed = new Subgoal("sub2", "plan1", "user1", "Completed task",
                "Already done", tomorrow, true, false);

        subgoalDAO.save(incomplete);
        subgoalDAO.save(completed);

        // Act - Get only incomplete
        List<Subgoal> upcomingIncomplete = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(1, upcomingIncomplete.size(), "Should only return incomplete subgoals");
        assertEquals("sub1", upcomingIncomplete.get(0).getId());
        assertFalse(upcomingIncomplete.get(0).isCompleted());
    }

    /**
     * Test that completed subgoals can still be displayed (for CalendarView with green background).
     */
    @Test
    void getUpcomingSubgoals_includeCompleted_showsCompletedWithGreenIndicator() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Subgoal completed = new Subgoal("sub1", "plan1", "user1", "Completed task",
                "Done!", tomorrow, true, false);

        subgoalDAO.save(completed);

        // Act - Get all subgoals including completed
        List<Subgoal> allUpcoming = getUpcomingSubgoalsIncludingCompleted("user1", today);

        // Assert
        assertEquals(1, allUpcoming.size());
        assertTrue(allUpcoming.get(0).isCompleted(), "Completed subgoal should have completed flag");
        // In the view, this would be displayed with green background
    }

    /**
     * Verify past subgoals are NOT shown in upcoming list.
     */
    @Test
    void getUpcomingSubgoals_hasPastSubgoals_excludesThem() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastWeek = today.minusDays(7);
        LocalDate tomorrow = today.plusDays(1);

        Subgoal pastSubgoal1 = new Subgoal("sub1", "plan1", "user1", "Past task 1",
                "Missed deadline", yesterday, false, false);
        Subgoal pastSubgoal2 = new Subgoal("sub2", "plan1", "user1", "Past task 2",
                "Old deadline", lastWeek, false, false);
        Subgoal futureSubgoal = new Subgoal("sub3", "plan1", "user1", "Future task",
                "Upcoming", tomorrow, false, false);

        subgoalDAO.save(pastSubgoal1);
        subgoalDAO.save(pastSubgoal2);
        subgoalDAO.save(futureSubgoal);

        // Act
        List<Subgoal> upcoming = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(1, upcoming.size(), "Should only include future and today's subgoals");
        assertEquals("sub3", upcoming.get(0).getId());
        assertEquals(tomorrow, upcoming.get(0).getDeadline());
    }

    /**
     * Verify priority subgoals are properly flagged.
     */
    @Test
    void getUpcomingSubgoals_hasPrioritySubgoals_flagsThemCorrectly() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Subgoal prioritySubgoal = new Subgoal("sub1", "plan1", "user1", "Important task",
                "High priority", tomorrow, false, true);
        Subgoal normalSubgoal = new Subgoal("sub2", "plan1", "user1", "Normal task",
                "Regular priority", tomorrow, false, false);

        subgoalDAO.save(prioritySubgoal);
        subgoalDAO.save(normalSubgoal);

        // Act
        List<Subgoal> upcoming = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(2, upcoming.size());

        // Find the priority subgoal
        Subgoal priority = upcoming.stream()
                .filter(s -> s.getId().equals("sub1"))
                .findFirst()
                .orElseThrow();

        assertTrue(priority.isPriority(), "Priority flag should be set");
        // In the view, this would be displayed with [PRIORITY] tag
    }

    /**
     * Test pagination: Show next 6 subgoals (CalendarView shows 6 at a time).
     */
    @Test
    void getUpcomingSubgoals_hasMoreThanSix_canPaginate() {
        // Arrange
        LocalDate today = LocalDate.now();

        // Create 10 upcoming subgoals
        for (int i = 1; i <= 10; i++) {
            Subgoal subgoal = new Subgoal("sub" + i, "plan1", "user1", "Task " + i,
                    "Description", today.plusDays(i), false, false);
            subgoalDAO.save(subgoal);
        }

        // Act - Get first page (6 items)
        List<Subgoal> page1 = getUpcomingSubgoalsPage("user1", today, 0, 6);

        // Act - Get second page (4 remaining items)
        List<Subgoal> page2 = getUpcomingSubgoalsPage("user1", today, 1, 6);

        // Assert
        assertEquals(6, page1.size(), "First page should have 6 subgoals");
        assertEquals(4, page2.size(), "Second page should have 4 remaining subgoals");

        // Verify no overlap
        Set<String> page1Ids = page1.stream().map(Subgoal::getId).collect(Collectors.toSet());
        Set<String> page2Ids = page2.stream().map(Subgoal::getId).collect(Collectors.toSet());
        assertTrue(Collections.disjoint(page1Ids, page2Ids), "Pages should not overlap");
    }

    /**
     * Verify only user's own subgoals are returned.
     */
    @Test
    void getUpcomingSubgoals_multipleUsers_returnsOnlyCurrentUserSubgoals() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Subgoal user1Subgoal = new Subgoal("sub1", "plan1", "user1", "User1 Task",
                "Description", tomorrow, false, false);
        Subgoal user2Subgoal = new Subgoal("sub2", "plan2", "user2", "User2 Task",
                "Description", tomorrow, false, false);

        subgoalDAO.save(user1Subgoal);
        subgoalDAO.save(user2Subgoal);

        // Act
        List<Subgoal> user1Upcoming = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(1, user1Upcoming.size(), "Should only get user1's subgoals");
        assertEquals("user1", user1Upcoming.get(0).getUsername());
    }

    /**
     * Test marking subgoal as completed updates the list.
     */
    @Test
    void markSubgoalAsCompleted_removesFromIncompleteList() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Subgoal subgoal = new Subgoal("sub1", "plan1", "user1", "Task to complete",
                "Description", tomorrow, false, false);
        subgoalDAO.save(subgoal);

        // Act - Before marking complete
        List<Subgoal> beforeComplete = getUpcomingSubgoalsForUser("user1", today);

        // Mark as completed
        subgoalDAO.updateCompleted("sub1", true);

        // Act - After marking complete
        List<Subgoal> afterComplete = getUpcomingSubgoalsForUser("user1", today);

        // Assert
        assertEquals(1, beforeComplete.size(), "Should have 1 incomplete subgoal before");
        assertEquals(0, afterComplete.size(), "Should have 0 incomplete subgoals after completion");
    }

    /**
     * Test empty state: User has no upcoming subgoals.
     */
    @Test
    void getUpcomingSubgoals_userHasNoSubgoals_returnsEmptyList() {
        // Arrange
        LocalDate today = LocalDate.now();

        // Act
        List<Subgoal> upcoming = getUpcomingSubgoalsForUser("newuser", today);

        // Assert
        assertEquals(0, upcoming.size(), "Should return empty list for user with no subgoals");
        assertNotNull(upcoming, "Should return non-null list");
    }

    // =====================================================================
    // Helper methods (simulating CalendarView logic)
    // =====================================================================

    /**
     * Get upcoming incomplete subgoals for a user, sorted by deadline.
     */
    private List<Subgoal> getUpcomingSubgoalsForUser(String username, LocalDate today) {
        List<Subgoal> allSubgoals = subgoalDAO.getSubgoalsByUsername(username);

        return allSubgoals.stream()
                .filter(s -> !s.isCompleted())
                .filter(s -> !s.getDeadline().isBefore(today))
                .sorted(Comparator.comparing(Subgoal::getDeadline))
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming subgoals including completed ones (for green background display).
     */
    private List<Subgoal> getUpcomingSubgoalsIncludingCompleted(String username, LocalDate today) {
        List<Subgoal> allSubgoals = subgoalDAO.getSubgoalsByUsername(username);

        return allSubgoals.stream()
                .filter(s -> !s.getDeadline().isBefore(today))
                .sorted(Comparator.comparing(Subgoal::getDeadline)
                        .thenComparing(Subgoal::isCompleted))
                .collect(Collectors.toList());
    }

    /**
     * Get paginated upcoming subgoals (CalendarView shows 6 at a time).
     */
    private List<Subgoal> getUpcomingSubgoalsPage(String username, LocalDate today, int page, int pageSize) {
        List<Subgoal> allUpcoming = getUpcomingSubgoalsForUser(username, today);

        int start = page * pageSize;
        int end = Math.min(start + pageSize, allUpcoming.size());

        if (start >= allUpcoming.size()) {
            return new ArrayList<>();
        }

        return allUpcoming.subList(start, end);
    }

    // =====================================================================
    // Test doubles
    // =====================================================================

    /**
     * In-memory subgoal DAO for testing.
     * This is a simple test double that doesn't implement any interface.
     */
    private static class InMemorySubgoalDAO {
        private final Map<String, Subgoal> storage = new HashMap<>();

        void save(Subgoal subgoal) {
            storage.put(subgoal.getId(), subgoal);
        }

        Subgoal getSubgoalById(String id) {
            return storage.get(id);
        }

        void updateCompleted(String id, boolean completed) {
            Subgoal old = storage.get(id);
            if (old != null) {
                Subgoal updated = new Subgoal(
                        old.getId(),
                        old.getPlanId(),
                        old.getUsername(),
                        old.getName(),
                        old.getDescription(),
                        old.getDeadline(),
                        completed,
                        old.isPriority()
                );
                storage.put(id, updated);
            }
        }

        List<Subgoal> getSubgoalsByUsername(String username) {
            return storage.values().stream()
                    .filter(s -> s.getUsername().equals(username))
                    .collect(Collectors.toList());
        }
    }
}

