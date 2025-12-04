using Microsoft.EntityFrameworkCore;
using RESTfullAPI.Data;
using RESTfullAPI.Entities;

namespace RESTfulAPI.Repositories
{
    public class ProdutoRepository : IProdutoRepository
    {
        private readonly ApplicationDbContext _dbContext;
        public ProdutoRepository(ApplicationDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task<IEnumerable<Produto>> ObterProdutosPorCategoriaAsync(int categoriaId)
        {
            return await _dbContext.Produtos
                .Where(p => p.CategoriaId == categoriaId)
                .Include(p =>p.modoEntrega) //o correto
                .Include("categoria")
                .OrderBy(p => p.Nome)
                .ToListAsync();
        }
        public async Task<IEnumerable<Produto>> ObterProdutosPromocaoAsync()
        {
            return await _dbContext.Produtos
                .Where(p => p.Promocao == true)
                .ToListAsync();
        }
        public async Task<IEnumerable<Produto>> ObterProdutosMaisVendidosAsync()
        {
            return await _dbContext.Produtos
                .Where(p => p.MaisVendido) // é igual a p=> p.MaisVendido == true 
                .OrderBy(p => p.categoria.Ordem)
                .ToListAsync();
        }
        public async Task<Produto> ObterDetalheProdutoAsync(int id)
        {
            var produto = await _dbContext.Produtos
                .Include(p => p.categoria)
                .Include(p => p.modoEntrega)
                .FirstOrDefaultAsync(p => p.Id == id);

            if(produto == null)
                throw new KeyNotFoundException($"Produto com Id {id} não encontrado.");

            return produto!;
        }


        public async Task<IEnumerable<Produto>> ObterTodosProdutosAsync()
        {
            return await _dbContext.Produtos
                .ToListAsync();
        }

        public async Task<Produto> AdicionarProdutoAsync(Produto produto)
        {
            await _dbContext.Produtos.AddAsync(produto);
            try{
                await _dbContext.SaveChangesAsync();
            }
            catch(Exception ex)
            {
                throw new Exception("Erro ao adicionar o produto: " + ex.Message);
            }
            return produto;
        }

        public async Task<Produto> AtualizarProdutoAsync(Produto produto)
        {
            _dbContext.Produtos.Update(produto);
            try
            {
                await _dbContext.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                throw new Exception("Erro ao atualizar o produto: " + ex.Message);
            }
            return produto;
        }

        public async Task<bool> DeletarProdutoAsync(int id)
        {
            var produto = await _dbContext.Produtos.FindAsync(id);
            if (produto == null)
            {
                return false;
            }
            _dbContext.Produtos.Remove(produto);
            try
            {
                await _dbContext.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                throw new Exception("Erro ao deletar o produto: " + ex.Message);
            }
            return true;
        }
    }
}
