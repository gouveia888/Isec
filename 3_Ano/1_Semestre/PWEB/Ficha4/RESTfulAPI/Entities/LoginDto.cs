using Microsoft.Build.Framework;
using System.ComponentModel.DataAnnotations;

namespace RESTfulAPI.Entities
{
    public class LoginDto
    {
        // deve ser Required
        [EmailAddress]
        public string Email { get; set; }
        public string Password { get; set; }
    }
}
