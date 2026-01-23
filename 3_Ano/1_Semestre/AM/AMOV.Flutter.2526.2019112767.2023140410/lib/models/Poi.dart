class Poi {
  final int id;
  final String name;
  final String shortDescription;
  final String description;
  final String image;
  final String schedule;
  final String averagePrice;
  final String location;
  final double latitude;
  final double longitude;


  Poi({
    required this.id,
    required this.name,
    required this.shortDescription,
    required this.description,
    required this.image,
    required this.schedule,
    required this.averagePrice,
    required this.location,
    required this.latitude,
    required this.longitude,
  });

  factory Poi.fromJson(Map<String, dynamic> json) {
    return Poi(
      id: json['id'],
      name: json['name'],
      shortDescription: json['short_description'],
      description: json['description'],
      image: json['image'],
      schedule: json['schedule'],
      averagePrice: json['average_price'],
      location: json['location'],
      latitude: json['lat'],
      longitude: json['lon'],
    );
  }
}

