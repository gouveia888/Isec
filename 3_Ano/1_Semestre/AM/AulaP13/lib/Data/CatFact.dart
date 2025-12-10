class CatFact {
  CatFact.fromJson(Map<String, dynamic> json)
      : fact = json['fact'],
        length = json['length'];
  final String fact;
  final int length;
}

