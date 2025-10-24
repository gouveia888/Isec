using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Interfaces
{
    public interface INoticiaService
    {
        string Titulo { get; set; }
        string Conteudo { get; set; }
        DateTime DataPublicacao { get; set; }
        string Autor { get; set; }
    }
}
