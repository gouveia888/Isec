package pt.isec.pa.teosysdlg.model.data;

/**
 * Represents a data model that manages a critical numeric value.
 * The number stored in this class is a fundamental value that must be
 * carefully managed and maintained throughout the system's lifecycle.
 *
 * @author ans
 * @version 1.0.0
 *
 */
public class Model {
    int number;

    /**
     * Constructs a new Model with the specified number value.
     *
     * @param number The initial value to be stored in this model
     */
    public Model(int number) {
        this.number = number;
    }

    /**
     * Retrieves the current number value stored in this model.
     *
     * @return The current number value
     */
    public int getNumber() {
        return number;
    }

    /**
     * Updates the number value stored in this model.
     *
     * @param number The new number value to be stored
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Compares this Model to another object for equality.
     * Two Model objects are considered equal if they contain the same number value.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;
        return number == model.number;
    }

    /**
     * Returns a hash code value for this Model.
     * The hash code is based on the stored number value.
     *
     * @return A hash code value for this object
     */
    @Override
    public int hashCode() {
        return number;
    }

    /**
     * Returns a string representation of this Model.
     * The string consists of the number value converted to text.
     *
     * @return A string representation of the number value
     */
    @Override
    public String toString() {
        return ""+number;
    }
}
