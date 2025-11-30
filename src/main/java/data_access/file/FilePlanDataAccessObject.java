package data_access.file;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import data_access.interfaces.file.JsonDataAccess;
import org.json.JSONObject;

import data_access.interfaces.plan.DeletePlanDataAccessInterface;
import data_access.interfaces.plan.SavePlanDataAccessInterface;
import data_access.interfaces.plan.ShowPlanDataAccessInterface;
import data_access.interfaces.plan.ShowPlansDataAccessInterface;
import entity.plan.Plan;
import use_case.edit_plan.EditPlanDataAccessInterface;

/**
 * FilePlanDataAccessObject provides file-based persistence for plans.
 * It can load plans from a JSON file, cache them in memory,
 * and save changes back to disk.
 */
public class FilePlanDataAccessObject extends JsonDataAccess<Plan> implements ShowPlansDataAccessInterface,
        DeletePlanDataAccessInterface, SavePlanDataAccessInterface, ShowPlanDataAccessInterface,
        EditPlanDataAccessInterface {

    private final List<Plan> allPlans = new ArrayList<>();
    private final Map<String, List<Plan>> cachedUserPlans = new HashMap<>();

    /**
     * Constructs a FilePlanDataAccessObject that loads plans from a JSON file if provided.
     *
     * @param plansDataFile path to the JSON file containing plans data, or null for demo data
     */
    public FilePlanDataAccessObject(String plansDataFile) {
        super(plansDataFile);
        if (plansDataFile != null) {
            try {
                loadFromJson(allPlans);
            }
            catch (Exception ex) {
                System.err.println("Could not load plans from file: " + ex.getMessage());
            }
        }
    }

    @Override
    public Plan parseJsonObject(JSONObject jsonObject) {
        final String planId = jsonObject.getString("id");
        final String name = jsonObject.getString("name");
        final String description = jsonObject.getString("description");
        final String username = jsonObject.getString("username");
        return new Plan(planId, name, description, username);
    }

    @Override
    public JSONObject convertObjectToJson(Plan object) {
        final JSONObject planObj = new JSONObject();
        planObj.put("id", object.getId());
        planObj.put("name", object.getName());
        planObj.put("description", object.getDescription());
        planObj.put("username", object.getUsername());
        return planObj;
    }

    /**
     * Constructs a FilePlanDataAccessObject without an initial JSON file
     * and uses demo data when needed.
     */
    public FilePlanDataAccessObject() {
        this(null);
    }

    /**
     * Initializes demo plans in memory for the given user.
     * Used when no JSON file is provided or when no plans exist.
     *
     * @param username the username to associate with the demo plans
     */
    private void initializeDemoPlansForUser(String username) {
        allPlans.add(new Plan(
                "demo-001",
                "Learn Guitar",
                "Master guitar playing in 6 months",
                username
        ));
        allPlans.add(new Plan(
                "demo-002",
                "Prepare for Exam",
                "Study for final exams in Computer Science",
                username
        ));
        allPlans.add(new Plan(
                "demo-003",
                "Build Portfolio",
                "Create a professional portfolio website",
                username
        ));
        allPlans.add(new Plan(
                "demo-004",
                "Learn Spanish",
                "Become conversational in Spanish",
                username
        ));
        allPlans.add(new Plan(
                "demo-005",
                "Fitness Goal",
                "Run a half marathon by summer",
                username
        ));
        allPlans.add(new Plan(
                "demo-006",
                "Read 12 Books",
                "Read one book per month this year",
                username
        ));
        allPlans.add(new Plan(
                "demo-007",
                "Learn Cooking",
                "Master 20 new recipes",
                username
        ));
        allPlans.add(new Plan(
                "demo-008",
                "Side Project",
                "Build and launch a mobile app",
                username
        ));
    }

    /**
     * Returns all plans belonging to the given user, using an in-memory cache.
     *
     * @param username the username whose plans are requested
     * @return a list of plans for the given user
     */
    private List<Plan> getPlansForUser(String username) {
        if (cachedUserPlans.containsKey(username)) {
            return cachedUserPlans.get(username);
        }
        final List<Plan> userPlans = allPlans.stream()
                .filter(plan -> plan.getUsername().equals(username))
                .collect(Collectors.toList());
        cachedUserPlans.put(username, userPlans);
        return userPlans;
    }

    /**
     * Returns all plans for the given username.
     *
     * @param username the username whose plans are requested
     * @return a list of all plans belonging to the user
     */
    @Override
    public List<Plan> getPlansByUsername(String username) {
        return getPlansForUser(username);
    }

    /**
     * Returns a paginated list of plans for the given username.
     *
     * @param username the username whose plans are requested
     * @param page     the zero-based page index
     * @param pageSize the number of plans per page
     * @return a sublist of the user's plans for the requested page,
     *         or an empty list if out of range
     */
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

    /**
     * Returns the number of plans for the given username.
     *
     * @param username the username whose plan count is requested
     * @return the number of plans belonging to the user
     */
    @Override
    public int getPlansCount(String username) {
        return getPlansForUser(username).size();
    }

    /**
     * Removes a plan with the given plan ID from memory and clears the cache.
     * This method does not persist changes to disk.
     *
     * @param planId the ID of the plan to remove
     * @return true if a plan was removed, false otherwise
     */
    public boolean removePlan(String planId) {
        final boolean removed = allPlans.removeIf(plan -> plan.getId().equals(planId));
        if (removed) {
            cachedUserPlans.clear();
        }
        return removed;
    }

    /**
     * Returns a plan with the given ID, or null if no such plan exists.
     *
     * @param planId the ID of the plan to find
     * @return the matching plan or null if not found
     */
    public Plan getPlanById(String planId) {
        return allPlans.stream()
                .filter(plan -> plan.getId().equals(planId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updatePlan(Plan plan) {
        Plan existingPlan = getPlanById(plan.getId());
        if (allPlans.contains(existingPlan)) {
            allPlans.remove(existingPlan);
        }
        allPlans.add(plan);
        saveToJson(allPlans);
    }

    /**
     * Deletes a plan with the given ID, clears the cache, and saves changes to disk.
     *
     * @param planId the ID of the plan to delete
     * @return true if the plan was deleted, false otherwise
     */
    @Override
    public boolean deletePlan(String planId) {
        final boolean removed = allPlans.removeIf(plan -> plan.getId().equals(planId));
        if (removed) {
            cachedUserPlans.clear();
            saveToJson(allPlans);
        }
        return removed;
    }

    /**
     * Saves a new plan or appends it to the in-memory list and persists to disk.
     * Also invalidates the cache for that user's plans.
     *
     * @param plan the plan to save
     */
    @Override
    public void savePlan(Plan plan) {
        allPlans.add(plan);
        cachedUserPlans.remove(plan.getUsername());
        saveToJson(allPlans);
    }

    /**
     * Checks whether a plan with the given name already exists.
     *
     * @param planName the name of the plan to check
     * @return true if a plan with the given name exists, false otherwise
     */
    @Override
    public boolean planExists(String planName) {
        return allPlans.stream()
                .filter(plan -> plan.getName().equals(planName))
                .findFirst()
                .orElse(null) != null;
    }
}
