using Newtonsoft.Json.Serialization;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace RESTfullAPI.Entities
{
    public class Categoria
    {
        public int Id { get; set; }
        [Required (ErrorMessage = "O nome da categoria é obrigatorio")]
        public string?  Nome { get; set; }
        public int? Ordem {  get; set; }
        public string? UrlImagem { get; set; }
        public byte[]? Imagem { get; set; }

        [NotMapped]
        public IFormFile? ImagemFile { get; set; }
    }
}
