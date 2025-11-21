package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
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
    private final interface_adapter.calendar.CalendarViewModel calendarViewModel;
    private final interface_adapter.show_plans.ShowPlansViewModel showPlansViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          interface_adapter.calendar.CalendarViewModel calendarViewModel,
                          interface_adapter.show_plans.ShowPlansViewModel showPlansViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.calendarViewModel = calendarViewModel;
        this.showPlansViewModel = showPlansViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();

        // Set username in CalendarState and ShowPlansState
        final interface_adapter.calendar.CalendarState calendarState = calendarViewModel.getCalendarState();
        calendarState.setUsername(response.getUsername());
        calendarViewModel.firePropertyChanged();

        final interface_adapter.show_plans.ShowPlansState showPlansState = showPlansViewModel.getState();
        showPlansState.setUsername(response.getUsername());
        showPlansViewModel.firePropertyChange();

        // and clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // switch to the calendar view
        this.viewManagerModel.setState("CalendarView");
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
