package data_access.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import data_access.interfaces.plan.DeletePlanDataAccessInterface;
import data_access.interfaces.plan.SavePlanDataAccessInterface;
import data_access.interfaces.plan.ShowPlanDataAccessInterface;
import data_access.interfaces.plan.ShowPlansDataAccessInterface;
import entity.plan.Plan;

/**
 * FilePlanDataAccessObject provides file-based persistence for plans.
 * It can load plans from a JSON file, cache them in memory,
 * and save changes back to disk.
 */
public class FilePlanDataAccessObject implements ShowPlansDataAccessInterface,
        DeletePlanDataAccessInterface, SavePlanDataAccessInterface, ShowPlanDataAccessInterface {

    private final List<Plan> allPlans = new ArrayList<>();
    private final Map<String, List<Plan>> cachedUserPlans = new HashMap<>();
    private final String plansFilePath;

    /**
     * Constructs a FilePlanDataAccessObject that loads plans from a JSON file if provided.
     *
     * @param plansDataFile path to the JSON file containing plans data, or null for demo data
     */
    public FilePlanDataAccessObject(String plansDataFile) {
        this.plansFilePath = plansDataFile;
        if (plansDataFile != null) {
            try {
                loadPlansFromJson(plansDataFile);
            }
            catch (Exception ex) {
                System.err.println("Could not load plans from file: " + ex.getMessage());
            }
        }
    }

    /**
     * Constructs a FilePlanDataAccessObject without an initial JSON file
     * and uses demo data when needed.
     */
    public FilePlanDataAccessObject() {
        this(null);
    }

    /**
     * Loads plans from a JSON file.
     * Expected format: array of plan objects with fields id, name, description, and username.
     *
     * @param filePath path to the JSON file
     * @throws IOException if the file cannot be read
     */
    private void loadPlansFromJson(String filePath) throws IOException {
        final String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        final JSONArray plansArray = new JSONArray(jsonContent);
        for (int i = 0; i < plansArray.length(); i++) {
            try {
                final JSONObject planObj = plansArray.getJSONObject(i);
                final String planId = planObj.getString("id");
                final String name = planObj.getString("name");
                final String description = planObj.getString("description");
                final String username = planObj.getString("username");
                final Plan plan = new Plan(planId, name, description, username);
                allPlans.add(plan);
            }
            catch (Exception ex) {
                System.err.println(
                        "Error parsing plan at index " + i + ": " + ex.getMessage());
            }
        }
    }

    /**
     * Saves all plans in memory to the JSON file specified by plansFilePath.
     * If no path was provided, this method does nothing.
     */
    private void savePlansToJson() {
        if (plansFilePath != null) {
            try {
                final JSONArray plansArray = new JSONArray();
                for (Plan plan : allPlans) {
                    final JSONObject planObj = new JSONObject();
                    planObj.put("id", plan.getId());
                    planObj.put("name", plan.getName());
                    planObj.put("description", plan.getDescription());
                    planObj.put("username", plan.getUsername());
                    plansArray.put(planObj);
                }
                final String jsonContent = plansArray.toString(2);
                Files.write(Paths.get(plansFilePath), jsonContent.getBytes());
                System.out.println("Plans saved to file: " + plansFilePath);
            }
            catch (IOException ex) {
                System.err.println("Error saving plans to file: " + ex.getMessage());
            }
        }
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
            savePlansToJson();
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
        savePlansToJson();
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
