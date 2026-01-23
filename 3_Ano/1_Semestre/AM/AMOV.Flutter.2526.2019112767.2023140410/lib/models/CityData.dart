import 'Category.dart';
import 'Poi.dart';

class CityData {
  final String city;
  final List<Category> categories;

  CityData({required this.city, required this.categories});

  factory CityData.fromJson(Map<String, dynamic> json) {
    var list = json['categories'] as List;
    List<Category> categoryList = list.map((i) => Category.fromJson(i)).toList();

    return CityData(
      city: json['city'],
      categories: categoryList,
    );
  }

  static List<Poi> getFavorites(List<Category> allCategories, List<String> favIds) {
    List<Poi> favoritePois = [];

    for (var category in allCategories) {
      for (var poi in category.points) {
        if (favIds.contains(poi.id.toString())) {
          favoritePois.add(poi);
        }
      }
    }
    return favoritePois;
  }

  static List<Poi> filterFavorites(List<Category> allCategories, List<String> favIds) {
    List<Poi> found = [];
    for (var cat in allCategories) {
      for (var p in cat.points) {
        if (favIds.contains(p.id.toString())) {
          found.add(p);
        }
      }
    }
    return found;
  }
}