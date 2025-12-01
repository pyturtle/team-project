package interface_adapter.calendar;

import java.time.LocalDate;
import java.util.List;

public class CalendarPresenter {

    private final CalendarViewModel calendarViewModel;

    public CalendarPresenter(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    public void addGoal(String goalDescription) {
        CalendarState calendarState = calendarViewModel.getState();
        LocalDate selectedDate = calendarState.getSelectedDate();
        calendarState.addGoal(selectedDate, goalDescription);
        calendarViewModel.setState(calendarState);
        calendarViewModel.firePropertyChange();
    }

    public void removeGoal(String goalDescription) {
        CalendarState calendarState = calendarViewModel.getState();
        LocalDate selectedDate = calendarState.getSelectedDate();
        List<String> goals = calendarState.getGoalsForDate(selectedDate);
        if (goals.contains(goalDescription)) {
            calendarState.removeGoal(selectedDate, goalDescription);
            calendarViewModel.setState(calendarState);
            calendarViewModel.firePropertyChange();
        } else {
            handleInputError("No Goals Here: " + goalDescription);
        }
    }

    public void changeDate(LocalDate newDate) {
        CalendarState calendarState = calendarViewModel.getState();
        calendarState.setSelectedDate(newDate);
        calendarViewModel.setState(calendarState);
        calendarViewModel.firePropertyChange();
    }

    public void handleInputError(String errorMessage) {
        CalendarState calendarState = calendarViewModel.getState();
        calendarState.setErrorMessage(errorMessage);
        calendarViewModel.setState(calendarState);
        calendarViewModel.firePropertyChange();
    }
}
