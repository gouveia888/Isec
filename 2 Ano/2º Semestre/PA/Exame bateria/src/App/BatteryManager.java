package App;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BatteryManager {
    private Battery battery;
    private PropertyChangeSupport pcs;

    public static final String PROP_LEVEL = "batteryLevel";

    public BatteryManager() {
        battery = new Battery(50); // nível inicial
        pcs = new PropertyChangeSupport(this);
    }

    // Permite a ligação à interface gráfica
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public int getLevel() {
        return battery.getLevel(); // Atenção ao método: deve ser getLevel() no Battery
    }

    // Carrega a bateria
    public void charge() {
        int oldLevel = battery.getLevel();
        battery.charge(10);
        pcs.firePropertyChange(PROP_LEVEL, oldLevel, battery.getLevel());
    }

    // Descarga a bateria
    public void discharge() {
        int oldLevel = battery.getLevel();
        battery.discharge(10);
        pcs.firePropertyChange(PROP_LEVEL, oldLevel, battery.getLevel());
    }

    public void undo(){

    }

}
