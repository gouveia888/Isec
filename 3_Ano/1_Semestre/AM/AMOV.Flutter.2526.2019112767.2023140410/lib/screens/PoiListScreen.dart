import 'package:amov_flutter/screens/PoiDetailsScreen.dart';
import 'package:flutter/material.dart';
import '../models/CityData.dart';
import '../services/DataService.dart';

class PoiListscreenl extends StatelessWidget {
  const PoiListscreenl({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Pontos de Interesse'),
      ),
      // carregar dados da cidade
      body: FutureBuilder<CityData>(
        future: DataService.loadCityData(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text("Erro: ${snapshot.error}"));
          }

          if (!snapshot.hasData) {
            return const Center(child: Text("Nenhuns dados encontrados."));
          }

          final cityData = snapshot.data!;

          // lista das categorias
          return ListView.builder(
            itemCount: cityData.categories.length,
            itemBuilder: (context, index) {
              final category = cityData.categories[index];

              return ExpansionTile(
                title: Text(
                  category.name,
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                ),
                children: category.points.map((poi) {
                  // item de cada categoria
                  return Card(
                    margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                    child: ListTile(
                      leading: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.asset(
                          poi.image,
                          width: 50,
                          height: 50,
                          fit: BoxFit.cover,
                        ),
                      ),
                      title: Text(poi.name),
                      subtitle: Text(poi.shortDescription),
                      trailing: const Icon(Icons.chevron_right),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => POIDetailsScreen(poi: poi), // Passa o objeto selecionado
                          ),
                        );
                      },
                    ),
                  );
                }).toList(),
              );
            },
          );
        },
      ),
    );
  }
}