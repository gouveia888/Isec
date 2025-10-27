using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using RCLUtilidades.Data.Interfaces;
using RCLUtilidades.Data.Services;

namespace RCLUtilidades.Data
{
    public static class ServiceCollectionExtensions
    {
        public static IServiceCollection AddUtilidadddesServices(this IServiceCollection services)
        {
            services.AddScoped<ITemperaturaService, TemperaturaService>();
            services.AddScoped<IEventoService, EventoService>();
            services.AddScoped<INoticiaService, NoticiaService>();
            return services;
        }
    }
}
