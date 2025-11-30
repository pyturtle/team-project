package interface_adapter;

/**
 * PartialViewModel stores the name of the currently active partial view.
 * It extends ViewModel with a String state representing the view identifier.
 */
public class PartialViewModel extends ViewModel<String> {

    /**
     * Creates a new PartialViewModel with the default name
     * and an empty string as its initial state.
     */
    public PartialViewModel() {
        super("partial view manager");
        this.setState("");
    }
}
