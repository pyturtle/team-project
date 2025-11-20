package entity.subgoal;

import java.time.LocalDate;

/**
 * The Subgoal entity represents a single actionable step within a plan.
 * Each Subgoal belongs to a specific plan and user, and contains information
 * such as its name, description, deadline, completion status, and priority flag.
 *
 * <p>This is an immutable data object: once created, its fields cannot be changed.
 * Any updates should result in constructing a new Subgoal entity.</p>
 */
public class Subgoal {

    private final String id;
    private final String planId;
    private final String username;

    private final String name;
    private final String description;
    private final LocalDate deadline;

    private final boolean isCompleted;
    private final boolean priority;


    /**
     * Constructs a new Subgoal entity.
     *
     * @param id the unique identifier of the subgoal
     * @param planId the identifier of the plan this subgoal belongs to
     * @param username the identifier of the user who owns this subgoal
     * @param name the name/title of the subgoal
     * @param description the description of what the subgoal requires
     * @param deadline the deadline by which the subgoal should be completed
     * @param isCompleted whether the subgoal is already completed
     * @param priority whether the subgoal is marked as high priority
     *
     * @throws IllegalArgumentException if {@code name} or {@code description} are empty
     *                                  or if {@code deadline} is null
     */
    public Subgoal(String id,
                   String planId,
                   String username,
                   String name,
                   String description,
                   LocalDate deadline,
                   boolean isCompleted,
                   boolean priority) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Subgoal name cannot be empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Subgoal description cannot be empty.");
        }
        if (deadline == null) {
            throw new IllegalArgumentException("Subgoal deadline cannot be null.");
        }

        this.id = id;
        this.planId = planId;
        this.username = username;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
        this.priority = priority;
    }

    /** @return the unique ID of the subgoal */
    public String getId() { return id; }

    /** @return the ID of the plan this subgoal belongs to */
    public String getPlanId() { return planId; }

    /** @return the ID of the user who owns this subgoal */
    public String getUsername() { return username; }

    /** @return the name/title of the subgoal */
    public String getName() { return name; }

    /** @return the description of the subgoal */
    public String getDescription() { return description; }

    /** @return the deadline of the subgoal */
    public LocalDate getDeadline() { return deadline; }

    /** @return whether the subgoal is completed */
    public boolean isCompleted() { return isCompleted; }

    /** @return whether the subgoal is marked as priority */
    public boolean isPriority() { return priority; }
}
