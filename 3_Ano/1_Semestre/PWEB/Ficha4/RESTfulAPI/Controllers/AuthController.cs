using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using RESTfullAPI.Data;
using RESTfulAPI.Entities;
using NuGet.Protocol;
using Azure.Core;
using System.Text.Json;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using System.Security.Claims;
using System.IdentityModel.Tokens.Jwt;

namespace RESTfulAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private UserManager<ApplicationUser> _userManager;
        private RoleManager<IdentityRole> _roleManager;
        private SignInManager<ApplicationUser> _signInManager;
        private readonly IConfiguration _configuration;

        public AuthController(
            UserManager<ApplicationUser> userManager,
            RoleManager<IdentityRole> roleManager, 
            SignInManager<ApplicationUser> signInManager,
            IConfiguration configuration
            )
        {
            _userManager = userManager;
            _roleManager = roleManager;
            _signInManager = signInManager;
            _configuration = configuration;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginDto loginData)
        {

            if (!ModelState.IsValid)
                return BadRequest();
            
            var user = await _userManager.FindByEmailAsync(loginData.Email);
            if (user == null)
                return Unauthorized(new {message = "Email ou password inválidos"});

            var result = await _signInManager.CheckPasswordSignInAsync(user, loginData.Password, false);
            if (!result.Succeeded)
                return Unauthorized(new { message = "Email ou password inválidos" });

            var token = await GenerateJwtToken(user);

            return Ok(new AuthResponseDto{
                AccessToken = token,
                TokenType = "Bearer",
                ExpiresIn = 3600,
                Email =  user.Email ?? string.Empty
            });
        }

        private async Task<string> GenerateJwtToken(ApplicationUser user)
        {
            var jwKey = _configuration["Jwt:Key"] ?? throw new InvalidOperationException("JW invalido");
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwKey));

            //Configurar algoritmo de assinatura(HMAC-SHA256)
            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var claims = new List<Claim>
            {
                new Claim(JwtRegisteredClaimNames.Sub, user.Email ?? string.Empty),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                new Claim(ClaimTypes.NameIdentifier, user.Id),
                new Claim(ClaimTypes.Name, user.UserName ?? string.Empty),
                new Claim(ClaimTypes.Email, user.Email ?? string.Empty)
            };

            var userRoles = await _userManager.GetRolesAsync(user);
            foreach (var role in userRoles)
            {
                claims.Add(new Claim(ClaimTypes.Role, role));
            }


            //criar o token JWT
            var token = new JwtSecurityToken(
                issuer: _configuration["Jwt:Issuer"],
                audience: _configuration["Jwt:Audience"],
                claims: claims,
                expires: DateTime.Now.AddHours(1),
                signingCredentials: credentials
            );

            //converter o token para string e retornar  
            return new JwtSecurityTokenHandler().WriteToken(token);

        }
    }
}
