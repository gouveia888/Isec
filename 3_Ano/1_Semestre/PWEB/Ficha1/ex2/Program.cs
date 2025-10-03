namespace GestaoFuncionario;
class Funcionario
{
    //nao deveria ter salario nem vendas??
    //colocar a class funcionario como abstract e vencimento como abstract tambem
    public double salario { get; set; }
    public string nome { get; }
    public string apelido { get; }
    public int nif { get; }
    public double vendas { get; set; }
    //definir sets e gets manuais para validaçoes de dados
    /*public double vendas {
        get { return vendas; }
        set { 
            if(value < 0)
                throw new ArgumentOutOfRangeException("Salario nao pode ser negativo");
            else
                vendas = value; 
        }

    }*/

    public Funcionario(string nome, string apelido, int nif, double vendas, double salario = 0)
    {
        this.nome = nome;
        this.apelido = apelido;
        this.nif = nif;
        this.vendas = vendas;
        this.salario = vendas;
    }

    public override string ToString()
    {
        return String.Format("Nome: {0}\nApelido: {1}\nNIF: {2}\nVendas: {3}\nSalario: {4}", nome, apelido, nif, vendas, salario);
    }

}

class FuncionarioComissao : Funcionario
{
    public double salarioComVendas { get; set; }
    public FuncionarioComissao(string nome, string apelido, int nif, double salario, double comissao, double vendas) : base(nome, apelido, nif, vendas)
    {
        this.salarioComVendas = (salario * comissao) + vendas;
    }
    public override string ToString()
    {
        return base.ToString() + String.Format("\nSalario com Vendas: {0}\n", salarioComVendas);
    }
}

class FuncionarioHora : Funcionario
{
    public double salarioHora { get; set; }
    public const int horasDia = 8;
    public const double valorizaHoraExtra = 1.5;
    public FuncionarioHora(string nome, string apelido, int nif, double vendas, int dia, double horasTrabalhadas, double valorHora) : base(nome, apelido, nif, vendas)
    {
        if (horasTrabalhadas > horasDia * dia)
        {
            this.salarioHora = ((horasTrabalhadas - (horasDia * dia)) * valorHora * valorizaHoraExtra);
        }

        this.salarioHora += horasDia * valorHora * dia;
    }
    public override string ToString()
    {
        return base.ToString() + String.Format("\nSalario por Hora: {0}\n", salarioHora);
    }
}

public class Program
{
    public static void Main(string[] args)
    {
        List<Funcionario> funcionarios = new List<Funcionario>();

        Funcionario f1 = new Funcionario("Joao", "Silva", 123456789,10000);
        FuncionarioComissao f2 = new FuncionarioComissao("Maria", "Santos", 987654321, 5000, 0.06, 600);
        FuncionarioHora f3 = new FuncionarioHora("Ana", "Costa", 192837465, 0.04, 20, 180, 10);
        
        funcionarios.Add(f1);
        funcionarios.Add(f2);
        funcionarios.Add(f3);

        foreach (var f in funcionarios)
        {
            Console.WriteLine(f.ToString());
            Console.WriteLine("--------------------");
        }   
    }
}