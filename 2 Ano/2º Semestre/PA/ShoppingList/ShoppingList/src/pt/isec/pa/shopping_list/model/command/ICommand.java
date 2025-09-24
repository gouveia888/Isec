package pt.isec.pa.shopping_list.model.command;

public interface ICommand {
    boolean execute();
    boolean undo();
}
