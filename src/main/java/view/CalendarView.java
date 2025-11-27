package view;

import entity.subgoal.Subgoal;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.calendar.CalendarState;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import interface_adapter.logout.LogoutController;
import interface_adapter.plan.show_plans.ShowPlansController;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

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

    // Pagination for upcoming subgoals
    private int subgoalPageOffset = 0;
    private static final int SUBGOALS_PER_PAGE = 6;
    private JButton prevSubgoalsButton;
    private JButton nextSubgoalsButton;
    private JButton openSubgoalButton;

    // Map to track subgoal IDs for the displayed items
    private final Map<String, String> displayTextToSubgoalId = new HashMap<>();

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

    // Subgoal data access for fetching actual subgoals
    private SubgoalDataAccessInterface subgoalDataAccess;

    // SubgoalView for opening individual subgoals
    private SubgoalView subgoalView;

    public CalendarView(CalendarViewModel viewModel, interface_adapter.ViewManagerModel viewManagerModel,
                        SubgoalDataAccessInterface subgoalDataAccess) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.subgoalDataAccess = subgoalDataAccess;
        this.displayedMonth = LocalDate.now().withDayOfMonth(1); // starting with current month, should prob be nov

        setLayout(new BorderLayout());

        //Changing months
        JPanel monthPanel = new JPanel(new BorderLayout());
        prevMonthButton = new JButton("<");
        nextMonthButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        monthPanel.add(prevMonthButton, BorderLayout.WEST);
        monthPanel.add(monthLabel, BorderLayout.CENTER);
        monthPanel.add(nextMonthButton, BorderLayout.EAST);
        add(monthPanel, BorderLayout.NORTH);

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

        // Navigation buttons for upcoming subgoals
        prevSubgoalsButton = new JButton("<");
        nextSubgoalsButton = new JButton(">");

        // Button to open selected subgoal
        openSubgoalButton = new JButton("Open Selected Subgoal");

        JPanel goalControlPanel = new JPanel();
        goalControlPanel.add(goalInput);
        goalControlPanel.add(addGoalButton);
        goalControlPanel.add(removeGoalButton);
        goalControlPanel.add(filterButton);
        goalControlPanel.add(openSubgoalButton);

        // Panel for subgoal navigation
        JPanel subgoalNavPanel = new JPanel(new BorderLayout());
        JLabel upcomingLabel = new JLabel("Upcoming Subgoals", SwingConstants.CENTER);
        upcomingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navButtonPanel.add(prevSubgoalsButton);
        navButtonPanel.add(nextSubgoalsButton);
        subgoalNavPanel.add(upcomingLabel, BorderLayout.WEST);
        subgoalNavPanel.add(navButtonPanel, BorderLayout.EAST);

        JPanel goalPanel = new JPanel(new BorderLayout());
        goalPanel.add(subgoalNavPanel, BorderLayout.NORTH);
        goalPanel.add(new JScrollPane(goalList), BorderLayout.CENTER);
        goalPanel.add(goalControlPanel, BorderLayout.SOUTH);
        add(goalPanel, BorderLayout.SOUTH);

        // Action perofmred buttons
        prevMonthButton.addActionListener(this);
        nextMonthButton.addActionListener(this);
        addGoalButton.addActionListener(this);
        removeGoalButton.addActionListener(this);
        filterButton.addActionListener(this);
        prevSubgoalsButton.addActionListener(this);
        nextSubgoalsButton.addActionListener(this);
        openSubgoalButton.addActionListener(this);

        // Register this view as a property change listener
        viewModel.addPropertyChangeListener(this);

        //defined below: this is to make the correct grid for calendar.
        updateCalendar();

        // Initial load of upcoming subgoals if username is already set
        if (viewModel.getCalendarState().getUsername() != null) {
            updateUpcomingSubgoals();
        }
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
                // Extract date and goal text from the selected item
                String goalText = selectedGoal;
                if (selectedGoal.contains(" - ")) {
                    String[] parts = selectedGoal.split(" - ", 2);
                    if (parts.length == 2) {
                        goalText = parts[1];
                        try {
                            LocalDate goalDate = LocalDate.parse(parts[0]);
                            state.removeGoal(goalDate, goalText); // remove a goal/finish
                            viewModel.firePropertyChanged(); // refresh goals
                        } catch (Exception ex) {
                            // If parsing fails, try to remove from selected date
                            state.removeGoal(selectedDate, selectedGoal);
                            viewModel.firePropertyChanged();
                        }
                    }
                } else {
                    state.removeGoal(selectedDate, goalText);
                    viewModel.firePropertyChanged();
                }
            }
        } else if (src == prevSubgoalsButton) {
            if (subgoalPageOffset > 0) {
                subgoalPageOffset -= SUBGOALS_PER_PAGE;
                updateUpcomingSubgoals();
            }
        } else if (src == nextSubgoalsButton) {
            subgoalPageOffset += SUBGOALS_PER_PAGE;
            updateUpcomingSubgoals();
        } else if (src == openSubgoalButton) {
            String selectedValue = goalList.getSelectedValue();
            if (selectedValue != null && subgoalView != null) {
                String subgoalId = displayTextToSubgoalId.get(selectedValue);
                if (subgoalId != null) {
                    subgoalView.openForSubgoal(subgoalId);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Could not find subgoal ID for the selected item.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedValue == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a subgoal first.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
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

        // Update the goal list display based on filter state
        goalListModel.clear();

        if (state.isFilterActive()) {
            // Display filtered subgoals
            List<entity.subgoal.Subgoal> filteredSubgoals = state.getFilteredSubgoals();
            System.out.println("DEBUG: Showing " + filteredSubgoals.size() + " filtered subgoals");
            for (entity.subgoal.Subgoal subgoal : filteredSubgoals) {
                goalListModel.addElement(subgoal.getName());
            }
        } else {
            // Display all goals for selected date (call updateUpcomingSubgoals or handle normally)
            updateUpcomingSubgoals();
        }

        updateCalendar();

        // error popups
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            state.setErrorMessage("");
        }
    }

    private void updateUpcomingSubgoals() {
        CalendarState state = viewModel.getCalendarState();
        goalListModel.clear();
        displayTextToSubgoalId.clear();

        if (subgoalDataAccess == null) {
            System.out.println("CalendarView: subgoalDataAccess is null");
            return;
        }

        if (state.getUsername() == null) {
            System.out.println("CalendarView: username is null, cannot load subgoals");
            return;
        }

        System.out.println("CalendarView: Loading subgoals for user: " + state.getUsername());

        // Get all subgoals for the current user
        List<Subgoal> allSubgoals = subgoalDataAccess.getSubgoalsByUsername(state.getUsername());
        System.out.println("CalendarView: Found " + allSubgoals.size() + " total subgoals for user");

        // Filter to upcoming subgoals (not completed and deadline is today or in the future)
        LocalDate today = LocalDate.now();
        List<Subgoal> upcomingSubgoals = new ArrayList<>();
        for (Subgoal subgoal : allSubgoals) {
            if (!subgoal.isCompleted() && !subgoal.getDeadline().isBefore(today)) {
                upcomingSubgoals.add(subgoal);
            }
        }

        System.out.println("CalendarView: Found " + upcomingSubgoals.size() + " upcoming subgoals");

        // Sort by deadline
        upcomingSubgoals.sort(Comparator.comparing(Subgoal::getDeadline));

        // Calculate the range to display
        int startIdx = subgoalPageOffset;
        int endIdx = Math.min(startIdx + SUBGOALS_PER_PAGE, upcomingSubgoals.size());

        System.out.println("CalendarView: Displaying subgoals from index " + startIdx + " to " + endIdx);

        // Display the subgoals for current page
        for (int i = startIdx; i < endIdx; i++) {
            Subgoal subgoal = upcomingSubgoals.get(i);
            String priorityFlag = subgoal.isPriority() ? " [PRIORITY]" : "";
            String displayText = subgoal.getDeadline() + " - " + subgoal.getName() + priorityFlag;
            goalListModel.addElement(displayText);
            displayTextToSubgoalId.put(displayText, subgoal.getId());
            System.out.println("CalendarView: Added subgoal: " + displayText);
        }

        // Enable/disable navigation buttons
        prevSubgoalsButton.setEnabled(subgoalPageOffset > 0);
        nextSubgoalsButton.setEnabled(endIdx < upcomingSubgoals.size());
    }
    public String getViewName() {
        return "CalendarView";
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    private void showFilterDialog() {
        String[] options = {"By Plan ID", "By Subgoal Name", "Priority Only", "Clear Filter", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Filter subgoals:",
                "Filter Subgoals",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: filterByPlanId(); break;        // "By Plan ID"
            case 1: filterBySubgoalName(); break;   // "By Subgoal Name"
            case 2: filterByPriority(); break;      // "Priority Only" - THIS WAS WRONG
            case 3: clearFilter(); break;           // "Clear Filter"
            // case 4: Cancel - do nothing
        }
    }

    private void filterByPlanId() {
        String planIdStr = JOptionPane.showInputDialog(this, "Enter Plan ID to filter:");
        if (planIdStr != null && !planIdStr.trim().isEmpty()) {
            if (filterSubgoalsController != null) {
                filterSubgoalsController.execute(planIdStr, null, false);  // Add null for subgoalName
            }
        }
    }

    private void filterByPriority() {
        if (filterSubgoalsController != null) {
            filterSubgoalsController.execute(null, null, true);  // Add null for subgoalName
        }
    }

    private void clearFilter() {
        CalendarState state = viewModel.getCalendarState();
        state.clearFilter();
        viewModel.firePropertyChanged();
    }

    private void filterBySubgoalName() {
        String name = JOptionPane.showInputDialog(this, "Enter subgoal name to search for:");
        if (name != null && !name.trim().isEmpty()) {
            if (filterSubgoalsController != null) {
                filterSubgoalsController.execute(null, name, false);
            }
        }
    }


    public void setShowPlansController(ShowPlansController showPlansController) {
        this.showPlansController = showPlansController;
    }

    public void setSubgoalView(SubgoalView subgoalView) {
        this.subgoalView = subgoalView;
    }

    public void setFilterSubgoalsController(FilterSubgoalsController controller) {
        this.filterSubgoalsController = controller;
    }
}
