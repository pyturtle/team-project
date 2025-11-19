package app;

import data_access.FileUserDataAccessObject;
import data_access.InMemoryPlanDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.delete_plan.DeletePlanController;
import interface_adapter.delete_plan.DeletePlanPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.show_plans.ShowPlansController;
import interface_adapter.show_plans.ShowPlansPresenter;
import interface_adapter.show_plans.ShowPlansViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.delete_plan.DeletePlanInputBoundary;
import use_case.delete_plan.DeletePlanInteractor;
import use_case.delete_plan.DeletePlanOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.show_plans.ShowPlansInputBoundary;
import use_case.show_plans.ShowPlansInteractor;
import use_case.show_plans.ShowPlansOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

// Delete Plan functionality added
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // Plan data access object - loads from JSON file
    // To use JSON file: new InMemoryPlanDataAccessObject("plans.json")
    // To use demo data: new InMemoryPlanDataAccessObject()
    final InMemoryPlanDataAccessObject planDataAccessObject = new InMemoryPlanDataAccessObject("plans.json");

    private ShowPlansView showPlansView;
    private ShowPlansViewModel showPlansViewModel;
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private CalendarViewModel calendarViewModel;
    private LoginView loginView;
    private CalendarView calendarView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addShowPlansView() {
        showPlansViewModel = new ShowPlansViewModel();
        showPlansView = new ShowPlansView(showPlansViewModel, viewManagerModel);
        cardPanel.add(showPlansView, showPlansView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        // LoggedInViewModel is still needed by presenters, but we don't need the view anymore
        loggedInViewModel = new LoggedInViewModel();
        return this;
    }
    public AppBuilder addCalendarView() {
        calendarViewModel = new CalendarViewModel();
        calendarView = new CalendarView(calendarViewModel, viewManagerModel);
        cardPanel.add(calendarView, calendarView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, signupViewModel, calendarViewModel, showPlansViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Show Plans Use Case to the application.
     * @return this builder
     */
    public AppBuilder addShowPlansUseCase() {
        final ShowPlansOutputBoundary showPlansOutputBoundary = new ShowPlansPresenter(
                viewManagerModel, showPlansViewModel, loggedInViewModel);

        final ShowPlansInputBoundary showPlansInteractor =
                new ShowPlansInteractor(planDataAccessObject, showPlansOutputBoundary);

        final ShowPlansController showPlansController = new ShowPlansController(showPlansInteractor);
        showPlansView.setShowPlansController(showPlansController);
        calendarView.setShowPlansController(showPlansController);
        return this;
    }

    /**
     * Adds the Delete Plan Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDeletePlanUseCase() {
        final DeletePlanOutputBoundary deletePlanOutputBoundary = new DeletePlanPresenter(showPlansViewModel);

        final DeletePlanInputBoundary deletePlanInteractor =
                new DeletePlanInteractor(planDataAccessObject, deletePlanOutputBoundary);

        final DeletePlanController deletePlanController = new DeletePlanController(deletePlanInteractor);
        showPlansView.setDeletePlanController(deletePlanController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        calendarView.setLogoutController(logoutController);
        showPlansView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Smart Planner");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
