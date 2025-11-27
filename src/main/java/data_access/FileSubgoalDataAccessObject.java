package data_access;

import entity.plan.Plan;
import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalBuilder;
import entity.subgoal.SubgoalFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple in-memory implementation of SubgoalDataAccessInterface.
 * <p>
 * This class stores Subgoal entities in a Map keyed by their ID.
 * It can be used as a placeholder until a file- or DB-backed DAO
 * is implemented by the team.
 */
public class FileSubgoalDataAccessObject implements SubgoalDataAccessInterface {
    private final String subgoalsFilePath;
    private final SubgoalBuilder subgoalBuilder;
    private final HashMap<String, Subgoal> subgoals = new HashMap();

    /**
     * Constructs an empty SubgoalDataAccessObject.
     */
    public FileSubgoalDataAccessObject(String subgoalsFilePath, SubgoalBuilder subgoalBuilder) {
        this.subgoalsFilePath = subgoalsFilePath;
        this.subgoalBuilder = subgoalBuilder;
        if (subgoalsFilePath != null) {
            try {
                loadSubgoalsFromJson(subgoalsFilePath);
            } catch (Exception e) {
                System.err.println("Could not load subgoals from file: " + e.getMessage());
                System.err.println("Will use demo data when needed.");
            }
        }
    }

    private void loadSubgoalsFromJson(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray subgoalsArray = new JSONArray(jsonContent);

        for (int i = 0; i < subgoalsArray.length(); i++) {
            try {
                JSONObject subgoalObj = subgoalsArray.getJSONObject(i);

                String subgoalId = subgoalObj.getString("id");
                String planId = subgoalObj.getString("plan_id");
                String username = subgoalObj.getString("username");
                String name = subgoalObj.getString("name");
                String description = subgoalObj.getString("description");
                LocalDate date = LocalDate.parse(subgoalObj.getString("deadline"));
                boolean isPriority = subgoalObj.getBoolean("priority");
                boolean isCompleted = subgoalObj.getBoolean("completed");
                Subgoal subgoal = subgoalBuilder
                        .setId(subgoalId)
                        .setPlanId(planId)
                        .setUsername(username)
                        .setName(name)
                        .setDescription(description)
                        .setDeadline(date)
                        .setPriority(isPriority)
                        .setIsCompleted(isCompleted)
                        .build();
                subgoals.put(subgoalId, subgoal);
            } catch (Exception e) {
                System.err.println("Error parsing subgoal at index " + i + ": " + e.getMessage());
                // Skip this plan and continue with the next one
            }
        }
    }

    @Override
    public void save() {
        if (subgoalsFilePath == null) {
            // No file path configured, can't save
            return;
        }

        try {
            JSONArray subgoalsArray = new JSONArray();
            for (Subgoal subgoal : subgoals.values()) {
                JSONObject subgoalObj = new JSONObject();
                subgoalObj.put("id", subgoal.getId());
                subgoalObj.put("plan_id", subgoal.getPlanId());
                subgoalObj.put("username", subgoal.getUsername());
                subgoalObj.put("name", subgoal.getName());
                subgoalObj.put("description", subgoal.getDescription());
                subgoalObj.put("deadline", subgoal.getDeadline());
                subgoalObj.put("priority", subgoal.isPriority());
                subgoalObj.put("completed", subgoal.isCompleted());
                subgoalsArray.put(subgoalObj);
            }

            // Write to file with proper formatting
            String jsonContent = subgoalsArray.toString(2); // 2 spaces indentation
            Files.write(Paths.get(subgoalsFilePath), jsonContent.getBytes());
            System.out.println("Subgoals saved to file: " + subgoalsFilePath);
        } catch (IOException e) {
            System.err.println("Error saving subgoals to file: " + e.getMessage());
        }
    }
    /**
     * Adds or replaces a subgoal in the in-memory map.
     * This is mainly useful for seeding data in AppBuilder.
     *
     * @param subgoal the subgoal to store
     */
    public void save(Subgoal subgoal) {
        subgoals.put(subgoal.getId(), subgoal);
        this.save();
    }

    @Override
    public Subgoal getSubgoalById(String id) {
        return subgoals.get(id);
    }

    @Override
    public void updatePriority(String id, boolean priority) {
        Subgoal old = subgoals.get(id);
        if (old == null) {
            return;
        }
        // Subgoal is immutable, so we create a new one with the updated priority.
        Subgoal updated = new Subgoal(
                old.getId(),
                old.getPlanId(),
                old.getUsername(),
                old.getName(),
                old.getDescription(),
                old.getDeadline(),
                old.isCompleted(),
                priority
        );
        subgoals.put(id, updated);
        this.save();
    }

    @Override
    public void updateCompleted(String id, boolean completed) {
        Subgoal old = subgoals.get(id);
        if (old == null) {
            return;
        }

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

        subgoals.put(id, updated);
        this.save();
    }

    @Override
    public java.util.List<Subgoal> getSubgoalsByUsername(String username) {
        java.util.List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals.values()) {
            if (subgoal.getUsername().equals(username)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public void deleteSubgoal(String id) {
        subgoals.remove(id);
        this.save();
    }

    @Override
    public java.util.List<Subgoal> getSubgoalsByPlanId(String planId) {
        java.util.List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals.values()) {
            if (subgoal.getPlanId().equals(planId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

}
