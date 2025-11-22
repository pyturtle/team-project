package use_case.message;

public class SendMessageOutputData {
    private final String userMessage;

    public SendMessageOutputData(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
