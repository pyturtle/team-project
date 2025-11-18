package app;

import data_access.FileUserDataAccessObject;
import data_access.GeminiApiDataAccessObject;
import entity.UserFactory;
import interface_adapter.DialogManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.message.SendMessageController;
import interface_adapter.message.SendMessagePresenter;
import interface_adapter.message.SendMessageViewModel;
import interface_adapter.plan.generate_plan.GeneratePlanController;
import interface_adapter.plan.generate_plan.GeneratePlanPresenter;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.plan.show_plan.ShowPlanController;
import interface_adapter.plan.show_plan.ShowPlanPresenter;
import interface_adapter.plan.show_plan.ShowPlanViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.message.SendMessageInputBoundary;
import use_case.message.SendMessageInteractor;
import use_case.message.SendMessageOutputBoundary;
import use_case.plan.generate_plan.GeneratePlanInputBoundary;
import use_case.plan.generate_plan.GeneratePlanInteractor;
import use_case.plan.generate_plan.GeneratePlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanInputBoundary;
import use_case.plan.show_plan.ShowPlanInteractor;
import use_case.plan.show_plan.ShowPlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanOutputData;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    final UserFactory userFactory = new UserFactory();

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private final JPanel dialogCardPanel = new JPanel();
    private final CardLayout dialogCardLayout = new CardLayout();
    final DialogManagerModel dialogManagerModel = new DialogManagerModel();
    DialogManager dialogManager = new DialogManager(dialogCardPanel, dialogCardLayout, dialogManagerModel);
    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    final GeminiApiDataAccessObject generatePlanDataAccessObject = new GeminiApiDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private GeneratePlanViewModel generatePlanViewModel;
    private SendMessageViewModel sendMessageViewModel;
    private ShowPlanViewModel showPlanViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private GeneratePlanView generatePlanView;
    private ShowPlanView showPlanView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        dialogCardPanel.setLayout(dialogCardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addGeneratePlanView() {
        generatePlanViewModel = new GeneratePlanViewModel();
        sendMessageViewModel = new SendMessageViewModel();
        generatePlanView = new GeneratePlanView(generatePlanViewModel, sendMessageViewModel);
        cardPanel.add(generatePlanView, generatePlanView.getViewName());
        return this;
    }

    public AppBuilder addShowPlanView() {
        showPlanViewModel = new ShowPlanViewModel();
        showPlanView = new ShowPlanView(showPlanViewModel);
        dialogCardPanel.add(showPlanView, showPlanView.getViewName());
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
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addGeneratePlanUseCase() {
        final GeneratePlanOutputBoundary generatePlanOutputBoundary = new GeneratePlanPresenter(generatePlanViewModel);
        final GeneratePlanInputBoundary generatePlanInteractor = new GeneratePlanInteractor(
                generatePlanDataAccessObject, generatePlanOutputBoundary);
        GeneratePlanController generatePlanController = new GeneratePlanController(generatePlanInteractor);

        final SendMessageOutputBoundary sendMessageOutputBoundary = new SendMessagePresenter(sendMessageViewModel);
        final SendMessageInputBoundary sendMessageInteractor = new SendMessageInteractor(sendMessageOutputBoundary);
        SendMessageController sendMessageController = new SendMessageController(sendMessageInteractor);

        final ShowPlanOutputBoundary showPlanOutputBoundary = new ShowPlanPresenter(showPlanViewModel,
                dialogManagerModel);
        final ShowPlanInputBoundary showPlanInteractor = new ShowPlanInteractor(showPlanOutputBoundary);
        ShowPlanController showPlanController = new ShowPlanController(showPlanInteractor);

        generatePlanView.setGeneratePlanController(generatePlanController);
        generatePlanView.setSendMessageController(sendMessageController);
        generatePlanView.setShowPlanController(showPlanController);
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
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Generate Plan Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(generatePlanView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
