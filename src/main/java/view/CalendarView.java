package view;

import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.calendar.CalendarState;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import interface_adapter.logout.LogoutController;
import interface_adapter.plan.show_plans.ShowPlansController;
import interface_adapter.filter_subgoals.FilterSubgoalsController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class CalendarView extends JPanel implements ActionListener, PropertyChangeListener {
    // Views, go on the top right


    // Month changing, ie. left/right buttons, and show current month.
    private JButton prevMonthButton;
    private JButton nextMonthButton;
    private JLabel monthLabel;

    // Create the calendar grid, probably 7 x 5 haven't figured this out yet ..
    private JPanel calendarGrid;
    private List<JButton> dayButtons;

    // Goal buttons, on the bottom i think...
    private DefaultListModel<String> goalListModel;
    private JList<String> goalList;
    private JTextField goalInput;
    private JButton addGoalButton;
    private JButton removeGoalButton;

    // Calendar state when we are in teh calendar view
    private CalendarViewModel viewModel;
    private LocalDate displayedMonth;

    // For switching views. This is not a button
    private interface_adapter.ViewManagerModel viewManagerModel;

    // Logout controller
    private LogoutController logoutController;

    // Show Plans controller for loading plans when switching views
    private ShowPlansController showPlansController;

    //filter
    private JButton filterButton;
    private FilterSubgoalsController filterSubgoalsController;

    public CalendarView(CalendarViewModel viewModel, interface_adapter.ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.displayedMonth = LocalDate.now().withDayOfMonth(1); // starting with current month, should prob be nov

        setLayout(new BorderLayout());

        //View buttons to change view.
        JPanel topPanel = new JPanel(new BorderLayout());

        // Navigation buttons on the left


        //Changing months
        JPanel monthPanel = new JPanel(new BorderLayout());
        prevMonthButton = new JButton("<");
        nextMonthButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        monthPanel.add(prevMonthButton, BorderLayout.WEST);
        monthPanel.add(monthLabel, BorderLayout.CENTER);
        monthPanel.add(nextMonthButton, BorderLayout.EAST);
        add(monthPanel, BorderLayout.CENTER);

        //Create the calendar grid. 0 rows to create as many as we need
        calendarGrid = new JPanel(new GridLayout(0, 7));
        add(calendarGrid, BorderLayout.CENTER);

        // Adding and removing subgoals
        goalListModel = new DefaultListModel<>();
        goalList = new JList<>(goalListModel);
        goalInput = new JTextField(15);
        addGoalButton = new JButton("Add Subgoal");
        removeGoalButton = new JButton("Remove Subgoal");
        filterButton = new JButton("Filter Subgoals");
        JPanel goalControlPanel = new JPanel();
        goalControlPanel.add(goalInput);
        goalControlPanel.add(addGoalButton);
        goalControlPanel.add(removeGoalButton);
        goalControlPanel.add(filterButton);
        JPanel goalPanel = new JPanel(new BorderLayout());
        goalPanel.add(new JScrollPane(goalList), BorderLayout.CENTER);
        goalPanel.add(goalControlPanel, BorderLayout.SOUTH);
        add(goalPanel, BorderLayout.SOUTH);

        // Action perofmred buttons
        prevMonthButton.addActionListener(this);
        nextMonthButton.addActionListener(this);
        addGoalButton.addActionListener(this);
        removeGoalButton.addActionListener(this);
        filterButton.addActionListener(this);

        //defined below: this is to make the correct grid for calendar.
        updateCalendar();
    }
    private void updateCalendar() {
        calendarGrid.removeAll(); //first start by removing everything so we can rewrite calendar.
        dayButtons = new ArrayList<>();

        // month number/name
        monthLabel.setText(displayedMonth.getMonth().toString() + " " + displayedMonth.getYear());

        // days in the week
        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String wd : weekdays) {
            JLabel label = new JLabel(wd, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            calendarGrid.add(label);
        }

        // first day of the month, important for structure of the calendar grid
        LocalDate firstOfMonth = displayedMonth.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday=0
        int daysInMonth = displayedMonth.lengthOfMonth();

        // empty before first of the month.
        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        // Each grid in the calendar is one button.
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            LocalDate buttonDate = displayedMonth.withDayOfMonth(day);

            // Show us that we chose a day, by highlighting
            if (buttonDate.equals(viewModel.getCalendarState().getSelectedDate())) {
                dayButton.setBackground(Color.CYAN);
            }

            dayButton.addActionListener(this);
            dayButtons.add(dayButton);
            calendarGrid.add(dayButton);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) { //this is to code in the buttons so they actually work
        Object src = e.getSource();
        CalendarState state = viewModel.getCalendarState(); // get current calendar state
        LocalDate selectedDate = state.getSelectedDate();


        // changing months
        if (src == prevMonthButton) {
            displayedMonth = displayedMonth.minusMonths(1);
            updateCalendar(); // redraw calendars
        } else if (src == nextMonthButton) {
            displayedMonth = displayedMonth.plusMonths(1);
            updateCalendar(); // redraw calendras
        }

        // goals
        else if (src == addGoalButton) {
            String text = goalInput.getText().trim();
            if (!text.isEmpty()) {
                state.addGoal(selectedDate, text); // add a goal
                goalInput.setText("");
                viewModel.firePropertyChanged(); // refresh goals
            }
        } else if (src == removeGoalButton) {
            String selectedGoal = goalList.getSelectedValue();
            if (selectedGoal != null) {
                state.removeGoal(selectedDate, selectedGoal); // remove a goal/finish
                viewModel.firePropertyChanged(); // refresh goals
            }
        } else if (src == filterButton) {
            showFilterDialog();
        }
        // Day buttons inside the grid, so we can press each day
        else if (src instanceof JButton) {
            JButton dayButton = (JButton) src;
            try {
                int day = Integer.parseInt(dayButton.getText());
                LocalDate clickedDate = displayedMonth.withDayOfMonth(day);
                state.setSelectedDate(clickedDate); // update the date
                viewModel.firePropertyChanged();
                updateCalendar();
            } catch (NumberFormatException ignored) {
                // ignore emptys
            }
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        CalendarState state = viewModel.getCalendarState();

        // update goals
        if (state.isFilterActive()) {
            // Display filtered subgoals
            List<entity.subgoal.Subgoal> filteredSubgoals = state.getFilteredSubgoals();
            for (entity.subgoal.Subgoal subgoal : filteredSubgoals) {
                goalListModel.addElement(subgoal.getName() + " (Plan: " + subgoal.getPlanId() + ")");
            }
        } else {
            // Display all goals for selected date
            List<String> goals = state.getGoalsForDate(state.getSelectedDate());
            for (String goal : goals) {
                goalListModel.addElement(goal);
            }
        }

        updateCalendar();

        // error popups
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            state.setErrorMessage("");
        }

    }
    public String getViewName() {
        return "CalendarView";
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    private void showFilterDialog() {
        String[] options = {"By Plan ID", "Priority Only", "Clear Filter", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Filter subgoals:",
                "Filter Subgoals",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: filterByPlanId(); break;
            case 1: filterByPriority(); break;
            case 2: clearFilter(); break;
        }
    }

    private void filterByPlanId() {
        String planIdStr = JOptionPane.showInputDialog(this, "Enter Plan ID to filter:");
        if (planIdStr != null && !planIdStr.trim().isEmpty()) {
            if (filterSubgoalsController != null) {
                filterSubgoalsController.execute(planIdStr, false);  // Instance method call
            } else {
                JOptionPane.showMessageDialog(this, "Filter controller not available");
            }
        }
    }

    private void filterByPriority() {
        if (filterSubgoalsController != null) {
            filterSubgoalsController.execute(null, true);  // Instance method call
        } else {
            JOptionPane.showMessageDialog(this, "Filter controller not available");
        }
    }

    private void clearFilter() {
        CalendarState state = viewModel.getCalendarState();
        state.clearFilter();
        viewModel.firePropertyChanged();
    }


    public void setShowPlansController(ShowPlansController showPlansController) {
        this.showPlansController = showPlansController;
    }

    public void setFilterSubgoalsController(FilterSubgoalsController controller) {
        this.filterSubgoalsController = controller;
    }
}
