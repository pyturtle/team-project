package use_case.logout;

import data_access.in_memory.InMemoryUserDataAccessObject;
import entity.user.UserFactory;
import entity.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    @Test
    void successTest() {
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        
        UserFactory factory = new UserFactory();
        User user = factory.create("Paul", "password");
        userRepository.save(user);
        userRepository.setCurrentUsername("Paul");

        
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                assertEquals("Paul", user.getUsername());
                assertNull(userRepository.getCurrentUsername());
            }
};

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, successPresenter);
        interactor.execute();
        assertNull(userRepository.getCurrentUsername());
    }

}