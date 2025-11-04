using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RCLUtilidades.Data.Interfaces;
using RCLUtilidades.Data.DTO;

namespace RCLUtilidades.Data.Services
{
    public class EnergiaService : IEnergiaService
    {
       public async Task<IEnumerable<Energia>>? LoadEnergiaAsync() //nao fica parado a espera dos dados com o metedo async (assincronos) 
       {
           await Task.Delay(1000); //Task é uma classe que representa uma operação assíncrona no pedido a BD ou API tem de ter o await para garantir que recebermos os dados

            var energias = new[] //substituir pela chamada a uma API ou base de dados
            {
                new Energia("Eletricidade", "0.15", "kWh"),
                new Energia("Gás", "0.08", "m³"),
                new Energia("Gasóleo", "1.69", "lt"),
                new Energia("Carvão", "0.05", "kg"),
            };
            return energias;
        }
    }
}
