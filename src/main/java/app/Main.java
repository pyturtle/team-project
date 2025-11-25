package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addGeneratePlanView()
                .addShowPlanView()
                .addSavePlanView()
                .addShowSubgoalView()
                .addSubgoalQAView()
                .addLoggedInView() // Still needed for LoggedInViewModel (used by presenters)
                .addCalendarView()
                .addSubgoalView()
                .addShowPlansView()
                .addMainView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addGeneratePlanUseCase()
                .addLogoutUseCase()
                .addShowPlansUseCase()
                .addShowSubgoalUseCase()
                .addDeletePlanUseCase()
                .addSubgoalQAUseCase()
                .build();

        application.setSize(1000, 600);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}