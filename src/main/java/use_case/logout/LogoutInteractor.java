package use_case.logout;

import data_access.interfaces.user.LogoutUserDataAccessInterface;

public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {

        String username = userDataAccessObject.getCurrentUsername();


        userDataAccessObject.setCurrentUsername(null);


        LogoutOutputData outputData = new LogoutOutputData(username);


        logoutPresenter.prepareSuccessView(outputData);

        System.out.println("LogoutInteractor: executed logout for " + username);
    }
}
