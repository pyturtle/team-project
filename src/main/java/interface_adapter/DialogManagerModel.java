package interface_adapter;

/**
 * DialogManagerModel stores the name of the currently active dialog view.
 * It extends ViewModel with a String state representing the dialog identifier.
 */
public class DialogManagerModel extends ViewModel<String> {

    /**
     * Creates a new DialogManagerModel with the default view name
     * and initializes its state to an empty string.
     */
    public DialogManagerModel() {
        super("dialog manager");
        this.setState("");
    }
}
