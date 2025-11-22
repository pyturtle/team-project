package interface_adapter.message;

import interface_adapter.ViewModel;

public class SendMessageViewModel extends ViewModel<SendMessageState> {
    public SendMessageViewModel() {
        super("send message");
        setState(new SendMessageState());
    }
}
