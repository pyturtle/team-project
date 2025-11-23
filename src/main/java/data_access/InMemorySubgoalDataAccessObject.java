package data_access;

import entity.subgoal.Subgoal;

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
    public Subgoal getSubgoalById(String subgoalId) {
        return subgoals.stream()
                .filter(s -> s.getId().equals(subgoalId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveUpdatedSubgoal(Subgoal subgoal) {
        subgoals.removeIf(s -> s.getId().equals(subgoal.getId()));
        subgoals.add(subgoal);
    }
    // Helper for testing
    public void add(Subgoal s) {
        subgoals.add(s);
    }
}
