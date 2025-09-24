package App;

public class Battery {
    private int level; // de 0 a 100

    public Battery(int initialLevel) {
        if (initialLevel < 0 || initialLevel > 100)
            throw new IllegalArgumentException("Initial level must be between 0 and 100.");
        this.level = initialLevel;
    }

    public int getLevel() {
        return level;
    }

    public void charge(int amount) {
        if (amount < 0) return;
        level = Math.min(100, level + amount);
    }

    public void discharge(int amount) {
        if (amount < 0) return;
        level = Math.max(0, level - amount);
    }

    public boolean isEmpty() {
        return level == 0;
    }

    public boolean isFull() {
        return level == 100;
    }
}
