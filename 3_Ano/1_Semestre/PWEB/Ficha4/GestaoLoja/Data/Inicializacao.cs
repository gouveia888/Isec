using Microsoft.AspNetCore.Identity;

namespace GestaoLoja.Data
{
    public class Inicializacao
    {
        public static async Task CriaDadosInicias(
            UserManager<ApplicationUser> userManager,
            RoleManager<IdentityRole> roleManager)
        {
            //Adicionar default roles
            string[] roles = ["Admin", "User", "Gestor"];

            foreach (var role in roles)
            {
                if (!await roleManager.RoleExistsAsync(role))
                {
                    IdentityRole roleRole = new IdentityRole(role);
                    await roleManager.CreateAsync(roleRole);
                }
            }

            //Adicionar default admin user
            var defaultUser = new ApplicationUser
            {
                UserName = "admin@localhost.com",
                Email = "admin@localhost.com",
                Nome = "Administrador",
                Apelido = "Local",
                EmailConfirmed = true,
                PhoneNumberConfirmed = true

            };

            if (userManager.Users.All(u => u.Id != defaultUser.Id))
            {
                var user = await userManager.FindByEmailAsync(defaultUser.Email);
                if (user == null)
                {
                    await userManager.CreateAsync(defaultUser, "Is3C..00");
                    await userManager.AddToRoleAsync(defaultUser, "Admin");
                }
            }

            var existingUser = await userManager.FindByEmailAsync(defaultUser.Email);

            if (existingUser != null)
            {
                if (!await userManager.IsInRoleAsync(existingUser, "Admin"))
                    await userManager.AddToRoleAsync(existingUser, "Admin");
            }
        }
    }
}
