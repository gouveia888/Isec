using Microsoft.AspNetCore.Identity;

namespace GestaoLoja.Data
{
    // Add profile data for application users by adding properties to the ApplicationUser class
    public class ApplicationUser : IdentityUser
    {
        public string NomeCompleto { get; set; } //definiçao das colunas a usar no Asp.NetUsers que vai adicionar a tabela users 
        public string Morada { get; set; }
    }

}
