using GestaoLoja.Entities;

namespace GestaoLoja.Services
{
    public class CategoriaService
    {
        private readonly HttpClient http;
        public CategoriaService(IHttpClientFactory factory)
        {
            http = factory.CreateClient("api");
        }   

        public async Task<IEnumerable<Categoria>> GetCategoriasAsync()
        {
            return await http.GetFromJsonAsync<IEnumerable<Categoria>>("api/Categorias");
            
        }

        public async Task<Categoria?> CreateCategoria(Categoria c)
        {
            var response = await http.PostAsJsonAsync("api/Categorias", c);
            if (!response.IsSuccessStatusCode)
            {
                return null;
            }

            return await response.Content.ReadFromJsonAsync<Categoria>();

        }
    }
}
