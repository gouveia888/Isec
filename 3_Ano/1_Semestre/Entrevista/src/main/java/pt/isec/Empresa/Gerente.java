package pt.isec.Empresa;

public class Gerente extends Funcionario{
    private double participacaoLucros;

    Gerente(String matricula, double participacaoLucros, String nome, double salario) {
        super(matricula,nome,salario);
        this.participacaoLucros = participacaoLucros;
    }

    @Override
    double getSalario() {
        return (double)salario+(salario*(participacaoLucros/100));
    }
}
