import 'package:flutter/material.dart';
import '../models/Poi.dart';
import '../services/PrefService.dart';
import '../services/WeatherService.dart';

class POIDetailsScreen extends StatefulWidget {
  final Poi poi;

  const POIDetailsScreen({super.key, required this.poi});

  @override
  State<POIDetailsScreen> createState() => _POIDetailsScreenState();
}

class _POIDetailsScreenState extends State<POIDetailsScreen> {
  bool _isFav = false;
  late Future<double> _weather;

  @override
  void initState() {
    super.initState();
    _checkFavorito();
    _weather = _weatherFuture();
  }

  // 'widget.poi' para aceder aos dados da classe pai
  _checkFavorito() async {
    bool fav = await PrefsService.isFavorito(widget.poi.id);
    if (mounted) {
      setState(() => _isFav = fav);
    }
  }

  Future<double> _weatherFuture() async {
    return await WeatherService.getCurrentTemperature(widget.poi.latitude, widget.poi.longitude);
  }

  _toggleFav() async {
    await PrefsService.toggleFavorito(widget.poi.id);
    await _checkFavorito();

    if(mounted){
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(_isFav ? "Adicionado aos Favoritos" : "Removido dos Favoritos"),
          backgroundColor: _isFav ? Colors.green : Colors.red,
        ),
      );
    }

  }

  @override
  Widget build(BuildContext context) {
    final poi = widget.poi;

    return Scaffold(
      appBar: AppBar(
        title: Text(poi.name),
        actions: [
          IconButton(
            icon: Icon(_isFav ? Icons.favorite : Icons.favorite_border),
            color: _isFav ? Colors.red : null,
            onPressed: _toggleFav,
          )
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            Image.asset(
              poi.image,
              width: double.infinity,
              height: 250,
              fit: BoxFit.cover
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(poi.name, style: Theme.of(context).textTheme.headlineMedium),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      _buildInfoIcon(Icons.access_time, poi.schedule),
                      _buildInfoIcon(Icons.euro, poi.averagePrice),
                    ],
                  ),
                  const Divider(height: 32),
                  Text("Sobre", style: Theme.of(context).textTheme.titleLarge),
                  const SizedBox(height: 8),
                  Text(poi.description, style: const TextStyle(fontSize: 16, height: 1.5)),
                  const Divider(height: 32),
                  _buildInfoIcon(Icons.location_on, poi.location),
                  const SizedBox(height: 32),

                  Text("Meteorologia em Tempo Real", style: Theme.of(context).textTheme.titleLarge),
                  const SizedBox(height: 12),

                  FutureBuilder<double>(
                    future: _weather,
                    builder: (context, snapshot) {
                      if (snapshot.connectionState == ConnectionState.waiting) {
                        return const Center(child: CircularProgressIndicator());
                      }
                      if (snapshot.hasError) {
                        return const Row(
                          children: [
                            Icon(Icons.cloud_off, color: Colors.red),
                            SizedBox(width: 8),
                            Text("Erro ao carregar meteorologia."),
                          ],
                        );
                      }
                      final temp = snapshot.data ?? 0.0;
                      return Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: Colors.blue.shade100,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(color: Colors.blue.shade200),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            Icon(
                              temp > 10 ? Icons.wb_sunny : Icons.ac_unit,
                              color: temp > 10 ? Colors.orange : Colors.white,
                              size: 40,
                            ),
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text("$temp°C", style: const TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
                                const Text("Temperatura Atual"),
                              ],
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  //modelo de icon de informacao
  Widget _buildInfoIcon(IconData icon, String text) {
    return Row(
      children: [
        Icon(icon, size: 20, color: Colors.blueAccent),
        const SizedBox(width: 8),
        Text(text, style: const TextStyle(fontWeight: FontWeight.w500)),
      ],
    );
  }
}