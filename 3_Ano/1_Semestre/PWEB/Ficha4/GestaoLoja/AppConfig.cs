using GestaoLoja.Data;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace GestaoLoja
{
    public static class AppConfig
    {
        // Método de extensão que adiciona o Seed Data ao pipeline de execução
        public static async Task SeedDatabaseAsync(this IHost host)
        {
            using (var scope = host.Services.CreateScope())
            {
                var services = scope.ServiceProvider;

                try
                {
                    // Obtém os serviços necessários
                    var context = services.GetRequiredService<ApplicationDbContext>();
                    var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
                    var roleManager = services.GetRequiredService<RoleManager<IdentityRole>>();

                    // Chama o método estático de Inicialização (o que criamos no 3.g)
                    await Inicialização.SeedDatabaseAsync(context, userManager, roleManager);
                }
                catch (Exception ex)
                {
                    var logger = services.GetRequiredService<ILogger<Program>>();
                    logger.LogError(ex, "Ocorreu um erro durante a inicialização da base de dados (Seed).");
                }
            }
        }
    }
}