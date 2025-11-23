package interface_adapter;

public class PartialViewModel extends ViewModel<String>{
    public PartialViewModel() {
        super("partial view manager");
        this.setState("");
    }
}
