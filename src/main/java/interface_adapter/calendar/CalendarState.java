package interface_adapter.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarState {
    private LocalDate selectedDate =  LocalDate.now();
    private List<String> goalDescriptions = new ArrayList<>();
    private String errorMessage;

    public LocalDate getSelectedDate() {return selectedDate;}
    public void setSelectedDate(LocalDate selectedDate) {this.selectedDate = selectedDate;}

    public List<String> getGoalDescriptions() {return goalDescriptions;}
    public void setGoalDescriptions(List<String> goalDescriptions) {this.goalDescriptions = goalDescriptions;}

    public String getErrorMessage() {return errorMessage;}
    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}
}
