package interface_adapter.calendar;

import java.time.LocalDate;

public class CalendarPresenter {

    private final CalendarViewModel calendarViewModel;
    public CalendarPresenter(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    public void addGoal(String goalDescription) {
        CalendarState calendarState = calendarViewModel.getState();
        calendarState.getGoalDescriptions().add(goalDescription);
        calendarViewModel.setState(calendarState);
        calendarViewModel.firePropertyChange();
    }

    public void removeGoal(int index) {
        CalendarState calendarState = calendarViewModel.getState();
        if (index >= 0 && index < calendarState.getGoalDescriptions().size()){
            calendarState.getGoalDescriptions().remove(index);
            calendarViewModel.setState(calendarState);
            calendarViewModel.firePropertyChange();
        }
        else {
            handleInputError("Invalid goal index: " + index);
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
