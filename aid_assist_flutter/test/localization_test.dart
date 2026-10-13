import 'package:aid_assist_flutter/core/localization/app_strings.dart';
import 'package:aid_assist_flutter/core/settings_controller.dart';
import 'package:aid_assist_flutter/data/repositories/emergency_repository.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  const repository = EmergencyRepository();
  final languages = ['en', 'hi', 'kn', 'bn', 'te', 'mr', 'ta', 'gu', 'ml', 'ur'];

  group('Multilingual Localization Completeness Tests', () {
    test('All 10 supported locales are declared in supportedLocales', () {
      final codes = supportedLocales.map((l) => l.languageCode).toList();
      expect(codes, containsAll(languages));
      expect(codes.length, 10);
    });

    test('AppStrings contains zero missing keys across all 10 locales', () {
      final testKeys = [
        'home', 'hospitals', 'kit', 'profile', 'search', 'offline',
        'clinicalTrauma', 'tagline', 'rapid', 'whistle', 'stopWhistle',
        'flashlight', 'flashOn', 'protocols', 'call112', 'call112Now',
        'all', 'critical', 'urgent', 'firstAid', 'previous', 'next',
        'restart', 'do', 'dont', 'continue', 'chooseLanguage', 'languageHelp',
        'save', 'addContact', 'darkMode', 'useLocation', 'directions',
        'call', 'step', 'of', 'offlineProtocol', 'criticalTimeSensitive',
        'triageLevel', 'nearbyHospitals', 'gpsSub', 'locationInitMsg',
        'locationDeniedMsg', 'gpsDisabledMsg', 'gpsAcquiredMsg',
        'noHospitalsMsg', 'liveHospitalsSortedMsg', 'offlineHospitalFallbackMsg',
        'rating', 'openNow', 'closed', 'kitReadiness', 'emergencyProfile',
        'name', 'bloodGroup', 'contactName', 'phoneNumber', 'readAloud', 'back'
      ];

      for (final lang in languages) {
        final strings = AppStrings.of(lang);
        for (final key in testKeys) {
          final val = strings.get(key);
          expect(val, isNotNull, reason: 'Key "$key" is null for locale "$lang"');
          expect(val.trim(), isNotEmpty, reason: 'Key "$key" is empty for locale "$lang"');
        }
      }
    });

    test('Every Emergency SOP has complete non-empty localized content in all 10 languages', () {
      final emergencies = repository.all;
      expect(emergencies, isNotEmpty);

      for (final emergency in emergencies) {
        for (final lang in languages) {
          expect(emergency.getTitle(lang), isNotEmpty,
              reason: '${emergency.id} title missing for $lang');
          expect(emergency.getCategory(lang), isNotEmpty,
              reason: '${emergency.id} category missing for $lang');
          expect(emergency.getSummary(lang), isNotEmpty,
              reason: '${emergency.id} summary missing for $lang');
          expect(emergency.getImmediateAction(lang), isNotEmpty,
              reason: '${emergency.id} immediateAction missing for $lang');
          expect(emergency.getEscalation(lang), isNotEmpty,
              reason: '${emergency.id} escalation missing for $lang');
          expect(emergency.getDoList(lang), isNotEmpty,
              reason: '${emergency.id} doList missing for $lang');
          expect(emergency.getDontList(lang), isNotEmpty,
              reason: '${emergency.id} dontList missing for $lang');
          expect(emergency.getKeywords(lang), isNotEmpty,
              reason: '${emergency.id} keywords missing for $lang');
          expect(emergency.steps, isNotEmpty,
              reason: '${emergency.id} has no steps');

          for (final step in emergency.steps) {
            expect(step.getTitle(lang), isNotEmpty,
                reason: '${emergency.id} step title missing for $lang');
            expect(step.getInstruction(lang), isNotEmpty,
                reason: '${emergency.id} step instruction missing for $lang');
            expect(step.getDescription(lang), isNotEmpty,
                reason: '${emergency.id} step description missing for $lang');
          }
        }
      }
    });

    test('Regional search query matching works in native scripts', () {
      expect(repository.all.where((e) => e.matchesQuery('सीपीआर', 'hi')).single.id, 'cpr');
      expect(repository.all.where((e) => e.matchesQuery('ಹಾವು', 'kn')).single.id, 'snake_bite');
      expect(repository.all.where((e) => e.matchesQuery('রক্তপাত', 'bn')).single.id, 'bleeding');
      expect(repository.all.where((e) => e.matchesQuery('కాటు', 'te')).single.id, 'snake_bite');
      expect(repository.all.where((e) => e.matchesQuery('जखम', 'mr')).single.id, 'bleeding');
      expect(repository.all.where((e) => e.matchesQuery('தீக்காயம்', 'ta')).single.id, 'burn');
      expect(repository.all.where((e) => e.matchesQuery('દાઝવું', 'gu')).single.id, 'burn');
      expect(repository.all.where((e) => e.matchesQuery('പൊള്ളൽ', 'ml')).single.id, 'burn');
      expect(repository.all.where((e) => e.matchesQuery('خون', 'ur')).single.id, 'bleeding');
    });
  });
}

