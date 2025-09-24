package pt.isec.pa.shopping_list;

import pt.isec.pa.shopping_list.model.ShoppingListManager;
import pt.isec.pa.shopping_list.ui.ShoppingListUI;

public class Main {
    public static void main(String[] args) {
        ShoppingListManager sm = new ShoppingListManager();
        ShoppingListUI ui = new ShoppingListUI(sm);
        ui.start();
    }
}
