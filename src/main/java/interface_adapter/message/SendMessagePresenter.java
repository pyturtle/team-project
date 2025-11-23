package interface_adapter.message;

import use_case.message.SendMessageOutputBoundary;
import use_case.message.SendMessageOutputData;

public class SendMessagePresenter implements SendMessageOutputBoundary {
    private final SendMessageViewModel sendMessageViewModel;

    public SendMessagePresenter(SendMessageViewModel sendMessageViewModel) {
        this.sendMessageViewModel = sendMessageViewModel;
    }

    @Override
    public void prepareSuccessView(SendMessageOutputData sendMessageOutputData) {
        SendMessageState sendMessageState = sendMessageViewModel.getState();
        sendMessageState.setUserMessage(sendMessageOutputData.getUserMessage());
        sendMessageViewModel.firePropertyChange();
    }
}
