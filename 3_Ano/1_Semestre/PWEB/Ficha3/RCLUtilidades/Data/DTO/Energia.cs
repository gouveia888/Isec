using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.DTO
{
    public class Energia
    {
        public string TipoEnergia { get; set; }
        public decimal ConsumoMensal { get; set; }
        public decimal CustoPorUnidad { get; set; }
        public decimal CustoTotal => ConsumoMensal * CustoPorUnidad;
    }
}
