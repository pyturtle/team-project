package app;

import data_access.*;
import entity.user.UserFactory;
import entity.plan.PlanFactory;
import entity.subgoal.SubgoalBuilder;
import entity.subgoal.SubgoalFactory;
import interface_adapter.DialogManagerModel;
import interface_adapter.PartialViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.plan.delete_plan.DeletePlanController;
import interface_adapter.plan.delete_plan.DeletePlanPresenter;
import interface_adapter.plan.generate_plan.GeneratePlanController;
import interface_adapter.plan.generate_plan.GeneratePlanPresenter;
import interface_adapter.plan.generate_plan.GeneratePlanViewModel;
import interface_adapter.plan.save_plan.SavePlanController;
import interface_adapter.plan.save_plan.SavePlanPresenter;
import interface_adapter.plan.save_plan.SavePlanViewModel;
import interface_adapter.plan.show_plan.ShowPlanController;
import interface_adapter.plan.show_plan.ShowPlanPresenter;
import interface_adapter.plan.show_plan.ShowPlanViewModel;
import interface_adapter.plan.show_plans.ShowPlansController;
import interface_adapter.plan.show_plans.ShowPlansPresenter;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import interface_adapter.show_subgoal.ShowSubgoalController;
import interface_adapter.show_subgoal.ShowSubgoalPresenter;
import interface_adapter.show_subgoal.ShowSubgoalViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.subgoal_qna.SubgoalQnaController;
import interface_adapter.subgoal_qna.SubgoalQnaPresenter;
import interface_adapter.subgoal_qna.SubgoalQnaViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.plan.delete_plan.DeletePlanInputBoundary;
import use_case.plan.delete_plan.DeletePlanInteractor;
import use_case.plan.delete_plan.DeletePlanOutputBoundary;
import use_case.plan.generate_plan.GeneratePlanInputBoundary;
import use_case.plan.generate_plan.GeneratePlanInteractor;
import use_case.plan.generate_plan.GeneratePlanOutputBoundary;
import use_case.plan.save_plan.SavePlanInputBoundary;
import use_case.plan.save_plan.SavePlanInteractor;
import use_case.plan.save_plan.SavePlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanInputBoundary;
import use_case.plan.show_plan.ShowPlanInteractor;
import use_case.plan.show_plan.ShowPlanOutputBoundary;
import use_case.plan.show_plans.ShowPlansInputBoundary;
import use_case.plan.show_plans.ShowPlansInteractor;
import use_case.plan.show_plans.ShowPlansOutputBoundary;
import use_case.remember_me.RememberMe;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalInputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalInteractor;
import use_case.subgoal.show_subgoal.ShowSubgoalOutputBoundary;
import use_case.subgoal.qna.SubgoalQnaDataAccessInterface;
import use_case.subgoal.qna.SubgoalQnaInputBoundary;
import use_case.subgoal.qna.SubgoalQnaInteractor;
import use_case.subgoal.qna.SubgoalQnaOutputBoundary;
import use_case.subgoal.qna.SubgoalQnaGeminiDataAccessInterface;
import view.*;
import view.plan.GeneratePlanView;
import view.plan.SavePlanView;
import view.plan.ShowPlanView;
import view.plan.ShowPlansView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import interface_adapter.filter_subgoals.FilterSubgoalsPresenter;
import use_case.filter_subgoals.FilterSubgoalsInputBoundary;
import use_case.filter_subgoals.FilterSubgoalsInteractor;

// Delete Plan functionality added
public class AppBuilder {
    final UserFactory userFactory = new UserFactory();
    final SubgoalFactory subgoalFactory = new SubgoalFactory();
    final PlanFactory planFactory = new PlanFactory();

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private final HashMap<String, JPanel> dialogViews = new HashMap<>();
    final DialogManagerModel dialogManagerModel = new DialogManagerModel();
    DialogManager dialogManager = new DialogManager(dialogViews, dialogManagerModel);

    private final HashMap<String, JPanel> partialViews = new HashMap<>();
    private final PartialViewModel partialViewModel = new PartialViewModel();
    PartialViewManager partialViewManager = new PartialViewManager(partialViews, partialViewModel);
    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    final FileSubgoalDataAccessObject subgoalDataAccessObject = new FileSubgoalDataAccessObject(
            "subgoals.json",
            new SubgoalBuilder());
    // DAO version using a shared external database

    // Plan data access object - loads from JSON file
    // To use JSON file: new InMemoryPlanDataAccessObject("plans.json")
    // To use demo data: new InMemoryPlanDataAccessObject()
    final FilePlanDataAccessObject planDataAccessObject = new FilePlanDataAccessObject("plans.json");

