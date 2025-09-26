namespace GestaoTarefas;

enum TipoPrioridade
{
    Baixa,
    Media,
    Alta
}
enum TipoCategoria
{
    Trabalho,
    Pessoal,
}

enum TipoEstado
{
    Executar,
    Execucao,
    Concluida
}

class Tarefa
{
    public TipoPrioridade Prioridade { get;} //variaveis
    public TipoCategoria Categoria { get; }
    public TipoEstado Estado { get; }
    public string Nome { get; }
    public DateTime DataRegisto { get; }
    public DateTime DataLimite { get; }
    //construtor da class
    public Tarefa (TipoPrioridade prioridade, TipoCategoria categoria, TipoEstado estado, string nome, DateTime dataRegisto, DateTime dataLimite )
    {
        Prioridade = prioridade; //inicializacao de variaveis
        Categoria = categoria;
        Estado = estado;
        Nome = nome;
        DataRegisto = dataRegisto;
        DataLimite = dataLimite;
    }

    public bool EmAtraso(DateTime agora) => Estado != TipoEstado.Concluida && DateTime.Now > DataLimite;

    public override string ToString(){
        //@ faz quebra de linha
        return String.Format("Nome: {0}, Categoria: {1}, Estado: {2}, Prioridade: {3}, DataRegisto: {4}, DataLimite: {5}", Nome, Categoria,Estado,Prioridade,DataRegisto, DataLimite);
    }
}

class Utilizador
    {
        public string Nome { get; }
        public List <Tarefa> Tarefas { get; set; }
        
        public Utilizador (string Nome){
            this.Nome = Nome;
            Tarefas = new List<Tarefa>();
        }

        public void AddTarefa(Tarefa t){
            Tarefas.Add(t);
        }

    public void TarefasEmAtraso() { 

        foreach(Tarefa t in Tarefas){

            if (t.EmAtraso(DateTime.Now))
                Console.WriteLine("Item em atraso: {0}", t.ToString());
            else
                Console.WriteLine("Item com tempo: {0}", t.ToString());
        }
    }

    public void ListaPrioridade(TipoPrioridade p)
    {
        foreach (Tarefa t in Tarefas.Where(tarefa => tarefa.Prioridade == p)){
            Console.WriteLine(t.ToString());
        }
    }

    public void ListaCategoria(TipoCategoria c)
    {
        foreach (Tarefa t in Tarefas.Where(tarefa => tarefa.Categoria == c))
        {
            Console.WriteLine(t.ToString());
        }
    }

    public void ListaEstado(TipoEstado e)
    {
        foreach (Tarefa t in Tarefas.Where(tarefa => tarefa.Estado == e))
        {
            Console.WriteLine(t.ToString());
        }
    }

    public void RemoveBaixa()
    {
        Tarefas.RemoveAll(t => t.Prioridade == TipoPrioridade.Baixa);
    }

    public void RemovePessoais()
    {
        Tarefas.RemoveAll(t => t.Categoria == TipoCategoria.Pessoal);
    }

    public void RemoveConcluidas()
    {
        Tarefas.RemoveAll(t => t.Estado == TipoEstado.Concluida);
    }

    public void Ficha2Ex3A(string Opcao, bool crescente=true) {

        switch (Opcao.ToLower())
        {
            case "prioridade":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.Prioridade).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.Prioridade).ToList();
                break;
            case "categoria":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.Categoria).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.Categoria).ToList();
                break;
            case "estado":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.Estado).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.Estado).ToList();
                break;
            case "dataregisto":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.DataRegisto).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.DataRegisto).ToList();
                break;
            case "datalimite":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.DataLimite).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.DataLimite).ToList();
                break;
            case "nome":
                if (crescente)
                    Tarefas = Tarefas.OrderBy(t => t.Nome).ToList();
                else
                    Tarefas = Tarefas.OrderByDescending(t => t.Nome).ToList();
                break;
             default:
                Console.WriteLine("Opção inválida.\n Opções: nome, prioridade, categoria, estado, dataregisto, datalimite");
                break;
        }
    }

    public void Ficha2Ex3B() { 
        Tarefas = Tarefas.OrderBy(d => d.DataLimite).ThenBy(p => p.Prioridade).ToList();
        foreach(var t in Tarefas)
            Console.WriteLine(t.ToString() + "\n");
    }
}

class Program
{
    public static void Main(string[] args)
    {
        Utilizador user = new Utilizador("Joao Xia");
        Tarefa t1 = new Tarefa(TipoPrioridade.Alta, TipoCategoria.Trabalho, TipoEstado.Executar, "Tarefa 1", DateTime.Now, DateTime.Now.AddDays(2));
        Tarefa t2 = new Tarefa(TipoPrioridade.Media, TipoCategoria.Pessoal, TipoEstado.Execucao, "Tarefa 2", DateTime.Now, DateTime.Now.AddDays(5));
        Tarefa t3 = new Tarefa(TipoPrioridade.Baixa, TipoCategoria.Trabalho, TipoEstado.Concluida, "Tarefa 3", DateTime.Now, DateTime.Now.AddDays(-1));
        user.AddTarefa(t1);
        user.AddTarefa(t2);
        user.AddTarefa(t3);
        Console.WriteLine("\nFicha 2 Ex3 b): ");
        user.Ficha2Ex3B();
        Console.WriteLine("Tarefas: ");
        user.TarefasEmAtraso();
        Console.WriteLine("\nLista por prioridade: ");
        user.ListaPrioridade(TipoPrioridade.Alta);
        user.ListaPrioridade(TipoPrioridade.Media);
        user.ListaPrioridade(TipoPrioridade.Baixa);
        Console.WriteLine("\nLista por categoria: ");
        user.ListaCategoria(TipoCategoria.Trabalho);
        user.ListaCategoria(TipoCategoria.Pessoal);
        Console.WriteLine("Tarefas por estado: ");
        user.ListaEstado(TipoEstado.Executar);
        user.ListaEstado(TipoEstado.Execucao);
        user.ListaEstado(TipoEstado.Concluida);
        Console.WriteLine("\nRemover tarefas concluídas, pessoais e de baixa prioridade...");
        user.RemoveConcluidas();
        user.RemovePessoais();
        user.RemoveBaixa();
        Console.WriteLine("Tarefas: ");
        user.TarefasEmAtraso();

    }
}