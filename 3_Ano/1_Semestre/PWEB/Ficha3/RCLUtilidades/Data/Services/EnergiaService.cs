using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RCLUtilidades.Data.Interfaces;

namespace RCLUtilidades.Data.Services
{
    public class EnergiaService : IEnergiaService
    {
        public string TipoEnergia { get; set; }
        public decimal ConsumoMensual { get; set; }
        public decimal CustoPorUnidad { get; set; }
        public decimal CustoTotal => ConsumoMensual * CustoPorUnidad;
    }
}
