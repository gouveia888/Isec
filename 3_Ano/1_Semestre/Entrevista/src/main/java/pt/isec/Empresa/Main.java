package pt.isec.Empresa;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() {
        Desenvolvedor dev = new Desenvolvedor("12-ac-23",1000.0,"Tiago");
        Gerente g = new Gerente("12-ac-23",5.0,"Manuel",1000.0);
        List<Funcionario> func = new ArrayList<Funcionario>();
        func.add(dev);
        func.add(g);

        for(Funcionario f : func){
            System.out.println(f.getNome());
            System.out.println(f.getSalario());
        }
    }
}
