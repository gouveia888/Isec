package pt.isec.Empresa;

public class Desenvolvedor extends Funcionario{

    Desenvolvedor(String matricula, double salario, String nome) {
        super(matricula,nome,salario);
    }


    @Override
    double getSalario(){
        double bonusmes = salario/12;
        return (double)salario+bonusmes;
    }
}