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
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST api/<ProdutosController>
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

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
