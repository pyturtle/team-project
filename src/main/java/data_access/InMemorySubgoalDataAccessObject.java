package data_access;

import entity.Subgoal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemorySubgoalDataAccessObject implements SubgoalDataAccessInterface {

    private final List<Subgoal> subgoals = new ArrayList<>();

    @Override
    public List<Subgoal> getSubgoalsByPlan(int planId, int userId) {
        return subgoals.stream()
                .filter(s -> s.getPlanId() == planId && s.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subgoal> getPrioritySubgoals(int userId) {
        return subgoals.stream()
                .filter(s -> s.getUserId() == userId && s.isPriority())
                .collect(Collectors.toList());
    }

    @Override
    public List<Subgoal> getAllSubgoalsForUser(int userId) {
        return subgoals.stream()
                .filter(s -> s.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Subgoal getSubgoalById(int subgoalId) {
        return subgoals.stream()
                .filter(s -> s.getId() == subgoalId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveUpdatedSubgoal(Subgoal subgoal) {
        subgoals.removeIf(s -> s.getId() == subgoal.getId());
        subgoals.add(subgoal);
    }

    // Helper for testing
    public void add(Subgoal s) {
        subgoals.add(s);
    }
}
