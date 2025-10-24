using RCLUtilidades.Data.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Services
{
    public class NoticiaService : INoticiaService
    {
        public Guid Id { get; set; }
        public string Titulo { get; set; } = string.Empty;
        public string Conteudo { get; set; } = string.Empty;
        public DateTime DataPublicacao { get; set; }
        public string Autor { get; set; } = string.Empty;
    }
}
