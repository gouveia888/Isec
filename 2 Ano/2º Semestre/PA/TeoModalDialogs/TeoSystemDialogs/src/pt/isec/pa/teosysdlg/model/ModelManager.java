package pt.isec.pa.teosysdlg.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import pt.isec.pa.teosysdlg.model.data.Model;

/**
 * Manages the business logic and operations for the Model class.
 * Acts as a facade to handle interactions with the underlying data model.
 *
 * @author ans
 * @version 1.0.0
 *
 * @see Model
 */
public class ModelManager {
    /**
     * Property name for model data changes
     */
    public static final String PROP_MODEL_DATA = "modelData";

    private final Model model;
    private final PropertyChangeSupport pcs;

    /**
     * Constructs a new ModelManager with a default Model initialized to zero.
     */
    public ModelManager() {
        this(new Model(0));
    }

    /**
     * Constructs a new ModelManager with the specified Model.
     *
     * @param model The Model instance to be managed
     */
    public ModelManager(Model model) {
        this.model = model;
        this.pcs = new PropertyChangeSupport(this);
               //Never use the reference to the model as sourceBean; use the reference to the Manager instead
    }

    /**
     * Adds a PropertyChangeListener to this ModelManager.
     * The listener will be notified when the model data changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    public void addListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    /**
     * Returns the current number value from the managed Model.
     *
     * @return The current number value
     */
    public int getNumber() {
        return model.getNumber();
    }

    /**
     * Sets a new number value in the managed Model.
     *
     * @param number The new number value to set
     */
    public void setNumber(int number) {
        int oldValue = model.getNumber();
        model.setNumber(number);
        pcs.firePropertyChange(PROP_MODEL_DATA, oldValue, number);
    }

    /**
     * Returns a string representation of this ModelManager.
     * Delegates to the toString method of the managed Model instance,
     * which returns the number value as a string.
     *
     * @return A string representation of the managed Model's number value
     *
     * @see Model#toString()
     *
     */
    @Override
    public String toString() {
        return model.toString();
    }
}
