package pt.isec.pa.shopping_list.model.command;

import pt.isec.pa.shopping_list.model.data.ShoppingList;

abstract class AbstractCommand implements ICommand {
    protected ShoppingList receiver;

    protected AbstractCommand(ShoppingList receiver) {
        this.receiver = receiver;
    }
}
