package data_access;

import entity.Plan;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.delete_plan.DeletePlanDataAccessInterface;
import use_case.show_plans.ShowPlansDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the DAO for storing plan data.
 * Loads plans from a flat JSON array and filters by userId.
 */
public class InMemoryPlanDataAccessObject implements ShowPlansDataAccessInterface, DeletePlanDataAccessInterface {

    private final List<Plan> allPlans = new ArrayList<>();
    private final Map<String, List<Plan>> cachedUserPlans = new HashMap<>();
    private final String plansFilePath;

    /**
     * Constructs the data access object that loads plans from a JSON file.
     * @param plansDataFile path to the JSON file containing plans data, or null for demo data
     */
    public InMemoryPlanDataAccessObject(String plansDataFile) {
        this.plansFilePath = plansDataFile;
        if (plansDataFile != null) {
            try {
                loadPlansFromJson(plansDataFile);
            } catch (Exception e) {
                System.err.println("Could not load plans from file: " + e.getMessage());
                System.err.println("Will use demo data when needed.");
            }
        }
    }

    /**
     * Constructs the data access object with demo plans.
     */
    public InMemoryPlanDataAccessObject() {
        this(null);
    }

    /**
     * Loads plans from a JSON file.
     * Expected format: Array of plan objects
     * [
     *   {"planId": "...", "name": "...", "description": "...", "userId": "..."},
     *   ...
     * ]
     * @param filePath path to the JSON file
     */
    private void loadPlansFromJson(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray plansArray = new JSONArray(jsonContent);

        for (int i = 0; i < plansArray.length(); i++) {
            try {
                JSONObject planObj = plansArray.getJSONObject(i);

                String planId = planObj.getString("planId");
                String name = planObj.getString("name");
                String description = planObj.getString("description");
                String userId = planObj.getString("userId");

                Plan plan = new Plan(planId, name, description, userId);
                allPlans.add(plan);
            } catch (Exception e) {
                System.err.println("Error parsing plan at index " + i + ": " + e.getMessage());
                // Skip this plan and continue with the next one
            }
        }
    }

    /**
     * Saves all plans to the JSON file.
     * This persists changes to disk.
     */
    private void savePlansToJson() {
        if (plansFilePath == null) {
            // No file path configured, can't save
            return;
        }

        try {
            JSONArray plansArray = new JSONArray();

            for (Plan plan : allPlans) {
                JSONObject planObj = new JSONObject();
                planObj.put("planId", plan.getPlanId());
                planObj.put("name", plan.getName());
                planObj.put("description", plan.getDescription());
                planObj.put("userId", plan.getUserId());
                plansArray.put(planObj);
            }

            // Write to file with proper formatting
            String jsonContent = plansArray.toString(2); // 2 spaces indentation
            Files.write(Paths.get(plansFilePath), jsonContent.getBytes());

            System.out.println("Plans saved to file: " + plansFilePath);
        } catch (IOException e) {
            System.err.println("Error saving plans to file: " + e.getMessage());
        }
    }

    /**
     * Initializes demo plans for a specific user.
     * Used when no JSON file is provided or file is empty.
     * @param userId the user ID (username)
     */
    private void initializeDemoPlansForUser(String userId) {
        // Create 8 demo plans for this user
        allPlans.add(new Plan("demo-001", "Learn Guitar", "Master guitar playing in 6 months", userId));
        allPlans.add(new Plan("demo-002", "Prepare for Exam", "Study for final exams in Computer Science", userId));
        allPlans.add(new Plan("demo-003", "Build Portfolio", "Create a professional portfolio website", userId));
        allPlans.add(new Plan("demo-004", "Learn Spanish", "Become conversational in Spanish", userId));
        allPlans.add(new Plan("demo-005", "Fitness Goal", "Run a half marathon by summer", userId));
        allPlans.add(new Plan("demo-006", "Read 12 Books", "Read one book per month this year", userId));
        allPlans.add(new Plan("demo-007", "Learn Cooking", "Master 20 new recipes", userId));
        allPlans.add(new Plan("demo-008", "Side Project", "Build and launch a mobile app", userId));
    }

    /**
     * Gets all plans for a specific user by filtering on userId.
     * @param userId the user ID (username) to filter by
     * @return list of plans belonging to this user
     */
    private List<Plan> getPlansForUser(String userId) {
        // Check cache first
        if (cachedUserPlans.containsKey(userId)) {
            return cachedUserPlans.get(userId);
        }

        // Filter allPlans by userId
        List<Plan> userPlans = allPlans.stream()
                .filter(plan -> plan.getUserId().equals(userId))
                .collect(Collectors.toList());

        // If no plans found and no plans loaded at all, initialize demo data
        if (userPlans.isEmpty() && allPlans.isEmpty()) {
            initializeDemoPlansForUser(userId);
            userPlans = allPlans.stream()
                    .filter(plan -> plan.getUserId().equals(userId))
                    .collect(Collectors.toList());
        }

        // Cache the result
        cachedUserPlans.put(userId, userPlans);
        return userPlans;
    }

    @Override
    public List<Plan> getPlansByUsername(String username) {
        return getPlansForUser(username);
    }

    @Override
    public List<Plan> getPlansByUsername(String username, int page, int pageSize) {
        final List<Plan> userPlans = getPlansForUser(username);
        final int start = page * pageSize;
        final int end = Math.min(start + pageSize, userPlans.size());

        if (start >= userPlans.size()) {
            return new ArrayList<>();
        }

        return userPlans.subList(start, end);
    }

    @Override
    public int getPlansCount(String username) {
        return getPlansForUser(username).size();
    }

    /**
     * Adds a plan for a user.
     * @param userId the user ID (username)
     * @param plan the plan to add
     */
    public void addPlan(String userId, Plan plan) {
        allPlans.add(plan);
        // Clear cache for this user so it gets rebuilt
        cachedUserPlans.remove(userId);
    }

    /**
     * Removes a plan by planId.
     * @param planId the plan ID to remove
     * @return true if the plan was removed, false otherwise
     */
    public boolean removePlan(String planId) {
        boolean removed = allPlans.removeIf(plan -> plan.getPlanId().equals(planId));
        if (removed) {
            // Clear all caches since we don't know which user it belonged to
            cachedUserPlans.clear();
        }
        return removed;
    }

    /**
     * Gets a plan by its ID.
     * @param planId the plan ID
     * @return the plan, or null if not found
     */
    public Plan getPlanById(String planId) {
        return allPlans.stream()
                .filter(plan -> plan.getPlanId().equals(planId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deletePlan(String planId) {
        boolean removed = allPlans.removeIf(plan -> plan.getPlanId().equals(planId));
        if (removed) {
            // Clear all caches since we don't know which user it belonged to
            cachedUserPlans.clear();
            // Save changes to the JSON file
            savePlansToJson();
        }
        return removed;
    }
}

