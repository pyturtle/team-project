package data_access.file;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalBuilder;

/**
 * FileSubgoalDataAccessObject is a file-based data access implementation for subgoals that keeps subgoals in memory
 * and persists them as JSON.
 */
public class FileSubgoalDataAccessObject extends JsonDataAccess<Subgoal>
        implements SubgoalDataAccessInterface {

    private final SubgoalBuilder subgoalBuilder;
    private final ArrayList<Subgoal> subgoals = new ArrayList<>();

    /**
     * Constructs a FileSubgoalDataAccessObject with an optional file path and builder.
     * @param subgoalsFilePath the path to the subgoals JSON file, or null to start empty
     * @param subgoalBuilder the builder used to construct Subgoal instances
     */
    public FileSubgoalDataAccessObject(String subgoalsFilePath,
                                       SubgoalBuilder subgoalBuilder) {
        super(subgoalsFilePath);
        this.subgoalBuilder = subgoalBuilder;
        if (subgoalsFilePath != null) {
            try {
                loadFromJson(subgoals);
            }
            // -@cs[IllegalCatch] log malformed or unreadable subgoal file but keep app running
            catch (Exception ex) {
                System.err.println(
                        "Could not load subgoals from file: " + ex.getMessage());
            }
        }
    }

    /**
     * Returns a subgoal with the given ID or null if none exists.
     * @param id the subgoal identifier
     * @return the matching Subgoal or null
     */
    @Override
    public Subgoal getSubgoalById(String id) {
        return subgoals.stream()
                .filter(subgoal -> subgoal.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the priority flag of the subgoal identified by the given ID and persists the change.
     * @param id the subgoal identifier
     * @param priority the new priority value
     */
    @Override
    public void updatePriority(String id, boolean priority) {
        final Subgoal old = subgoals.stream()
                .filter(subgoal -> subgoal.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (old == null) {
            return;
        }
        subgoals.remove(old);
        final Subgoal updated = subgoalBuilder
                .copyFromSubgoal(old)
                .setPriority(priority)
                .build();
        subgoals.add(updated);
        this.saveToJson(subgoals);
    }

    /**
     * Returns all subgoals for the given plan and user.
     * @param planId the plan identifier
     * @param userId the user identifier
     * @return a list of matching subgoals
     */
    @Override
    public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getPlanId().equals(planId)
                    && subgoal.getUsername().equals(userId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Returns all priority subgoals for the given user.
     * @param userId the user identifier
     * @return a list of priority subgoals
     */
    @Override
    public List<Subgoal> getPrioritySubgoals(String userId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && subgoal.isPriority()) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Returns all subgoals belonging to the given user.
     * @param userId the user identifier
     * @return a list of all subgoals for the user
     */
    @Override
    public List<Subgoal> getAllSubgoalsForUser(String userId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Saves or updates a subgoal in memory and writes all subgoals to the JSON file.
     * @param subgoal the subgoal to save
     */
    @Override
    public void saveSubgoal(Subgoal subgoal) {
        final Subgoal existing = getSubgoalById(subgoal.getId());
        if (existing != null) {
            subgoals.remove(existing);
        }
        subgoals.add(subgoal);
        this.saveToJson(subgoals);
    }

    /**
     * Updates the completed flag for the subgoal with the given ID and persists the change.
     * @param id the subgoal identifier
     * @param completed the new completed value
     */
    public void updateCompleted(String id, boolean completed) {
        final Subgoal old = getSubgoalById(id);
        if (old == null) {
            return;
        }
        subgoals.remove(old);
        final Subgoal updated = subgoalBuilder
                .copyFromSubgoal(old)
                .setIsCompleted(completed)
                .build();
        subgoals.add(updated);
        this.saveToJson(subgoals);
    }

    /**
     * Returns all subgoals for a user whose names contain the given search term.
     * @param name the name fragment to search for
     * @param userId the user identifier
     * @return a list of matching subgoals
     */
    public List<Subgoal> getSubgoalsByName(String name, String userId) {
        final String searchTerm = name.toLowerCase();
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId)
                    && subgoal.getName().toLowerCase().contains(searchTerm)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Returns all incomplete subgoals for the given user.
     * @param userId the user identifier
     * @return a list of incomplete subgoals
     */
    @Override
    public List<Subgoal> getIncompleteSubgoals(String userId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && !subgoal.isCompleted()) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Returns all completed subgoals for the given user.
     * @param userId the user identifier
     * @return a list of completed subgoals
     */
    @Override
    public List<Subgoal> getCompletedSubgoals(String userId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && subgoal.isCompleted()) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Returns all subgoals for the given username.
     * @param username the username to search for
     * @return a list of subgoals belonging to the user
     */
    @Override
    public List<Subgoal> getSubgoalsByUsername(String username) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(username)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Deletes the subgoal with the given ID and persists the change if it exists.
     * @param id the subgoal identifier
     */
    @Override
    public void deleteSubgoal(String id) {
        final Subgoal subgoal = getSubgoalById(id);
        if (subgoal == null) {
            return;
        }
        subgoals.remove(subgoal);
        this.saveToJson(subgoals);
    }

    /**
     * Returns all subgoals associated with the given plan ID.
     * @param planId the plan identifier
     * @return a list of subgoals for the plan
     */
    @Override
    public List<Subgoal> getSubgoalsByPlanId(String planId) {
        final List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getPlanId().equals(planId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    /**
     * Parses a JSON object into a Subgoal instance using the configured builder.
     * @param jsonObject the JSON representation of a subgoal
     * @return a Subgoal created from the JSON data
     */
    @Override
    public Subgoal parseJsonObject(JSONObject jsonObject) {
        final String subgoalId = jsonObject.getString("id");
        final String planId = jsonObject.getString("plan_id");
        final String username = jsonObject.getString("username");
        final String name = jsonObject.getString("name");
        final String description = jsonObject.getString("description");
        final LocalDate date = LocalDate.parse(jsonObject.getString("deadline"));
        final boolean isPriority = jsonObject.getBoolean("priority");
        final boolean isCompleted = jsonObject.getBoolean("completed");
        return subgoalBuilder
                .setId(subgoalId)
                .setPlanId(planId)
                .setUsername(username)
                .setName(name)
                .setDescription(description)
                .setDeadline(date)
                .setPriority(isPriority)
                .setIsCompleted(isCompleted)
                .build();
    }

    /**
     * Converts a Subgoal instance into a JSON object suitable for persistence.
     * @param object the Subgoal to convert
     * @return a JSONObject representing the subgoal
     */
    @Override
    public JSONObject convertObjectToJson(Subgoal object) {
        final JSONObject subgoalObj = new JSONObject();
        subgoalObj.put("id", object.getId());
        subgoalObj.put("plan_id", object.getPlanId());
        subgoalObj.put("username", object.getUsername());
        subgoalObj.put("name", object.getName());
        subgoalObj.put("description", object.getDescription());
        subgoalObj.put("deadline", object.getDeadline());
        subgoalObj.put("priority", object.isPriority());
        subgoalObj.put("completed", object.isCompleted());
        return subgoalObj;
    }
}
