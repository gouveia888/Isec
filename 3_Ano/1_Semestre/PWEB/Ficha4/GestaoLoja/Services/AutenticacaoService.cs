namespace GestaoLoja.Services
{
    public class AutenticacaoService
    {
        private readonly HttpClient http;
        public AutenticacaoService(IHttpClientFactory factory)
        {
            http = factory.CreateClient("api");
        }

        public async Task<HttpResponseMessage> Login(string username, string password)
        {
            var credentials = new
            {
                Username = username,
                Password = password
            };
 
            return await http.PostAsJsonAsync("/identity/login", credentials);
        }
    }
}
