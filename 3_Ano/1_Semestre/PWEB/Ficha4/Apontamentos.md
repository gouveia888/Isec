A nivel de segurança temos de validar as transaçoes entre o frontend e a API (frameworkIdentity para login) e depois do login o uso de tokens (jwt) criados pela API
O token vai junto com o pedido e é verificado na API se corresponde ao user correto e se é um token valido

Interatividade global é definida a nivel geral e assumida por todos os componentes, por pagina podemos escolher onde o componente é renderizado onde quisermos e cliente/servidor

Contas individuais cria ja base de dados com Indentity no Program

No AplicationDbContext sao mapeadas as classes a criar na base de dados as tabelas

public DbSet<Categoria> Categorias => Set<Categoria>();

update-database para criar a BD

[NotMapped] //nao vai ser mapeado para a BD
public IFormFile? ImagemFile { get; set; }

No ApplicationUser adicionar propriedades aos users

Program adicionar 
depois de
var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    try
    {
        var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
        var roleManager = services.GetRequiredService<RoleManager<IdentityRole>>();

        await Inicializacao.CriaDadosInicias(userManager, roleManager);
        //Log.Information("Dados iniciais criados com sucesso.");
    }
    catch (Exception ex)
    {
        //Log.Error(ex, "Ocorreu um erro ao criar os dados iniciais.");
        throw;
    }
}


CRIAR CRUD COM sacfolding atraves da class anteriormente criada com o entetity razor component
model class - entidade a selecionar
DBContext - dbcontext de acesso a DB

Alterar no Program de DBContext para DBcontextFactory

Nos formularios criados pelo CRUD
binds para adicionar o conteudo ao modelo

@code {
    [SupplyParameterFromForm]
    private Categoria Categoria { get; set; } = new();

    // To protect from overposting attacks, see https://learn.microsoft.com/aspnet/core/blazor/forms/#mitigate-overposting-attacks.
    private async Task AddCategoria()
    {
        using var context = DbFactory.CreateDbContext();
        context.Categorias.Add(Categoria);
        await context.SaveChangesAsync();
        NavigationManager.NavigateTo("/categoria");
    }
    private string? previewUrl
}

Aviso de erro no formulario
<ValidationSummary class="text-danger" role="alert"/> 
ou entao aviso de erro na class
[Required (ErrorMessage = "O nome da categoria é obrigatorio")]
public string?  Nome { get; set; }


produtos 
inicialize definir 2 variaveis uma que define categorias outra modos de entrega e no formulaio consumir as listas