using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace GestaoLoja.Data
{
    public class ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : IdentityDbContext<ApplicationUser>(options)
    {

        public DbSet<GestaoLoja.Entities.Produtos> Produtos { get; set; } //definiçao das classes que vao virar tabelas na base de dados
        public DbSet<GestaoLoja.Entities.Categorias> Categorias { get; set; }
        public DbSet<GestaoLoja.Entities.ModoEntrega> ModoEntrega { get; set; }
    }

}
