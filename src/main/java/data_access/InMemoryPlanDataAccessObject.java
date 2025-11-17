package data_access;

import entity.Plan;
import use_case.show_plans.ShowPlansDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing plan data.
 * This implementation does NOT persist data between runs of the program.
 */
public class InMemoryPlanDataAccessObject implements ShowPlansDataAccessInterface {

    private final Map<String, List<Plan>> userPlans = new HashMap<>();

    /**
     * Constructs the data access object with demo plans.
     */
    public InMemoryPlanDataAccessObject() {
        // Demo plans will be initialized on first access
    }

    @Override
    public List<Plan> getPlansByUsername(String username) {
        // Add demo plans if this user doesn't have any yet
        if (!userPlans.containsKey(username)) {
            initializeDemoPlansForUser(username);
        }
        return userPlans.getOrDefault(username, new ArrayList<>());
    }

    /**
     * Initializes demo plans for a specific user.
     * @param username the username
     */
    private void initializeDemoPlansForUser(String username) {
        final List<Plan> demoPlans = new ArrayList<>();

        // Create 8 demo plans to test pagination
        demoPlans.add(new Plan("Learn Guitar", "Master guitar playing in 6 months", 1));
        demoPlans.add(new Plan("Prepare for Exam", "Study for final exams in Computer Science", 1));
        demoPlans.add(new Plan("Build Portfolio", "Create a professional portfolio website", 1));
        demoPlans.add(new Plan("Learn Spanish", "Become conversational in Spanish", 1));
        demoPlans.add(new Plan("Fitness Goal", "Run a half marathon by summer", 1));
        demoPlans.add(new Plan("Read 12 Books", "Read one book per month this year", 1));
        demoPlans.add(new Plan("Learn Cooking", "Master 20 new recipes", 1));
        demoPlans.add(new Plan("Side Project", "Build and launch a mobile app", 1));

        userPlans.put(username, demoPlans);
    }

    @Override
    public List<Plan> getPlansByUsername(String username, int page, int pageSize) {
        final List<Plan> allPlans = getPlansByUsername(username);
        final int start = page * pageSize;
        final int end = Math.min(start + pageSize, allPlans.size());

        if (start >= allPlans.size()) {
            return new ArrayList<>();
        }

        return allPlans.subList(start, end);
    }

    @Override
    public int getPlansCount(String username) {
        return getPlansByUsername(username).size();
    }

    /**
     * Adds a plan for a user.
     * @param username the username
     * @param plan the plan to add
     */
    public void addPlan(String username, Plan plan) {
        userPlans.computeIfAbsent(username, k -> new ArrayList<>()).add(plan);
    }

    /**
     * Removes a plan for a user.
     * @param username the username
     * @param planId the plan ID to remove
     * @return true if the plan was removed, false otherwise
     */
    public boolean removePlan(String username, String planId) {
        final List<Plan> plans = userPlans.get(username);
        if (plans != null) {
            return plans.removeIf(plan -> plan.getId().equals(planId));
        }
        return false;
    }
}

