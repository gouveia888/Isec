using Microsoft.EntityFrameworkCore;
using RESTfullAPI.Data;
using RESTfullAPI.Entities; 

namespace RESTfulAPI.Repositories
{
    public class CategoriaRepository : ICategoriaRepository
    {
        private readonly ApplicationDbContext dbContext;

        public CategoriaRepository(ApplicationDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<IEnumerable<Categoria>> GetCategorias()
        {
            return await dbContext.Categorias
                .Where(x => /*x.Imagem != null &&*/ x.Imagem.Length > 0)
                .OrderBy(O => O.Ordem)
                .ThenBy(p => p.Nome)
                .ToListAsync();
        }
    }
}
