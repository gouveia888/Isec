using Microsoft.EntityFrameworkCore;
using RESTfullAPI.Data;
using RESTfullAPI.Entities;

namespace RESTfulAPI.Repositories
{
    public class ProdutoRepository : IProdutoRepository
    {
        private readonly ApplicationDbContext dbContext;
        public ProdutoRepository(ApplicationDbContext dbContext)
        {
            this.dbContext = dbContext;
        }
        public async Task<IEnumerable<Produto>> ObterProdutosPorCategoriaAsync(int categoriaId)
        {
            return await dbContext.Produtos
                .Where(p => p.CategoriaId == categoriaId)
                .Include(p =>p.modoEntrega) //o correto
                .Include("categoria")
                .OrderBy(p => p.Nome)
                .ToListAsync();
        }
        public async Task<IEnumerable<Produto>> ObterProdutosPromocaoAsync()
        {
            return await dbContext.Produtos
                .Where(p => p.Promocao == true)
                .ToListAsync();
        }
        public async Task<IEnumerable<Produto>> ObterProdutosMaisVendidosAsync()
        {
            return await dbContext.Produtos
                .Where(p => p.MaisVendido) // é igual a p=> p.MaisVendido == true 
                .OrderBy(p => p.categoria.Ordem)
                .ToListAsync();
        }
        public async Task<Produto> ObterDetalheProdutoAsync(int id)
        {
            var produto = await dbContext.Produtos
                .Include(p => p.categoria)
                .Include(p => p.modoEntrega)
                .FirstOrDefaultAsync(p => p.Id == id);

            if(produto == null)
                throw new KeyNotFoundException($"Produto com Id {id} não encontrado.");

            return produto!;
        }


        public async Task<IEnumerable<Produto>> ObterTodosProdutosAsync()
        {
            return await dbContext.Produtos
                .ToListAsync();
        }
    }
}
