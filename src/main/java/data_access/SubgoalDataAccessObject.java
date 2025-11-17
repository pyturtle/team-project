package data_access;

import entity.Subgoal;
import use_case.show_subgoal.SubgoalDataAccessInterface;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple in-memory implementation of SubgoalDataAccessInterface.
 * <p>
 * This class stores Subgoal entities in a Map keyed by their ID.
 * It can be used as a placeholder until a file- or DB-backed DAO
 * is implemented by the team.
 */
public class SubgoalDataAccessObject implements SubgoalDataAccessInterface {

    private final Map<Integer, Subgoal> subgoals = new HashMap<>();

    /**
     * Constructs an empty SubgoalDataAccessObject.
     */
    public SubgoalDataAccessObject() {
    }

    /**
     * Constructs a SubgoalDataAccessObject pre-populated with the given subgoals.
     *
     * @param initialSubgoals a collection of subgoals to add on construction
     */
    public SubgoalDataAccessObject(Collection<Subgoal> initialSubgoals) {
        for (Subgoal s : initialSubgoals) {
            subgoals.put(s.getId(), s);
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
    }

    @Override
    public Subgoal getSubgoalById(int id) {
        return subgoals.get(id);
    }

    @Override
    public void updatePriority(int id, boolean priority) {
        Subgoal old = subgoals.get(id);
        if (old == null) {
            return;
        }

        // Subgoal is immutable, so we create a new one with the updated priority.
        Subgoal updated = new Subgoal(
                old.getId(),
                old.getPlanId(),
                old.getUserId(),
                old.getName(),
                old.getDescription(),
                old.getDeadline(),
                old.isCompleted(),
                priority
        );

        subgoals.put(id, updated);
    }
}
