import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';

const supportedLocales = [
  Locale('en'),
  Locale('hi'),
  Locale('kn'),
  Locale('bn'),
  Locale('te'),
  Locale('mr'),
  Locale('ta'),
  Locale('gu'),
  Locale('ml'),
  Locale('ur')
];
const languageNames = <String, String>{
  'en': 'English',
  'hi': 'Hindi',
  'kn': 'Kannada',
  'bn': 'Bengali',
  'te': 'Telugu',
  'mr': 'Marathi',
  'ta': 'Tamil',
  'gu': 'Gujarati',
  'ml': 'Malayalam',
  'ur': 'Urdu'
};

class SettingsState {
  const SettingsState(
      {required this.languageCode,
      required this.onboarded,
      required this.darkMode,
      required this.kitItems,
      required this.contacts,
      required this.name,
      required this.bloodGroup});
  final String languageCode;
  final bool onboarded;
  final bool darkMode;
  final Set<String> kitItems;
  final List<String> contacts;
  final String name;
  final String bloodGroup;
  SettingsState copyWith(
          {String? languageCode,
          bool? onboarded,
          bool? darkMode,
          Set<String>? kitItems,
          List<String>? contacts,
          String? name,
          String? bloodGroup}) =>
      SettingsState(
          languageCode: languageCode ?? this.languageCode,
          onboarded: onboarded ?? this.onboarded,
          darkMode: darkMode ?? this.darkMode,
          kitItems: kitItems ?? this.kitItems,
          contacts: contacts ?? this.contacts,
          name: name ?? this.name,
          bloodGroup: bloodGroup ?? this.bloodGroup);
}

final sharedPreferencesProvider =
    Provider<SharedPreferences>((_) => throw UnimplementedError());
final settingsProvider =
    StateNotifierProvider<SettingsController, SettingsState>(
        (ref) => SettingsController(ref.read(sharedPreferencesProvider)));

class SettingsController extends StateNotifier<SettingsState> {
  SettingsController(this._prefs)
      : super(SettingsState(
            languageCode: _prefs.getString('language') ?? 'en',
            onboarded: _prefs.getBool('onboarded') ?? false,
            darkMode: _prefs.getBool('darkMode') ?? false,
            kitItems: (_prefs.getStringList('kitItems') ?? []).toSet(),
            contacts: _prefs.getStringList('contacts') ?? [],
            name: _prefs.getString('name') ?? '',
            bloodGroup: _prefs.getString('bloodGroup') ?? ''));
  final SharedPreferences _prefs;
  Future<void> setLanguage(String code) async {
    await _prefs.setString('language', code);
    await _prefs.setBool('onboarded', true);
    state = state.copyWith(languageCode: code, onboarded: true);
  }

  Future<void> setDarkMode(bool value) async {
    await _prefs.setBool('darkMode', value);
    state = state.copyWith(darkMode: value);
  }

  Future<void> toggleKit(String id) async {
    final next = {...state.kitItems};
    next.contains(id) ? next.remove(id) : next.add(id);
    await _prefs.setStringList('kitItems', next.toList());
    state = state.copyWith(kitItems: next);
  }

  Future<void> saveProfile(String name, String bloodGroup) async {
    await _prefs.setString('name', name.trim());
    await _prefs.setString('bloodGroup', bloodGroup.trim().toUpperCase());
    state = state.copyWith(
        name: name.trim(), bloodGroup: bloodGroup.trim().toUpperCase());
  }

  Future<void> addContact(String name, String phone) async {
    if (name.trim().isEmpty || phone.trim().isEmpty) return;
    final next = [...state.contacts, '${name.trim()}|${phone.trim()}'];
    await _prefs.setStringList('contacts', next);
    state = state.copyWith(contacts: next);
  }

  Future<void> removeContact(String contact) async {
    final next = state.contacts.where((item) => item != contact).toList();
    await _prefs.setStringList('contacts', next);
    state = state.copyWith(contacts: next);
  }
}
