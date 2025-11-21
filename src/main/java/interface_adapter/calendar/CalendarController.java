package interface_adapter.calendar;

import java.time.LocalDate;

public class CalendarController {
    private final CalendarPresenter calendarPresenter;

    public CalendarController(CalendarPresenter calendarPresenter) {
        this.calendarPresenter = calendarPresenter;
    }
    public void addGoal(String goalDescription) {
        if (goalDescription == null || goalDescription.trim().isEmpty()) {
            calendarPresenter.handleInputError("Goals can't be empty!");
            return;
        }

        calendarPresenter.addGoal(goalDescription.trim());
    }

    public void removeGoal(String goalDescription) {
        if (goalDescription == null || goalDescription.trim().isEmpty()) {
            calendarPresenter.handleInputError("No goal selected!");
            return;
        }

        calendarPresenter.removeGoal(goalDescription.trim());
    }
    public void changeDate(LocalDate newDate) {
        if (newDate == null) {
            calendarPresenter.handleInputError("Date Invalid!");
            return;
        }

        calendarPresenter.changeDate(newDate);
    }
}
