import 'Poi.dart';

class Category {
  final String name;
  final List<Poi> points;

  Category({required this.name, required this.points});

  factory Category.fromJson(Map<String, dynamic> json) {
    var list = json['points'] as List;
    List<Poi> pointsList = list.map((i) => Poi.fromJson(i)).toList();

    return Category(
      name: json['name'],
      points: pointsList,
    );
  }
}