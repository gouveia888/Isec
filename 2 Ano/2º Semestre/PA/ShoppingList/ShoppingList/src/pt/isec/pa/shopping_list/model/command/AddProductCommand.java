package pt.isec.pa.shopping_list.model.command;

import pt.isec.pa.shopping_list.model.data.ShoppingList;

public class AddProductCommand extends AbstractCommand {
    private final String name;
    private final double qt;
    private double new_qt;

    public AddProductCommand(ShoppingList receiver, String name, double qt) {
        super(receiver);
        this.name = name;
        this.qt = qt;
        new_qt = -1.0;
    }

    @Override
    public boolean execute() {
        new_qt = qt;
        double old_qt = receiver.getQuantity(name);
        if (old_qt >= 0.0) {
            receiver.removeProduct(name,old_qt);
            new_qt += old_qt;
        }
        return receiver.addProduct(name,new_qt);
    }

    @Override
    public boolean undo() {
        boolean res=receiver.removeProduct(name,new_qt);
        if (qt != new_qt)
            return receiver.addProduct(name,new_qt-qt);
        return res;
    }
}
