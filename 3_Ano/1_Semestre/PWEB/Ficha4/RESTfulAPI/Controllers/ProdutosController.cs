using Microsoft.AspNetCore.Mvc;
using RESTfulAPI.Repositories;
using RESTfullAPI.Entities;
using System.Threading.Tasks;

namespace RESTfulAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProdutosController : ControllerBase
    {

        private readonly IProdutoRepository _produtoRepository;

        public ProdutosController(IProdutoRepository produtoRepository)
        {
            _produtoRepository = produtoRepository;
        }

        // GET: api/<ProdutosController>
        [HttpGet]
        public async Task<IEnumerable<Produto>> Get()
        {
            return await _produtoRepository.ObterTodosProdutosAsync();
        }

        // GET api/<ProdutosController>/5
        [HttpGet("detalhes/{id}")]
        public async Task<Produto> Get(int id)
        {
            return await _produtoRepository.ObterDetalheProdutoAsync(id);
        }

        // GET api/<ProdutosController>/5
        [HttpGet("mais-vendidos")]
        public async Task<IEnumerable<Produto>> GetMaisVendidos()
        {
            return await _produtoRepository.ObterProdutosMaisVendidosAsync();
        }


        // GET api/<ProdutosController>/5
        [HttpGet("promocoes")]
        public async Task<IEnumerable<Produto>> GetPromocoes()
        {
            return await _produtoRepository.ObterProdutosPromocaoAsync();
        }

        // GET api/<ProdutosController>/5
        [HttpGet("por-categoria/{categoriaId}")]
        public async Task<IEnumerable<Produto>> GetPorCategoria(int id)
        {
            return await _produtoRepository.ObterProdutosPorCategoriaAsync(id);
        }

        // POST api/<ProdutosController>
        [HttpPost("novo-produto")]
        public async Task Post([FromBody] Produto produto)
        {
            await _produtoRepository.AdicionarProdutoAsync(produto);
        }

        // sem return
        //[HttpPost]
        //public async Task Post([FromBody] Produto produto)
        //{
        //    await _produtoRepository.AdicionarProdutoAsync(produto);
        //}

        // PUT api/<ProdutosController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<ProdutosController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
