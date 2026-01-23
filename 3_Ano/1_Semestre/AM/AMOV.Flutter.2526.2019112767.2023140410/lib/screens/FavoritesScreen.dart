import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/CityData.dart';
import '../screens/PoiDetailsScreen.dart';
import '../models/Poi.dart';
import '../services/DataService.dart';

class FavoritesScreen extends StatefulWidget {
  const FavoritesScreen({super.key});

  @override
  State<FavoritesScreen> createState() => _FavoritesScreenState();
}

class _FavoritesScreenState extends State<FavoritesScreen> {
  List<Poi> _favoritePois = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadFavorites();
  }

  // carrega os IDs das SharedPreferences e filtra os objetos Poi
  Future<void> _loadFavorites() async {
    try {
      // Obter todos os dados do JSON atraves do Service
      final cityData = await DataService.loadCityData();

      // Obter os IDs das SharedPreferences
      final prefs = await SharedPreferences.getInstance();
      final List<String> favIds = prefs.getStringList('favoritos') ?? [];

      setState(() {
        _favoritePois = CityData.filterFavorites(cityData.categories, favIds);
        _isLoading = false;
      });
    } catch (e) {
      debugPrint("Erro ao carregar favoritos: $e");
      setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Os Meus Favoritos")),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _favoritePois.isEmpty
          ? const Center(child: Text("Ainda não tens favoritos."))
          : ListView.builder(
        itemCount: _favoritePois.length,
        itemBuilder: (context, index) {
          final poi = _favoritePois[index];
          return ListTile(
            leading: ClipRRect(
              borderRadius: BorderRadius.circular(8),
              child: Image.asset(poi.image, width: 50, height: 50, fit: BoxFit.cover),
            ),
            title: Text(poi.name),
            subtitle: Text(poi.location),
            trailing: const Icon(Icons.arrow_forward_ios, size: 16),
            onTap: () async {
              await Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => POIDetailsScreen(poi: poi)),
              );
              _loadFavorites();
            },
          );
        },
      ),
    );
  }
}