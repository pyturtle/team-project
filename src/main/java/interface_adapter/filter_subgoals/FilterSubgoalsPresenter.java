package interface_adapter.filter_subgoals;

import interface_adapter.calendar.CalendarViewModel;
import use_case.filter_subgoals.FilterSubgoalsOutputBoundary;
import use_case.filter_subgoals.FilterSubgoalsOutputData;

public class FilterSubgoalsPresenter implements FilterSubgoalsOutputBoundary {
    private final CalendarViewModel calendarViewModel;

    public FilterSubgoalsPresenter(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void present(FilterSubgoalsOutputData outputData) {
        var state = calendarViewModel.getCalendarState();
        state.setFilteredSubgoals(outputData.getFilteredSubgoals());
        state.setFilterActive(true);
        calendarViewModel.firePropertyChanged();
    }
}