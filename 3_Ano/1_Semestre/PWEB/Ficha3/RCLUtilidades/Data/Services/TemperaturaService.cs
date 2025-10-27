using RCLUtilidades.Data.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Services
{
    public class TemperaturaService : ITemperaturaService
    {
        public decimal ValorCelsius { get; set; }
        public decimal ValorFahrenheit { get; set; }
        public decimal ValorKelvin { get; set; }
    }
}
