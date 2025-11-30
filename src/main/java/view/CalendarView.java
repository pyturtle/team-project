package view;

import entity.subgoal.Subgoal;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.calendar.CalendarState;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import interface_adapter.show_subgoal.ShowSubgoalController;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private final DefaultListModel<String> goalListModel;
    private final JList<String> goalList;
    private ShowSubgoalController showSubgoalController;

    //calendar buttons: below is the map to store if each date has a goal
    private Map<LocalDate, List<Subgoal>> subgoalsByDate = new HashMap<>();


    // Pagination for upcoming subgoals
    private int subgoalPageOffset = 0;
    private static final int SUBGOALS_PER_PAGE = 6;
    private JButton prevSubgoalsButton;
    private JButton nextSubgoalsButton;
    private JButton openSubgoalButton;

    // Map to track subgoal IDs for the displayed items
    private final Map<String, String> displayTextToSubgoalId = new HashMap<>();
    // Map to track completion status for coloring
    private final Map<String, Boolean> displayTextToCompleted = new HashMap<>();

    // Calendar state when we are in teh calendar view
    private CalendarViewModel viewModel;
    private LocalDate displayedMonth;

    //filter
    private JButton filterButton;
    private FilterSubgoalsController filterSubgoalsController;

    // Subgoal data access for fetching actual subgoals
    private SubgoalDataAccessInterface subgoalDataAccess;
    //private ShowSubgoalController showSubgoalController;

    //new function for calendar buttons
    private void mapSubgoalsByDate(List<Subgoal> allSubgoals) {
        subgoalsByDate.clear();
        for (Subgoal subgoal : allSubgoals) {
            LocalDate deadline = subgoal.getDeadline();
            subgoalsByDate.computeIfAbsent(deadline, k -> new ArrayList<>()).add(subgoal);
        }
    }


    public CalendarView(CalendarViewModel viewModel,
                        SubgoalDataAccessInterface subgoalDataAccess) {
        this.viewModel = viewModel;
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

        // Set custom cell renderer to color completed subgoals green
        goalList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String text = value.toString();
                Boolean isCompleted = displayTextToCompleted.get(text);

                if (isCompleted != null && isCompleted) {
                    // Completed subgoals have green background
                    if (!isSelected) {
                        c.setBackground(new Color(144, 238, 144)); // Light green
                    }
                } else {
                    // Non-completed subgoals have default background
                    if (!isSelected) {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        filterButton = new JButton("Filter Subgoals");

        // Navigation buttons for upcoming subgoals
        prevSubgoalsButton = new JButton("<");
        nextSubgoalsButton = new JButton(">");

        // Button to open selected subgoal
        openSubgoalButton = new JButton("Open Selected Subgoal");

        JPanel goalControlPanel = new JPanel();
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
        filterButton.addActionListener(this);
        prevSubgoalsButton.addActionListener(this);
        nextSubgoalsButton.addActionListener(this);
        openSubgoalButton.addActionListener(this);

        // Register this view as a property change listener
        viewModel.addPropertyChangeListener(this);

        //defined below: this is to make the correct grid for calendar.
        mapSubgoalsByDate(subgoalDataAccess.getSubgoalsByUsername(viewModel.getCalendarState().getUsername()));
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

        // Each grid in the calendar is one button, also added the mini buttons!
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate buttonDate = displayedMonth.withDayOfMonth(day);

            // Main day cell panel
            JPanel dayCell = new JPanel(new BorderLayout());
            dayCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            dayCell.setBackground(Color.WHITE);
            dayCell.setOpaque(true);

            if (buttonDate.equals(viewModel.getCalendarState().getSelectedDate())) {
                dayCell.setBackground(Color.CYAN);
            }

            // Day number at the top
            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            dayCell.add(dayLabel, BorderLayout.NORTH);

            // Subgoals panel with scrolling
            List<Subgoal> subgoalsForDate = subgoalsByDate.getOrDefault(buttonDate, Collections.emptyList());

            if (!subgoalsForDate.isEmpty()) {
                JPanel subgoalsPanel = new JPanel();
                subgoalsPanel.setLayout(new BoxLayout(subgoalsPanel, BoxLayout.Y_AXIS));
                subgoalsPanel.setBackground(Color.WHITE);
                subgoalsPanel.setOpaque(true);

                // Add each subgoal as a clickable button
                for (int i = 0; i < subgoalsForDate.size(); i++) {
                    Subgoal subgoal = subgoalsForDate.get(i);

                    // Create button with better text formatting
                    String buttonText = (i + 1) + ". ";
                    if (subgoal.getName().length() > 10) {
                        buttonText += subgoal.getName().substring(0, 10) + "…";
                    } else {
                        buttonText += subgoal.getName();
                    }

                    JButton subgoalBtn = new JButton(buttonText);
                    subgoalBtn.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    subgoalBtn.setMargin(new Insets(1, 2, 1, 2));
                    subgoalBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                    subgoalBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

                    // Color code based on priority and completion
                    if (subgoal.isCompleted()) {
                        subgoalBtn.setBackground(new Color(144, 238, 144)); // Light green
                        subgoalBtn.setOpaque(true);
                    } else if (subgoal.isPriority()) {
                        subgoalBtn.setBackground(new Color(255, 200, 200)); // Light red
                        subgoalBtn.setOpaque(true);
                    }

                    // Set tooltip to show full name
                    subgoalBtn.setToolTipText(subgoal.getName());

                    subgoalBtn.addActionListener(ev -> {
                        if (showSubgoalController != null) {
                            showSubgoalController.execute(subgoal.getId());
                        }
                    });

                    subgoalsPanel.add(subgoalBtn);
                }

                // Add scroll pane if there are many subgoals
                JScrollPane scrollPane = new JScrollPane(subgoalsPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setBorder(null);
                scrollPane.setBackground(Color.WHITE);

                dayCell.add(scrollPane, BorderLayout.CENTER);
            }

            calendarGrid.add(dayCell);
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
        if (src == prevSubgoalsButton) {
            if (subgoalPageOffset > 0) {
                subgoalPageOffset -= SUBGOALS_PER_PAGE;
                updateUpcomingSubgoals();
            }
        } else if (src == nextSubgoalsButton) {
            subgoalPageOffset += SUBGOALS_PER_PAGE;
            updateUpcomingSubgoals();
        } else if (src == openSubgoalButton) {
            String selectedValue = goalList.getSelectedValue();
            if (selectedValue != null && showSubgoalController != null) {
                String subgoalId = displayTextToSubgoalId.get(selectedValue);
                if (subgoalId != null) {
                    showSubgoalController.execute(subgoalId);
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
        displayTextToSubgoalId.clear();
        displayTextToCompleted.clear();

        if (state.isFilterActive()) {
            // Display filtered subgoals with same format as unfiltered
            List<Subgoal> filteredSubgoals = state.getFilteredSubgoals();
            System.out.println("CalendarView: Showing " + filteredSubgoals.size() + " filtered subgoals");

            // Sort filtered subgoals by deadline, then by completion
            filteredSubgoals.sort(Comparator
                    .comparing(Subgoal::getDeadline)
                    .thenComparing(Subgoal::isCompleted));

            for (Subgoal subgoal : filteredSubgoals) {
                String priorityFlag = subgoal.isPriority() ? " [PRIORITY]" : "";
                String completedFlag = subgoal.isCompleted() ? " ✓" : "";
                String displayText = subgoal.getDeadline() + " - " + subgoal.getName() + priorityFlag + completedFlag;
                goalListModel.addElement(displayText);
                displayTextToSubgoalId.put(displayText, subgoal.getId());
                displayTextToCompleted.put(displayText, subgoal.isCompleted());
            }

            // Disable pagination for filtered results
            prevSubgoalsButton.setEnabled(false);
            nextSubgoalsButton.setEnabled(false);
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
        displayTextToCompleted.clear();

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
        mapSubgoalsByDate(allSubgoals);
        System.out.println("CalendarView: Found " + allSubgoals.size() + " total subgoals for user");

        // Filter to upcoming subgoals (deadline is today or in the future - include completed ones)
        LocalDate today = LocalDate.now();
        List<Subgoal> upcomingSubgoals = new ArrayList<>();
        for (Subgoal subgoal : allSubgoals) {
            if (!subgoal.getDeadline().isBefore(today)) {
                upcomingSubgoals.add(subgoal);
            }
        }

        System.out.println("CalendarView: Found " + upcomingSubgoals.size() + " upcoming subgoals (including completed)");

        // Sort by deadline, then by completion status (incomplete first, then completed)
        upcomingSubgoals.sort(Comparator
                .comparing(Subgoal::getDeadline)
                .thenComparing(Subgoal::isCompleted));

        // Calculate the range to display
        int startIdx = subgoalPageOffset;
        int endIdx = Math.min(startIdx + SUBGOALS_PER_PAGE, upcomingSubgoals.size());

        System.out.println("CalendarView: Displaying subgoals from index " + startIdx + " to " + endIdx);

        // Display the subgoals for current page
        for (int i = startIdx; i < endIdx; i++) {
            Subgoal subgoal = upcomingSubgoals.get(i);
            String priorityFlag = subgoal.isPriority() ? " [PRIORITY]" : "";
            String completedFlag = subgoal.isCompleted() ? " ✓" : "";
            String displayText = subgoal.getDeadline() + " - " + subgoal.getName() + priorityFlag + completedFlag;
            goalListModel.addElement(displayText);
            displayTextToSubgoalId.put(displayText, subgoal.getId());
            displayTextToCompleted.put(displayText, subgoal.isCompleted());
            System.out.println("CalendarView: Added subgoal: " + displayText + (subgoal.isCompleted() ? " (completed)" : ""));
        }

        // Enable/disable navigation buttons
        prevSubgoalsButton.setEnabled(subgoalPageOffset > 0);
        nextSubgoalsButton.setEnabled(endIdx < upcomingSubgoals.size());
        updateCalendar();
    }
    public String getViewName() {
        return "CalendarView";
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
            case 0: filterByPlanId(); break;
            case 1: filterBySubgoalName(); break;
            case 2: filterByPriority(); break;
            case 3: clearFilter(); break;
            // case 4: Cancel - do nothing
        }
    }

    private void filterByPlanId() {
        String planIdStr = JOptionPane.showInputDialog(this, "Enter Plan ID to filter:");
        if (planIdStr != null && !planIdStr.trim().isEmpty()) {
            if (filterSubgoalsController != null) {
                String username = viewModel.getCalendarState().getUsername();
                if (username != null) {
                    filterSubgoalsController.execute(username, planIdStr, null, false);
                } else {
                    JOptionPane.showMessageDialog(this, "User not logged in", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void filterByPriority() {
        if (filterSubgoalsController != null) {
            String username = viewModel.getCalendarState().getUsername();
            if (username != null) {
                filterSubgoalsController.execute(username, null, null, true);
            } else {
                JOptionPane.showMessageDialog(this, "User not logged in", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
                String username = viewModel.getCalendarState().getUsername();
                if (username != null) {
                    filterSubgoalsController.execute(username, null, name, false);
                } else {
                    JOptionPane.showMessageDialog(this, "User not logged in", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void setFilterSubgoalsController(FilterSubgoalsController controller) {
        this.filterSubgoalsController = controller;
    }

    public void setShowSubgoalController(ShowSubgoalController showSubgoalController) {
        this.showSubgoalController = showSubgoalController;
    }
}
