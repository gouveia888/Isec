package pt.isec.pa.shopping_list.model.data;

import java.util.ArrayList;

public class ShoppingList {
    private ArrayList<Product> list;

    public ShoppingList() {
        list = new ArrayList<>();
    }

    public boolean addProduct(String name, double qt) {
        if (name!=null && !name.isBlank() && qt>0) {
            list.add(new Product(name, qt));
            return true;
        }
        return false;
    }

    public boolean removeProduct(String name, double qt) {
        return list.remove(new Product(name,qt));
    }

    public double getQuantity(String name) {
        for(Product p : list)
            if (p.name().equals(name))
                return p.qt();
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shopping List:\n");
        for(Product p : list) sb.append("\t- ").append(p).append("\n");
        return sb.toString();
    }
}
