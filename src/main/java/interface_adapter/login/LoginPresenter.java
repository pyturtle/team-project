package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.CalendarState;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.plan.show_plan.ShowPlanState;
import interface_adapter.plan.show_plan.ShowPlanViewModel;
import interface_adapter.plan.show_plans.ShowPlansState;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final CalendarViewModel calendarViewModel;
    private final ShowPlansViewModel showPlansViewModel;
    private final ShowPlanViewModel showPlanViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          interface_adapter.calendar.CalendarViewModel calendarViewModel,
                          ShowPlansViewModel showPlansViewModel,
                          ShowPlanViewModel showPlanViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.calendarViewModel = calendarViewModel;
        this.showPlansViewModel = showPlansViewModel;
        this.showPlanViewModel = showPlanViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {

        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();


        final CalendarState calendarState = calendarViewModel.getCalendarState();
        calendarState.setUsername(response.getUsername());
        calendarViewModel.firePropertyChanged();

        final ShowPlansState showPlansState = showPlansViewModel.getState();
        showPlansState.setUsername(response.getUsername());
        showPlansViewModel.firePropertyChange();

        final ShowPlanState showPlanState = showPlanViewModel.getState();
        showPlanState.setUsername(response.getUsername());


        loginViewModel.setState(new LoginState());


        this.viewManagerModel.setState("main page");
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
