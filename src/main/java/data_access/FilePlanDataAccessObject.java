package data_access;

import entity.plan.Plan;
import entity.subgoal.Subgoal;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.plan.delete_plan.DeletePlanDataAccessInterface;
import use_case.plan.save_plan.SavePlanDataAccessInterface;
import use_case.plan.show_plan.ShowPlanDataAccessInterface;
import use_case.plan.show_plans.ShowPlansDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilePlanDataAccessObject implements ShowPlansDataAccessInterface,
        DeletePlanDataAccessInterface, SavePlanDataAccessInterface, ShowPlanDataAccessInterface {

    private final List<Plan> allPlans = new ArrayList<>();
    private final Map<String, List<Plan>> cachedUserPlans = new HashMap<>();
    private final String plansFilePath;

    public FilePlanDataAccessObject(String plansDataFile) {
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

    public FilePlanDataAccessObject() {
        this(null);
    }

    private void loadPlansFromJson(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray plansArray = new JSONArray(jsonContent);

        for (int i = 0; i < plansArray.length(); i++) {
            try {
                JSONObject planObj = plansArray.getJSONObject(i);

                String planId = planObj.getString("id");
                String name = planObj.getString("name");
                String description = planObj.getString("description");
                String username = planObj.getString("username");

                Plan plan = new Plan(planId, name, description, username, new ArrayList<Subgoal>());
                allPlans.add(plan);
            } catch (Exception e) {
                System.err.println("Error parsing plan at index " + i + ": " + e.getMessage());
            }
        }
    }

    private void savePlansToJson() {
        if (plansFilePath == null) {
            return;
        }

        try {
            JSONArray plansArray = new JSONArray();
            for (Plan plan : allPlans) {
                JSONObject planObj = new JSONObject();
                planObj.put("id", plan.getId());
                planObj.put("name", plan.getName());
                planObj.put("description", plan.getDescription());
                planObj.put("username", plan.getUsername());
                plansArray.put(planObj);
            }

            String jsonContent = plansArray.toString(2);
            Files.write(Paths.get(plansFilePath), jsonContent.getBytes());

            System.out.println("Plans saved to file: " + plansFilePath);
        } catch (IOException e) {
            System.err.println("Error saving plans to file: " + e.getMessage());
        }
    }

    public void initializeDemoPlansForUser(String username) {
        allPlans.add(new Plan("demo-001", "Learn Guitar", "Master guitar playing in 6 months", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-002", "Prepare for Exam", "Study for final exams in Computer Science", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-003", "Build Portfolio", "Create a professional portfolio website", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-004", "Learn Spanish", "Become conversational in Spanish", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-005", "Fitness Goal", "Run a half marathon by summer", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-006", "Read 12 Books", "Read one book per month this year", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-007", "Learn Cooking", "Master 20 new recipes", username, new ArrayList<>()));
        allPlans.add(new Plan("demo-008", "Side Project", "Build and launch a mobile app", username, new ArrayList<>()));
    }

    private List<Plan> getPlansForUser(String username) {
        if (cachedUserPlans.containsKey(username)) {
            return cachedUserPlans.get(username);
        }

        List<Plan> userPlans = allPlans.stream()
                .filter(plan -> plan.getUsername().equals(username))
                .collect(Collectors.toList());

        cachedUserPlans.put(username, userPlans);
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

    public boolean removePlan(String planId) {
        boolean removed = allPlans.removeIf(plan -> plan.getId().equals(planId));
        if (removed) {
            cachedUserPlans.clear();
        }
        return removed;
    }

    public Plan getPlanById(String planId) {
        return allPlans.stream()
                .filter(plan -> plan.getId().equals(planId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deletePlan(String planId) {
        boolean removed = allPlans.removeIf(plan -> plan.getId().equals(planId));
        if (removed) {
            cachedUserPlans.clear();
            savePlansToJson();
        }
        return removed;
    }

    @Override
    public void savePlan(Plan plan) {
        allPlans.add(plan);
        cachedUserPlans.remove(plan.getUsername());
        savePlansToJson();
    }

    @Override
    public boolean planExists(String planName) {
        return allPlans.stream()
                .filter(plan -> plan.getName().equals(planName))
                .findFirst()
                .orElse(null) != null;
    }
}
