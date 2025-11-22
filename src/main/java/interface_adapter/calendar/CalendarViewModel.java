package interface_adapter.calendar;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CalendarViewModel extends ViewModel<CalendarState> {

    private CalendarState calendarState = new CalendarState();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public CalendarViewModel() {
        super("Calendar");
    }

    public CalendarState getCalendarState() {
        return calendarState;
    }
    public void setCalendarState(CalendarState calendarState) {
        this.calendarState = calendarState;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public void firePropertyChanged() {
        propertyChangeSupport.firePropertyChange("state", null, this.calendarState);
    }
}
