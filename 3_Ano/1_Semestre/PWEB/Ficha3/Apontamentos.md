🚀 Conceitos a reter

Na declaração de component é indicado o html e ser apresentado e a interaçao com o sistema atraves de `@code{C#}` os dados indicados com `[Parameter]` devem ser indicados como propriedades no uso do component 
Com o uso de [EditorRequired] tornamos obrigatorio a passagem desse dado atraves das propriedades do component
Exemplo: HomeComponent

``` csharp
public Guid Id { get; set; }
```
`Guid` é usado como Chave Primária da entidade, substituindo um número sequencial (int) para fornecer um identificador que é garantidamente único em toda a aplicação

``` csharp
 public interface IEventoService
 {
     Task<IEnumerable<Evento>> GetAllAsync();
     Task AddAsync(string Nome);
     Task ToggleAsync(Guid id);
     Task DeleteAsync(Guid id);
 }
```

 `Task` remete para um metodo assincrono 
 Tipo de retorno que é uma lista enumeravel de objetos Evento (Diferença entre List e IEnumerable é que os dados só são lidos quando a iteração começa ao contrario da List)

 Os serviços implementam a Interface associada a class
 Neste caso usamos uma List para simular um ambiente de BD visto que os dados nao sao guardados

``` csharp
return Task.FromResult(_eventos.AsEnumerable())
```

 Esta a ser retornada uma Task que é criada e devolvida (FromResult) 
 AsEnumerable converte o tipo de dados para IEnumerable, garatindo a assinatura do metedo

``` csharp
var evento = _eventos.FirstOrDefault(e => e.Id == id); //precorre a lista e devolve o objeto com o id correspondente
```

 Task.CompletedTask garante o retorno do metedo confirmando que tudo foi executado corretamente


 Este é o método de ciclo de vida do Blazor. É chamado uma vez, quando o componente é inicializado.

``` csharp
protected override async Task OnInitializedAsync(){
    eventos = await EventoService.GetAllAsync();
}
```

Aqui a aplicação chama o serviço (eventos = await EventoService.GetAllAsync();) para carregar os dados. O await garante que o componente espera pela base de dados antes de tentar renderizar a lista visto que GetAllAsync() é um Task e é assincrono.

``` csharp
 @bind="novoNome"
```
Link com a variavel novoNome

``` csharp
 @bind:event="oninput"
```


 Garante update imediato da variavel ex:
  Se o utilizador digitar "Comprar Leite", a variável C# novoNome será atualizada:

    Para 'C'

    Para 'Co'

    Para 'Com'

    ...

    Até 'Comprar Leite'

A lógica implementada protege a aplicação porque a Interface limita o que a UI pode pedir (so tem acesso as metedos da interface), e os DTOs limitam o que a UI pode ver(so tem acesso as propriedades do DTO). A Camada de Services é o intermediário seguro que implementa os meteods e executa as operações necessárias na base de dados nao expondo dados sensiveis para a UI.

O uso de uma ServiceCollectionExtensions serve para carregar todos os serviços necessarios para o funcionamento da App no Program ou MauiProgram (tem de ser adicionados nos ficheiros)

Aula do Barbosa

Arquitetura onde os serviços se concentram numa API que é a ponte de comunicaçao entre todas as aplicaçoes e a BD

Componente HomeComponent deve ser aplicado em Home no MAUI e no Blazor, com alteraçao da string do componente dependendo da app que estamos a desenvolver

Query sintax para chamar um component um dentro de outro [Query]?? confirmar

namespace RCLUtilidades.Data.DTO cria uma dll com todas as classes que estejam no mesmo namespace

Task é como se fosse uma thread que pode ser parado e retomado enquanto aguardamos pelos dados assincronos e pela chamada a BD com o await