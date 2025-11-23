package interface_adapter.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entity.subgoal.Subgoal;



public class CalendarState {
    private LocalDate selectedDate = LocalDate.now();
    private Map<LocalDate, List<String>> goalsByDate = new HashMap<>();
    private String errorMessage;
    private String username;
    private boolean filterActive = false;
    private List<String> filteredGoals = new ArrayList<>(); // Store as Strings for display
    private List<entity.subgoal.Subgoal> filteredSubgoals = new ArrayList<>();
    private String currentFilter = "";

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
    public void setFilteredSubgoals(List<entity.subgoal.Subgoal> filteredSubgoals) {
        this.filteredSubgoals = new ArrayList<>(filteredSubgoals);
        this.filteredGoals = convertSubgoalsToStrings(filteredSubgoals);
        this.filterActive = true;
    }

    public void clearFilter() {
        this.filterActive = false;
        this.filteredSubgoals.clear();
        this.filteredGoals.clear();
        this.currentFilter = "";
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public List<String> getFilteredGoals() {
        return new ArrayList<>(filteredGoals);
    }

    public List<entity.subgoal.Subgoal> getFilteredSubgoals() {
        return new ArrayList<>(filteredSubgoals);
    }

    public String getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    private List<String> convertSubgoalsToStrings(List<entity.subgoal.Subgoal> subgoals) {
        List<String> goalStrings = new ArrayList<>();
        for (entity.subgoal.Subgoal subgoal : subgoals) {
            goalStrings.add(subgoalToString(subgoal));
        }
        return goalStrings;
    }

    private String subgoalToString(entity.subgoal.Subgoal subgoal) {
        // Convert Subgoal entity to display string
        // Adjust this based on your Subgoal class structure
        return String.format("%s (Plan: %d, Priority: %s)",
                subgoal.getName(),
                subgoal.getPlanId(),
                subgoal.isPriority() ? "High" : "Normal");
    }
}