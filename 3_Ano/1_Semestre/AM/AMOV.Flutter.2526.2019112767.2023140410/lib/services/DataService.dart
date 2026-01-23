import 'dart:convert';
import 'package:flutter/services.dart';
import '../models/CityData.dart';
import '../models/Poi.dart';

class DataService {
  static Future<CityData> loadCityData() async {
    // carrega o JSON
    final String response = await rootBundle.loadString('data/pontos_turisticos.json');
    final data = json.decode(response);

    // converte os dados no objeto CityData
    return CityData.fromJson(data);
  }
}