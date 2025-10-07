using Microsoft.VisualBasic;

public class ex2
{
    public static void Main()
    {
        string[] ListaUm = {  "C#", "Aprender C#","ASP.NET MVC com C#",
        "Entity Framework","Bootstrap","Identity",
        "Lambda","Delegates","Linq","POO com C# "
        };



        string[] ListaDois = { "C#", "ASP.NET MVC", "Linq", "Lambda e C#" };
        int[] Numeros = { 10, 23, 54, 77, 85, 12, 1, 4, 53 };


        var ListaA = new List<string>(ListaUm);

        //Demos usar Extension Methods 

        //ex A)
        Console.Write("Ex A)\n");
        var str = ListaA.Order();
        foreach( var item in str )
            Console.Write(item+"\n");

        //ex B)
        Console.Write("\nEx B)\n");
        var ListaB = new List<string>(ListaUm);
        var str2 = ListaB.Where(s => s.Length < 6);
        foreach (var item in str2)
            Console.Write(item + "\n");

        //ex C)
        Console.Write("\nEx C)\n");
        var ListaC = new List<string>(ListaUm);
        var str3 = ListaC.Where(s => s.Contains("C#")).OrderBy(w => w);
        Console.WriteLine(str3.Count() + " versão 1");
        var str3_1 = ListaC.Count(x => x.Contains("C#"));
        Console.WriteLine(str3_1 + " versão 2\n");

        //ex D)
        Console.Write("\nEx D)\n");
        var num = ListaUm.Select(s => s.Split(' ', StringSplitOptions.RemoveEmptyEntries).Length);
        var num_v2d = ListaUm.Select(x => x.Trim().Split(' ').Count()); //Em cada string da ListaUm, remove os espaços em branco no início e no fim (Trim), divide a string em palavras com base nos espaços (Split(' ')) e conta o número de palavras resultantes (Count()).
        foreach (var item in num)
        {
            Console.WriteLine(item);
        }

        //ex E)
        Console.Write("\nEx E)\n");
        var num2 = Numeros.Average();
        Console.WriteLine("{0}",num2 + "\n");

        //ex F)
        Console.Write("\nEx F)\n");
        Console.WriteLine(Numeros.Max() + "\n");

        //ex G)
        Console.Write("\nEx G)\n");
        var num3 = Numeros.Where(n => n > 0 && n < 26);
        foreach (var i in num3)
        {
            Console.WriteLine(i+"\t");
        }
        Console.Write("\n");

        //ex H)
        Console.Write("\nEx H)\n");
        var num4 = ListaUm.Intersect(ListaDois);
        foreach (var i in num4)
        {
            Console.WriteLine(i);
        }

        //ex I)
        Console.Write("\nEx I)\n");
        var str4 = ListaUm.Union(ListaDois);
        foreach (var i in str4)
        {
            Console.WriteLine(i);
        }

        //ex J)
        Console.Write("\nEx J)\n");
        IEnumerable<IGrouping<int, int>> solucao = from number in Numeros
                      group number by number % 2; //agrupa os números em dois grupos: Grupo 0 = números pares && Grupo 1 = números ímpares

        foreach (var x in solucao)
        {
            Console.WriteLine();
            Console.Write(x.Key == 0 ? "Numeros Pares: " : "Numeros Impares: "); //é a "chave" do grupo (0 para pares, 1 para ímpares).
            foreach (var y in x) Console.Write(y + " ");
        }

        //ex K)
        Console.Write("\n\nEx K)\n");
        var num5 = Numeros.Where(x => x < 30 ).Aggregate(1,(acc,n)=> acc * n);
        Console.WriteLine(num5);

        //ex L)
        Console.Write("\n\nEx L)\n");
        var strings = ListaUm.Where(s => s.Contains("C#"));
        var strings2 = from s in strings
                       let palavras = s.Split(' ', StringSplitOptions.RemoveEmptyEntries)
                       select new
                       {
                           str = s,
                           sInicial = palavras.First(),
                           sFinal = (palavras.Length > 1) ? palavras.Last() : "Nao tem"
                       };

        foreach (var s in strings2)
            Console.WriteLine("String: " + s.str + "\n\t Primeira Palavra: " + s.sInicial + "\n\t Ultima Palavra: " + s.sFinal);
    }
}


/*
orderBY (t=x).ThenBy(...)
thenby é para se der empate a primeira entao ve a segunda



Select e Split -> split serve para ignorar um dado neste caso ' '  ja o select é o select nao tem muito que saber

var numeroPalavras = lisatUm
                       .Select(s = s.Split(' ', StrignSplitOptions.RemoveEmptyEntries).Lenght);



Intersect e Distinct como o nome diz um devolve o comum nas duas lisatas o outro a diferenca





Agregate -> como o nome diz da para agregar conteudo a um x valor é tipo o do string quando concatnavamos as string java



Select NEW objecto dinamico 








*/