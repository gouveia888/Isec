package pt.isec.pa.shopping_list.model;

import pt.isec.pa.shopping_list.model.command.AddProductCommand;
import pt.isec.pa.shopping_list.model.command.CommandManager;
import pt.isec.pa.shopping_list.model.command.RemoveProductCommand;
import pt.isec.pa.shopping_list.model.data.ShoppingList;

public class ShoppingListManager {
    ShoppingList sp;
    CommandManager cm;

    public ShoppingListManager() {
        sp = new ShoppingList();
        cm = new CommandManager();
    }

    public boolean addProduct(String name, double qt) {
        return cm.invokeCommand(new AddProductCommand(sp,name,qt));
    }

    public boolean removeProduct(String name, double qt) {
        return cm.invokeCommand(new RemoveProductCommand(sp,name,qt));
    }

    public boolean hasUndo() {
        return cm.hasUndo();
    }

    public boolean undo() {
        return cm.undo();
    }

    public boolean hasRedo() {
        return cm.hasRedo();
    }

    public boolean redo() {
        return cm.redo();
    }

    @Override
    public String toString() {
        return sp.toString();
    }
}