    private ShowPlansView showPlansView;
    private ShowPlansViewModel showPlansViewModel;
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    final GeminiApiDataAccessObject generatePlanDataAccessObject = new GeminiApiDataAccessObject();

    private MainPageView  mainPageView;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private GeneratePlanViewModel generatePlanViewModel;
    private ShowPlanViewModel showPlanViewModel;
    private SavePlanViewModel savePlanViewModel;
    private CalendarViewModel calendarViewModel;
    private ShowSubgoalViewModel showSubgoalViewModel;
    private SubgoalQnaViewModel subgoalQnaViewModel;
    private LoginView loginView;
    private GeneratePlanView generatePlanView;
    private ShowPlanView showPlanView;
    private SavePlanView savePlanView;
    private CalendarView calendarView;
    private ShowSubgoalView showSubgoalView;
    private SubgoalQnaView subgoalQnaView;
    private FilterSubgoalsController filterSubgoalsController;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addMainView()
    {
        mainPageView = new MainPageView(viewManagerModel,
                partialViewModel,
                showPlansViewModel,
                partialViewManager);
        cardPanel.add(mainPageView, mainPageView.getViewName());
        return this;
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addShowPlansView() {
        showPlansViewModel = new ShowPlansViewModel();
        showPlansView = new ShowPlansView(showPlansViewModel, savePlanViewModel);
        partialViews.put(showPlansView.getViewName(), showPlansView);
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();

        PreferenceRepository preferenceRepository = new PreferenceRepository();
        RememberMe rememberMeUseCase = new RememberMe(preferenceRepository);

        loginView = new LoginView(loginViewModel, rememberMeUseCase);
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
        calendarView = new CalendarView(calendarViewModel, subgoalDataAccessObject);
        partialViews.put(calendarView.getViewName(), calendarView);
        return this;
    }

    public AppBuilder addSubgoalQnaView() {
        subgoalQnaViewModel = new SubgoalQnaViewModel();
        subgoalQnaView = new SubgoalQnaView(subgoalQnaViewModel);
        dialogViews.put(subgoalQnaView.getViewName(), subgoalQnaView);
        return this;
    }

    public AppBuilder addShowSubgoalView() {
        showSubgoalViewModel = new ShowSubgoalViewModel();
        showSubgoalView = new ShowSubgoalView(showSubgoalViewModel);
        dialogViews.put(showSubgoalView.getViewName(), showSubgoalView);
        return this;
    }

    public AppBuilder addGeneratePlanView() {
        generatePlanViewModel = new GeneratePlanViewModel();
        generatePlanView = new GeneratePlanView(generatePlanViewModel);
        partialViews.put(generatePlanView.getViewName(), generatePlanView);
        return this;
    }

    public AppBuilder addShowPlanView() {
        showPlanViewModel = new ShowPlanViewModel();
        showPlanView = new ShowPlanView(showPlanViewModel);
        dialogViews.put(showPlanView.getViewName(), showPlanView);
        return this;
    }

    public AppBuilder addSavePlanView() {
        savePlanViewModel = new SavePlanViewModel();
        savePlanView = new SavePlanView(savePlanViewModel);
        dialogViews.put(savePlanView.getViewName(), savePlanView);
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
                loggedInViewModel, loginViewModel, signupViewModel, calendarViewModel, showPlansViewModel,
                showPlanViewModel);
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
        mainPageView.setShowPlansController(showPlansController);
        return this;
    }

    public AppBuilder addFilterSubgoalsUseCase() {
        // Add these imports at the top of AppBuilder if not already there:
        // import interface_adapter.filter_subgoals.FilterSubgoalsController;
        // import interface_adapter.filter_subgoals.FilterSubgoalsPresenter;
        // import use_case.filter_subgoals.FilterSubgoalsInputBoundary;
        // import use_case.filter_subgoals.FilterSubgoalsInteractor;

        final FilterSubgoalsPresenter filterSubgoalsPresenter = new FilterSubgoalsPresenter(calendarViewModel);
        final FilterSubgoalsInputBoundary filterSubgoalsInteractor =
                new FilterSubgoalsInteractor(subgoalDataAccessObject, filterSubgoalsPresenter);
        filterSubgoalsController = new FilterSubgoalsController(filterSubgoalsInteractor);

        System.out.println("AppBuilder: Created filterSubgoalsController: " + filterSubgoalsController);

        calendarView.setFilterSubgoalsController(filterSubgoalsController);
        return this;
    }

