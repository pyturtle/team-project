package data_access;

import entity.subgoal.Subgoal;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemorySubgoalDataAccessObject implements SubgoalDataAccessInterface {

    private final List<Subgoal> subgoals = new ArrayList<>();

    @Override
    public List<Subgoal> getSubgoalsByPlan(String planId, String userId) {
        return subgoals.stream()
                .filter(s -> s.getPlanId().equals(planId) && s.getUsername().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Subgoal> getPrioritySubgoals(String userId) {
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(userId) && s.isPriority())
                .collect(Collectors.toList());
    }

    @Override
    public List<Subgoal> getAllSubgoalsForUser(String userId) {
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Subgoal> getSubgoalsByUsername(String username) {
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public Subgoal getSubgoalById(String id) {
        return subgoals.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveUpdatedSubgoal(Subgoal subgoal) {
        subgoals.removeIf(s -> s.getId().equals(subgoal.getId()));
        subgoals.add(subgoal);
    }

    @Override
    public void save() {
        // In-memory doesn't need to save to persistent storage
    }

    @Override
    public void save(Subgoal subgoal) {
        subgoals.add(subgoal);
    }

    @Override
    public void updatePriority(String id, boolean priority) {
        Subgoal old = getSubgoalById(id);
        if (old != null) {
            Subgoal updated = new Subgoal(
                    old.getId(), old.getPlanId(), old.getUsername(),
                    old.getName(), old.getDescription(), old.getDeadline(),
                    old.isCompleted(), priority
            );
            saveUpdatedSubgoal(updated);
        }
    }

    @Override
    public void updateCompleted(String id, boolean completed) {
        Subgoal old = getSubgoalById(id);
        if (old != null) {
            Subgoal updated = new Subgoal(
                    old.getId(), old.getPlanId(), old.getUsername(),
                    old.getName(), old.getDescription(), old.getDeadline(),
                    completed, old.isPriority()
            );
            saveUpdatedSubgoal(updated);
        }
    }

    @Override
    public void deleteSubgoal(String id) {
        subgoals.removeIf(s -> s.getId().equals(id));
    }

    @Override
    public List<Subgoal> getSubgoalsByPlanId(String planId) {
        return subgoals.stream()
                .filter(s -> s.getPlanId().equals(planId))
                .collect(Collectors.toList());
    }

    public List<Subgoal> getSubgoalsByName(String name, String userId) {
        String searchTerm = name.toLowerCase();
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(userId) &&
                        s.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }


    public List<Subgoal> getCompletedSubgoals(String userId) {
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(userId) && s.isCompleted())
                .collect(Collectors.toList());
    }


    public List<Subgoal> getIncompleteSubgoals(String userId) {
        return subgoals.stream()
                .filter(s -> s.getUsername().equals(userId) && !s.isCompleted())
                .collect(Collectors.toList());
    }

    // Helper for testing
    public void add(Subgoal s) {
        subgoals.add(s);
    }
}