package pt.isec.Empresa;

abstract class Funcionario {
    protected String nome, matricula;
    protected double salario;

    Funcionario(String matricula, String nome, double salario) {
        this.matricula = matricula;
        this.nome = nome;
        this.salario = salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return "Nome " + nome;
    }

    public String getMatricula() {
        return " Matricula " + matricula;
    }

    abstract double getSalario();
}
