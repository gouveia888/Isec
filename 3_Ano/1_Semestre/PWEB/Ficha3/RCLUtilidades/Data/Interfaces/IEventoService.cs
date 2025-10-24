using RCLUtilidades.Data.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RCLUtilidades.Data.Interfaces
{
    public interface IEventoService
    {
        Task<IEnumerable<Evento>> GetAllAsync();
        Task AddAsync(string Nome);
        Task ToggleAsync(Guid id);
        Task DeleteAsync(Guid id);
    }
}
