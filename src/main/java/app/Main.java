package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView() // Still needed for LoggedInViewModel (used by presenters)
                .addCalendarView()
                .addShowPlansView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addShowPlansUseCase()
                .addDeletePlanUseCase()
                .addEditPlanUseCase()
                .build();

        application.setSize(1000, 600);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
