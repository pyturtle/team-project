package use_case.subgoal.save_subgoal;

import java.time.LocalDate;

public class SaveSubgoalInputData {
    private final String name;
    private final String description;
    private final LocalDate deadline;

    public SaveSubgoalInputData(String name, String description, LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
}
