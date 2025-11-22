package use_case.message;

public class SendMessageInteractor implements SendMessageInputBoundary{
    private final SendMessageOutputBoundary sendMessagePresenter;

    public SendMessageInteractor(SendMessageOutputBoundary sendMessagePresenter) {
        this.sendMessagePresenter = sendMessagePresenter;
    }


    @Override
    public void execute(SendMessageInputData sendMessageInputData) {
        String userMessage = sendMessageInputData.getUserMessage();
        SendMessageOutputData sendMessageOutputData = new SendMessageOutputData(userMessage);
        sendMessagePresenter.prepareSuccessView(sendMessageOutputData);
    }
}
