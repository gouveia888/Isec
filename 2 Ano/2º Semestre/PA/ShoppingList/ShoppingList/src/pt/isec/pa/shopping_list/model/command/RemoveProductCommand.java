package pt.isec.pa.shopping_list.model.command;

import pt.isec.pa.shopping_list.model.data.ShoppingList;

public class RemoveProductCommand extends AbstractCommand {
    private final String name;
    private final double qt;
    private double old_qt;

    public RemoveProductCommand(ShoppingList receiver, String name, double qt) {
        super(receiver);
        this.name = name;
        this.qt = qt;
    }

    @Override
    public boolean execute() {
        old_qt = receiver.getQuantity(name);
        if (old_qt<0)
            return false;
        receiver.removeProduct(name,old_qt);
        if (old_qt == qt)
            return true;
        return receiver.addProduct(name,old_qt-qt);
    }

    @Override
    public boolean undo() {
        receiver.removeProduct(name,old_qt-qt);
        return receiver.addProduct(name,old_qt);
    }
}
