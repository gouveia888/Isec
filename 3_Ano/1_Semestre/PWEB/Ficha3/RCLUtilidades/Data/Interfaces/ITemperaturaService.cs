using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Interfaces
{
    public interface ITemperaturaService
    {
        decimal ValorCelsius { get; set; }
        decimal ValorFahrenheit { get; set; }
        decimal ValorKelvin { get; set; }
    }
}
