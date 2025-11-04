using RCLUtilidades.Data.DTO;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Interfaces
{
    public interface IEnergiaService
    {
       Task<IEnumerable<Energia>>? LoadEnergiaAsync();
       //Task<IEnumerable<Energia>>? CreateEnergiaAsync();
       //Task<IEnumerable<Energia>>? DeleteEnergiaAsync();
       //Task<IEnumerable<Energia>>? EditEnergiaAsync();

    }
}
