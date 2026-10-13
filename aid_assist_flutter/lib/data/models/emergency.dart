import 'package:flutter/material.dart';

enum EmergencySeverity { critical, urgent, firstAid }

class EmergencyStep {
  const EmergencyStep({
    required this.titleMap,
    required this.instructionMap,
    required this.descriptionMap,
    this.illustration = 'guide',
  });

  final Map<String, String> titleMap;
  final Map<String, String> instructionMap;
  final Map<String, String> descriptionMap;
  final String illustration;

  String getTitle(String lang) => titleMap[lang] ?? titleMap['en'] ?? '';
  String getInstruction(String lang) => instructionMap[lang] ?? instructionMap['en'] ?? '';
  String getDescription(String lang) => descriptionMap[lang] ?? descriptionMap['en'] ?? '';
}

class Emergency {
  const Emergency({
    required this.id,
    required this.titleMap,
    required this.categoryMap,
    required this.severity,
    required this.icon,
    required this.summaryMap,
    required this.immediateActionMap,
    required this.steps,
    required this.doListMap,
    required this.dontListMap,
    required this.escalationMap,
    required this.keywordsMap,
  });

  final String id;
  final Map<String, String> titleMap;
  final Map<String, String> categoryMap;
  final EmergencySeverity severity;
  final IconData icon;
  final Map<String, String> summaryMap;
  final Map<String, String> immediateActionMap;
  final List<EmergencyStep> steps;
  final Map<String, List<String>> doListMap;
  final Map<String, List<String>> dontListMap;
  final Map<String, String> escalationMap;
  final Map<String, List<String>> keywordsMap;

  String getTitle(String lang) => titleMap[lang] ?? titleMap['en'] ?? '';
  String getCategory(String lang) => categoryMap[lang] ?? categoryMap['en'] ?? '';
  String getSummary(String lang) => summaryMap[lang] ?? summaryMap['en'] ?? '';
  String getImmediateAction(String lang) => immediateActionMap[lang] ?? immediateActionMap['en'] ?? '';
  String getEscalation(String lang) => escalationMap[lang] ?? escalationMap['en'] ?? '';
  List<String> getDoList(String lang) => doListMap[lang] ?? doListMap['en'] ?? const [];
  List<String> getDontList(String lang) => dontListMap[lang] ?? dontListMap['en'] ?? const [];
  List<String> getKeywords(String lang) => keywordsMap[lang] ?? keywordsMap['en'] ?? const [];

  bool matches(String query) => matchesQuery(query, 'en');

  bool matchesQuery(String query, String lang) {
    final q = query.toLowerCase().trim();
    if (q.isEmpty) return true;

    final title = getTitle(lang).toLowerCase();
    final category = getCategory(lang).toLowerCase();
    final summary = getSummary(lang).toLowerCase();
    final keywords = getKeywords(lang).map((k) => k.toLowerCase()).toList();

    // Cross-check English fallback as well for universal search convenience
    final enTitle = getTitle('en').toLowerCase();
    final enKeywords = getKeywords('en').map((k) => k.toLowerCase()).toList();

    return title.contains(q) ||
        category.contains(q) ||
        summary.contains(q) ||
        keywords.any((w) => w.contains(q)) ||
        enTitle.contains(q) ||
        enKeywords.any((w) => w.contains(q));
  }
}

class Hospital {
  const Hospital({
    required this.name,
    required this.address,
    required this.phone,
    required this.latitude,
    required this.longitude,
    this.mapsUri,
    this.rating,
    this.open,
  });

  final String name;
  final String address;
  final String phone;
  final double latitude;
  final double longitude;
  final String? mapsUri;
  final double? rating;
  final bool? open;
}
