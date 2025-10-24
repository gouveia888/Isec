using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.DTO
{
    public class Temperatura
    {
        public DateTime DataHora { get; set; }
        public decimal ValorCelsius { get; set; }
        public decimal ValorFahrenheit { get; set; }
        public decimal ValorKelvin { get; set; }

    }
}
