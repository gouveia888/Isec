using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RCLUtilidades.Data.Interfaces;
using RCLUtilidades.Data.DTO;   


namespace RCLUtilidades.Data.Services
{
    public class EventoService : IEventoService
    {
        private readonly List<Evento> _eventos = [];

        public Task<IEnumerable<Evento>> GetAllAsync()
        {
            return Task.FromResult(_eventos.AsEnumerable());
        }

        public Task AddAsync(string nome) {
            _eventos.Add(new Evento { Nome = nome });
            return Task.CompletedTask;
        }

        public Task ToggleAsync(Guid id) {
            var evento = _eventos.FirstOrDefault(e => e.Id == id); //precorre a lista e devolve o objeto com o id correspondente
            if (evento is not null) {
                evento.Concluido = !evento.Concluido;
            }
            return Task.CompletedTask;
        }

        public Task DeleteAsync(Guid id) {
            var evento = _eventos.FirstOrDefault(e => e.Id == id);
            if (evento is not null) {
                _eventos.Remove(evento);
            }
            return Task.CompletedTask;
        }
    }
}
