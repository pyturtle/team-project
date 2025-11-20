package entity.subgoal;

import java.time.LocalDate;

public class SubgoalFactory {
    public Subgoal create(String id,
                          String planId,
                          String userEmail,
                          String name,
                          String description,
                          LocalDate deadline,
                          boolean isCompleted,
                          boolean priority)
    {
        return new Subgoal(id, planId, userEmail, name, description, deadline, isCompleted, priority);
    }
}
