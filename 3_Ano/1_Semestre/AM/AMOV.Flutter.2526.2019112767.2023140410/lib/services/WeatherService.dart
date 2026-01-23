import 'dart:convert';
import 'package:http/http.dart' as http;

class WeatherService {
  static Future<double> getCurrentTemperature(double lat, double lon) async {
    await Future.delayed(const Duration(seconds: 3));
    try {
      final url = Uri.parse(
          'https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true'
      );

      final response = await http.get(url);

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        // o Open-Meteo devolve a temperatura dentro de current_weather
        return data['current_weather']['temperature'].toDouble();
      } else {
        throw Exception('Falha ao carregar meteorologia');
      }
    } catch (e) {
      throw Exception('Erro de rede: $e');
    }
  }
}