    /**
     * Adds the Delete Plan Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDeletePlanUseCase() {
        final DeletePlanOutputBoundary deletePlanOutputBoundary = new DeletePlanPresenter(showPlansViewModel, calendarViewModel);

        final DeletePlanInputBoundary deletePlanInteractor =
                new DeletePlanInteractor(planDataAccessObject, deletePlanOutputBoundary, subgoalDataAccessObject);

        final DeletePlanController deletePlanController = new DeletePlanController(deletePlanInteractor);
        showPlansView.setDeletePlanController(deletePlanController);
        return this;
    }

    public AppBuilder addGeneratePlanUseCase() {
        final GeneratePlanOutputBoundary generatePlanOutputBoundary = new GeneratePlanPresenter(generatePlanViewModel);
        final GeneratePlanInputBoundary generatePlanInteractor = new GeneratePlanInteractor(
                generatePlanDataAccessObject, generatePlanOutputBoundary);
        GeneratePlanController generatePlanController = new GeneratePlanController(generatePlanInteractor);
        generatePlanView.setGeneratePlanController(generatePlanController);
        return this;
    }

    public AppBuilder addSavePlanUseCase() {
        final SavePlanOutputBoundary savePlanOutputBoundary = new SavePlanPresenter(savePlanViewModel,
                dialogManagerModel, calendarViewModel);
        final SavePlanInputBoundary savePlanInteractor = new SavePlanInteractor(savePlanOutputBoundary,
                planDataAccessObject, subgoalDataAccessObject);
        SavePlanController savePlanController = new SavePlanController(savePlanInteractor);
        showPlanView.setSavePlanController(savePlanController);
        return this;
    }

    public AppBuilder addShowPlanUseCase() {
        final ShowPlanOutputBoundary showPlanOutputBoundary = new ShowPlanPresenter(showPlanViewModel,
                dialogManagerModel);
        final ShowPlanInputBoundary showPlanInteractor = new ShowPlanInteractor(showPlanOutputBoundary,
                planDataAccessObject);
        ShowPlanController showPlanController = new ShowPlanController(showPlanInteractor);
        generatePlanView.setShowPlanController(showPlanController);
        return this;
    }

    public AppBuilder addShowSubgoalUseCase() {
        ShowSubgoalOutputBoundary showSubgoalPresenter = new ShowSubgoalPresenter(
                showSubgoalViewModel, dialogManagerModel, calendarViewModel);

        // Pass FilterSubgoalsController so presenter can re-apply filters after changes
        System.out.println("AppBuilder.addShowSubgoalUseCase: filterSubgoalsController is " +
                         (filterSubgoalsController == null ? "NULL" : "NOT NULL"));
        if (filterSubgoalsController != null) {
            ((ShowSubgoalPresenter) showSubgoalPresenter).setFilterSubgoalsController(filterSubgoalsController);
            System.out.println("AppBuilder: Set filterSubgoalsController on ShowSubgoalPresenter");
        } else {
            System.out.println("AppBuilder: WARNING - filterSubgoalsController is null! Cannot set on presenter.");
        }

        ShowSubgoalInputBoundary showSubgoalInteractor = new ShowSubgoalInteractor(
                subgoalDataAccessObject,
                showSubgoalPresenter);
        ShowSubgoalController showSubgoalController = new ShowSubgoalController(showSubgoalInteractor);

        showSubgoalView.setShowSubgoalController(showSubgoalController);
        calendarView.setShowSubgoalController(showSubgoalController);

        showPlansView.setShowSubgoalController(showSubgoalController);

        return this;
    }

    public AppBuilder addSubgoalQnaUseCase() {
        SubgoalQnaDataAccessInterface subgoalQnaDataAccessObject =
                new FileSubgoalQnaDataAccessObject("subgoal_qna.json");
        SubgoalQnaOutputBoundary subgoalQnaPresenter =
                new SubgoalQnaPresenter(subgoalQnaViewModel, dialogManagerModel);
        SubgoalQnaGeminiDataAccessInterface qnaGeminiGateway = generatePlanDataAccessObject;
        SubgoalQnaInputBoundary subgoalQnaInteractor =
                new SubgoalQnaInteractor(subgoalQnaDataAccessObject,
                        qnaGeminiGateway,
                        subgoalQnaPresenter);
        SubgoalQnaController subgoalQnaController =
                new SubgoalQnaController(subgoalQnaInteractor);
        subgoalQnaView.setSubgoalQnaController(subgoalQnaController);
        showSubgoalView.setQnaController(subgoalQnaController);
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
        mainPageView.setLogoutController(logoutController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Group Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        partialViewModel.setState("CalendarView");
        partialViewModel.firePropertyChange();

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }




}
