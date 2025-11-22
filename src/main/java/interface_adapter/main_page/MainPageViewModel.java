package interface_adapter.main_page;

import interface_adapter.ViewModel;

public class MainPageViewModel extends ViewModel<String> {
    public MainPageViewModel() {
        super("main page");
        setState("");
    }
}
