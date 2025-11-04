using GestaoLoja.Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace GestaoLoja.Data
{
    public static class Inicialização
    {
        public static async Task SeedDatabaseAsync(
            ApplicationDbContext context,
            UserManager<ApplicationUser> userManager,
            RoleManager<IdentityRole> roleManager)
        {
            // 1. Aplica Migrações e Cria a BD (se ainda não existir)
            // Isto garante que o schema da BD está atualizado.
            await context.Database.MigrateAsync();

            // ----------------------------------------------------
            // 2. SEED IDENTITY - Criação de Roles e Utilizadores iniciais
            // ----------------------------------------------------

            // Exemplo: Criar a Role "Admin" se não existir
            if (await roleManager.FindByNameAsync("Admin") == null)
            {
                await roleManager.CreateAsync(new IdentityRole("Admin"));
            }

            // Exemplo: Criar o Utilizador Administrador se não existir
            if (await userManager.FindByNameAsync("admin@gestaoloja.pt") == null)
            {
                var adminUser = new ApplicationUser
                {
                    UserName = "admin@gestaoloja.pt",
                    Email = "admin@gestaoloja.pt",
                    EmailConfirmed = true,
                    NomeCompleto = "Administrador da Loja", // Usando o campo adicionado no 3.d
                    // ... outras propriedades
                };

                await userManager.CreateAsync(adminUser, "PasswordSegura123!"); // Defina uma password segura
                await userManager.AddToRoleAsync(adminUser, "Admin");
            }

            // ----------------------------------------------------
            // 3. SEED DOMAIN DATA - Adicionar dados às tabelas da aplicação
            // ----------------------------------------------------

            /* Exemplo: Seeding de Categorias
            if (!await context.Categorias.AnyAsync())
            {
                context.Categorias.AddRange(
                    new Categoria { Nome = "Frutas Frescas" },
                    new Categoria { Nome = "Vegetais" }
                );
                await context.SaveChangesAsync();
            }

            // Exemplo: Seeding de Modos de Entrega
            if (!await context.Entrega.AnyAsync())
            {
                context.Entrega.AddRange(
                    new Entrega { Nome = "Levantamento na Loja" },
                    new Entrega { Nome = "Entrega ao Domicílio" }
                );
                await context.SaveChangesAsync();
            }*/

            // NOTE: O Seeding de Produtos pode depender das Categorias criadas acima.
        }
    }
}