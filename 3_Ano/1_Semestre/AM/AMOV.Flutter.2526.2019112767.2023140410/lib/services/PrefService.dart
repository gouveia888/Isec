import 'package:shared_preferences/shared_preferences.dart';

class PrefsService {
  static const String _key = 'favoritos';

  // guarda ou remove um ID da lista de favoritos
  static Future<void> toggleFavorito(int id) async {
    final prefs = await SharedPreferences.getInstance();
    List<String> favoritos = prefs.getStringList(_key) ?? [];

    String idStr = id.toString();
    if (favoritos.contains(idStr)) {
      favoritos.remove(idStr);
    } else {
      favoritos.add(idStr);
    }
    await prefs.setStringList(_key, favoritos);
  }

  // verifica se um ID e favorito
  static Future<bool> isFavorito(int id) async {
    final prefs = await SharedPreferences.getInstance();
    List<String> favoritos = prefs.getStringList(_key) ?? [];
    return favoritos.contains(id.toString());
  }
}