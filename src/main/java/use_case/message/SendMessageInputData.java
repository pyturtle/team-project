package use_case.message;

public class SendMessageInputData {
    private final String userMessage;

    public SendMessageInputData(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
