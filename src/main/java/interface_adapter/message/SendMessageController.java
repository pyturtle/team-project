package interface_adapter.message;

import use_case.message.SendMessageInputBoundary;
import use_case.message.SendMessageInputData;

public class SendMessageController {
    private final SendMessageInputBoundary sendMessageInteractor;

    public SendMessageController(SendMessageInputBoundary sendMessageInputBoundary) {
        sendMessageInteractor = sendMessageInputBoundary;
    }

    public void execute(String userMessage) {
        final SendMessageInputData sendMessageInputData =
                new SendMessageInputData(userMessage);
        sendMessageInteractor.execute(sendMessageInputData);
    }
}
