package interface_adapter.calendar;

import java.time.LocalDate;

public class CalendarController {
    private final CalendarPresenter calendarPresenter;
    public CalendarController(CalendarPresenter calendarPresenter) {
        this.calendarPresenter = calendarPresenter;
    }

    public void addGoal(String goalDescription) {
        if (goalDescription == null || goalDescription.trim().isEmpty()) {
            calendarPresenter.handleInputError("Goal cannot be empty.");
            return;
        }

        calendarPresenter.addGoal(goalDescription.trim());
    }

    public void removeGoal(int index) {
        calendarPresenter.removeGoal(index);
    }

    public void changeDate(LocalDate newDate) {
        calendarPresenter.changeDate(newDate);
    }
}
