package interface_adapter.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarState {
    private LocalDate selectedDate = LocalDate.now();
    private Map<LocalDate, List<String>> goalsByDate = new HashMap<>();
    private String errorMessage;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<String> getGoalsForDate(LocalDate date) {
        return goalsByDate.computeIfAbsent(date, k -> new ArrayList<>());
    }

    public void addGoal(LocalDate date, String goal) {
        getGoalsForDate(date).add(goal);
    }

    public void removeGoal(LocalDate date, String goal) {
        getGoalsForDate(date).remove(goal);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}