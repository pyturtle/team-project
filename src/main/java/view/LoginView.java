package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.remember_me.RememberMe;
import entity.SavedUser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;
    private final RememberMe rememberMeUseCase;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JCheckBox rememberMeCheckbox;

    private final JButton logIn;
    private final JButton signUp;
    private LoginController loginController = null;

    public LoginView(LoginViewModel loginViewModel, RememberMe rememberMeUseCase) {
        this.loginViewModel = loginViewModel;
        this.rememberMeUseCase = rememberMeUseCase;
        this.loginViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        final JPanel checkboxes = new JPanel();
        rememberMeCheckbox = new JCheckBox("Remember Me");
        checkboxes.add(rememberMeCheckbox);

        final JPanel buttons = new JPanel();
        logIn = new JButton("log in");
        buttons.add(logIn);
        signUp = new JButton("sign up");
        buttons.add(signUp);

        loadSavedCredentials();

        logIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            performLogin();
                        }
                    }
                }
        );

        signUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (loginController != null) {
                            loginController.switchToSignupView();
                        }
                    }
                }
        );

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(usernameErrorField);
        this.add(passwordInfo);
        this.add(passwordErrorField);
        this.add(checkboxes);
        this.add(buttons);

        //We have to wait a bit for the auto login to work
        SwingUtilities.invokeLater(() -> {
            Timer timer = new Timer(1000, e -> attemptAutoLogin());
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void loadSavedCredentials() {
        SavedUser savedUser = rememberMeUseCase.loadCredentials();
        if (savedUser != null) {
            usernameInputField.setText(savedUser.getUsername());
            passwordInputField.setText(savedUser.getPassword());
            rememberMeCheckbox.setSelected(true);

            LoginState currentState = loginViewModel.getState();
            currentState.setUsername(savedUser.getUsername());
            currentState.setPassword(savedUser.getPassword());
            currentState.setRememberMe(true);
            loginViewModel.setState(currentState);
        }
    }

    private void attemptAutoLogin() {
        if (loginController != null && rememberMeCheckbox.isSelected()) {
            final LoginState currentState = loginViewModel.getState();
            if (!currentState.getUsername().isEmpty() && !currentState.getPassword().isEmpty()) {
                System.out.println("Performing automatic login for: " + currentState.getUsername());
                loginController.execute(
                        currentState.getUsername(),
                        currentState.getPassword()
                );
            }
        }
    }

    private void performLogin() {
        final LoginState currentState = loginViewModel.getState();

        rememberMeUseCase.saveCredentials(
                currentState.getUsername(),
                currentState.getPassword(),
                rememberMeCheckbox.isSelected()
        );

        loginController.execute(
                currentState.getUsername(),
                currentState.getPassword()
        );
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);

        if (state.getLoginError() != null) {
            usernameErrorField.setText(state.getLoginError());
            usernameErrorField.setForeground(Color.RED);
        } else {
            usernameErrorField.setText("");
        }
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        rememberMeCheckbox.setSelected(state.isRememberMe());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}