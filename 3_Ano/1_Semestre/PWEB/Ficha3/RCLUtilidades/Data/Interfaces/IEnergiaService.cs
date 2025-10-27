using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Interfaces
{
    public interface IEnergiaService
    {
        string TipoEnergia { get; set; }
        decimal ConsumoMensual { get; set; }
        decimal CustoPorUnidad { get; set; }
        decimal CustoTotal { get; }
    }
}
