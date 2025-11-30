package data_access.file;
import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalBuilder;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple in-memory implementation of SubgoalDataAccessInterface.
 * <p>
 * This class stores Subgoal entities in a Map keyed by their ID.
 * It can be used as a placeholder until a file- or DB-backed DAO
 * is implemented by the team.
 */
public class FileSubgoalDataAccessObject extends JsonDataAccess<Subgoal>
        implements SubgoalDataAccessInterface{
    private final SubgoalBuilder subgoalBuilder;
    private final ArrayList<Subgoal> subgoals = new ArrayList<>();

    /**
     * Constructs an empty SubgoalDataAccessObject.
     */
    public FileSubgoalDataAccessObject(String subgoalsFilePath, SubgoalBuilder subgoalBuilder) {
        super(subgoalsFilePath);
        this.subgoalBuilder = subgoalBuilder;
        if (subgoalsFilePath != null) {
            try {
                loadFromJson(subgoals);
            } catch (Exception e) {
                System.err.println("Could not load subgoals from file: " + e.getMessage());
            }
        }
    }


    @Override
    public Subgoal getSubgoalById(String id) {
        return subgoals.stream()
                .filter(subgoal -> subgoal.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updatePriority(String id, boolean priority) {
        Subgoal old = subgoals.stream()
                .filter(subgoal -> subgoal.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (old == null) {
            return;
        }
        subgoals.remove(old);
        Subgoal updated = subgoalBuilder.copyFromSubgoal(old).setPriority(priority).build();
        subgoals.add(updated);
        this.saveToJson(subgoals);
    }

    @Override
    public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getPlanId().equals(planId) && subgoal.getUsername().equals(userId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public List<Subgoal> getPrioritySubgoals(String userId) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && subgoal.isPriority()) {
                result.add(subgoal);
            }
        }
        return result;
    }


    @Override
    public List<Subgoal> getAllSubgoalsForUser(String userId) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public void saveSubgoal(Subgoal subgoal) {
        Subgoal existing = getSubgoalById(subgoal.getId());
        if (existing != null) {
            subgoals.remove(existing);
        }
        subgoals.add(subgoal);
        this.saveToJson(subgoals);
    }

    public void updateCompleted(String id, boolean completed) {
        Subgoal old = getSubgoalById(id);
        if (old == null) {
            return;
        }
        subgoals.remove(old);
        Subgoal updated = subgoalBuilder.copyFromSubgoal(old).setIsCompleted(completed).build();
        subgoals.add(updated);
        this.saveToJson(subgoals);
    }


    public List<Subgoal> getSubgoalsByName(String name, String userId) {
        String searchTerm = name.toLowerCase();
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) &&
                    subgoal.getName().toLowerCase().contains(searchTerm)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public List<Subgoal> getIncompleteSubgoals(String userId) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && !subgoal.isCompleted()) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public List<Subgoal> getCompletedSubgoals(String userId) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(userId) && subgoal.isCompleted()) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public List<Subgoal> getSubgoalsByUsername(String username) {
        List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getUsername().equals(username)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public void deleteSubgoal(String id) {
        Subgoal subgoal = getSubgoalById(id);
        if (subgoal == null) {
            return;
        }
        subgoals.remove(subgoal);
        this.saveToJson(subgoals);
    }

    @Override
    public java.util.List<Subgoal> getSubgoalsByPlanId(String planId) {
        java.util.List<Subgoal> result = new ArrayList<>();
        for (Subgoal subgoal : subgoals) {
            if (subgoal.getPlanId().equals(planId)) {
                result.add(subgoal);
            }
        }
        return result;
    }

    @Override
    public Subgoal parseJsonObject(JSONObject jsonObject) {
        String subgoalId = jsonObject.getString("id");
        String planId = jsonObject.getString("plan_id");
        String username = jsonObject.getString("username");
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        LocalDate date = LocalDate.parse(jsonObject.getString("deadline"));
        boolean isPriority = jsonObject.getBoolean("priority");
        boolean isCompleted = jsonObject.getBoolean("completed");
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

    @Override
    public JSONObject convertObjectToJson(Subgoal object) {
        JSONObject subgoalObj = new JSONObject();
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
