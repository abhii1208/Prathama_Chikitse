import 'package:flutter/services.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:url_launcher/url_launcher.dart';

class DeviceService {
  static const _channel = MethodChannel('aid_assist/device');
  final FlutterTts _tts = FlutterTts();

  static const _ttsLocales = <String, String>{
    'en': 'en-IN',
    'hi': 'hi-IN',
    'kn': 'kn-IN',
    'bn': 'bn-IN',
    'te': 'te-IN',
    'mr': 'mr-IN',
    'ta': 'ta-IN',
    'gu': 'gu-IN',
    'ml': 'ml-IN',
    'ur': 'ur-PK',
  };

  Future<bool> setTorch(bool enabled) async {
    try {
      return await _channel
              .invokeMethod<bool>('setTorch', {'enabled': enabled}) ??
          false;
    } on PlatformException {
      return false;
    }
  }

  Future<bool> setWhistle(bool enabled) async {
    try {
      return await _channel
              .invokeMethod<bool>('setWhistle', {'enabled': enabled}) ??
          false;
    } on PlatformException {
      return false;
    }
  }

  Future<void> speak(String text, String languageCode) async {
    try {
      final ttsLocale = _ttsLocales[languageCode] ?? 'en-IN';
      await _tts.setLanguage(ttsLocale);
      await _tts.speak(text);
    } catch (_) {}
  }

  Future<void> dial(String number) async {
    final uri = Uri(scheme: 'tel', path: number);
    if (await canLaunchUrl(uri)) await launchUrl(uri);
  }

  Future<void> directions(double lat, double lng) async {
    final uri = Uri.parse('google.navigation:q=$lat,$lng');
    if (await canLaunchUrl(uri)) await launchUrl(uri);
  }
}
