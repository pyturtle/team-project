package app;

import javax.swing.*;

/**
 * The Main class serves as the entry point for the application.
 * This class creates an AppBuilder instance, registers all views and use cases,
 * builds the main JFrame window, and displays it to the user.
 */
public class Main {

    /**
     * The main entry point of the application.
     * This method performs the following steps:
     * - Creates an AppBuilder instance.
     * - Adds all required views: login, signup, logged-in, generate plan, show plan,
     *   save plan, calendar, subgoal Q&A, show subgoal, show plans, and the main view.
     * - Adds all required use cases: signup, login, generate plan, save plan,
     *   show plan, filter subgoals, show subgoal, subgoal Q&A, logout, show plans,
     *   and delete plan.
     * - Builds the main application window using AppBuilder.
     * - Sets the window size.
     * - Centers the window on the screen.
     * - Makes the window visible.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addGeneratePlanView()
                .addShowPlanView()
                .addSavePlanView()
                .addLoggedInView()
                .addCalendarView()
                .addSubgoalQnaView()
                .addShowSubgoalView()
                .addShowPlansView()
                .addMainView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addGeneratePlanUseCase()
                .addSavePlanUseCase()
                .addShowPlanUseCase()
                .addFilterSubgoalsUseCase()
                .addShowSubgoalUseCase()
                .addSubgoalQnaUseCase()
                .addLogoutUseCase()
                .addShowPlansUseCase()
                .addDeletePlanUseCase()
                .addEditPlanUseCase()
                .build();

        final int windowWidth = 1000;
        final int windowHeight = 600;
        application.setSize(windowWidth, windowHeight);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
