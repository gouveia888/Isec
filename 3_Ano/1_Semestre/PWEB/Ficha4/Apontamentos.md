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
campo ordem serve para ordenar no frontend os campos/listas 

---------------------------------------------------------------------------------------------------------------------
Para mostrar erros detalhados adicionar o AddHubOptions (APENAS EM DEBUG)
        builder.Services.AddRazorComponents(
            options => {
                options.DetailedErrors = true;
            }
            )
            .AddInteractiveServerComponents()
            .AddHubOptions(
            options =>{
                options.EnableDetailedErrors = true;
            });

    e adicionar o 

        if (app.Environment.IsDevelopment())
        {
            app.UseMigrationsEndPoint();
            app.UseDeveloperExceptionPage();
        }

No create do crud substituir os ids por opçoes (exemplo Produto)

//Criar variaveis auxiliares para a lista de categorias e de modods de entrega
List<Categoria> Categorias = new();
List<ModoEntrega> ModosdeEntrega = new();

protected override async Task OnInitializedAsync()
{
    using var context = DbFactory.CreateDbContext();
    Categorias = await context.Categorias.OrderBy(c=>c.Ordem).ToListAsync(); //ordenado pela ordem ThenBy para subordenar
    ModosdeEntrega = await context.ModosEntrega.ToListAsync();
}

no formulario substituir o inputnumber por
    <option value="-1">Escolha um modo de entrega</option>
    <InputSelect id="modoentregaid" class="form-select" @bind-Value="@Produto.ModoEntregaId">
        @foreach(var item in ModosdeEntrega)
        {
            <option value="@item.Id"> @item.Nome </option>
        }
    </InputSelect>

Os pedidos de informaçoes a base de dados sao feitos no OnInitializedAsync
Uso de MarkupString para apresentar codigo no meio de uma string

A utilizaçao de base64 para a preview das fotos deve ser utilizada para imagens pequenas
substituir o <PropertyColumn Property="categoria => categoria.Imagem" /> por
   <TemplateColumn Context="categoria">
    @if(categoria.Imagem is not null && categoria.Imagem.Length>0)
    {
        <img src="@($"data:imagem/png;base64,{Convert.ToBase64String(categoria.Imagem)}")"
        alt="@categoria.Nome" style="max-width: 100px; max-height: 100px;" />
    }
    else
    {
        <img src="img/placeholder.png" alt="@categoria.Nome" style="max-width: 100px; max-height: 100px;" />
    }
</TemplateColumn>
para ver o preview da imagem o context e importante para saber a que dados aceder

imagens localmente sem ser em base de dados
    <TemplateColumn Context="produto">
        @if(!string.IsNullOrEmpty(produto.UrlImagem) && produto.UrlImagem.Length>4)
        {
            <img src="@produto.UrlImagem"
            alt="@produto.Nome" style="max-width: 100px; max-height: 100px;" />
        }
        else
        {
            <img src="img/placeholder.png" alt="@produto.Nome" style="max-width: 100px; max-height: 100px;" />
        }
    </TemplateColumn>

Adicionar icons do bootstrap colocar o stylesheet no app razor

Aceder a entidades atraves de outra
IQueryable<Produto> produtos = default!;

protected override void OnInitialized()
{
    context = DbFactory.CreateDbContext();
	produtos = context.Produtos.Include(p => p.categoria).Include( p =>p.modoEntrega).ToList().AsQueryable();
}

Na navbar tudo o que esta dentro do AuthorizeView necessita de o user esta logado para aparecer
tudo o que tiver com o @attribute [Authorize] necessita do login para funcionar

@layout GestaoLoja.Components.Layout.AdminLayout Isso faz com que o conteúdo do Index seja renderizado dentro do AdminLayout.
O AdminLayout.razor por sua vez declara:
@layout MainLayout e contém o HTML do layout de admin. Ou seja a cadeia de renderização é: MainLayout → AdminLayout → conteúdo do Index (Body).

--------------------------------------------------------------------------------

Para protejer o Create Edit e Delete do CRUD

@using Microsoft.AspNetCore.Authorization
@attribute [Authorize (Roles = "Admin")]

e para aplicar a items (esconde)

<AuthorizeView  Roles="Admin">
	<Authorized>
        <a href="@($"categoria/edit?id={categoria.Id}")">
            <input type="button" class="btn btn-dark btn-sm" name="name" value="Editar" />
        </a>
        <a href="@($"categoria/delete?id={categoria.Id}")"><i class="bi bi-trash"></i></a>
    </Authorized>
</AuthorizeView>

AdminRoles e AdminUsers sao componentes razor   

diferença entre @bind = "newRole" e @bind-Value = "Nome"
    bind e uma forma simplificada e automatica de ligar o input com a variavel
    o bind-value serve para quando queremos uma forma explicita do valor associado atraves de um evento