import 'package:aid_assist_flutter/data/models/emergency.dart';
import 'package:flutter/services.dart';
import 'dart:math' as math;

class HospitalRepository {
  HospitalRepository({MethodChannel? channel})
      : _channel = channel ?? const MethodChannel('aid_assist/device');

  final MethodChannel _channel;

  Future<bool> isConfigured() async {
    try {
      return await _channel.invokeMethod<bool>('hasGoogleMapsApiKey') ?? false;
    } on PlatformException {
      return false;
    }
  }

  Future<List<Hospital>> nearby({required double latitude, required double longitude}) async {
    try {
      final values = await _channel.invokeMethod<List<dynamic>>(
        'searchNearbyHospitals',
        {'latitude': latitude, 'longitude': longitude},
      );
      final hospitals = (values ?? const <dynamic>[]).whereType<Map>().map((item) {
        return Hospital(
          name: item['name'] as String? ?? 'Hospital',
          address: item['address'] as String? ?? '',
          phone: item['phone'] as String? ?? '',
          latitude: (item['latitude'] as num).toDouble(),
          longitude: (item['longitude'] as num).toDouble(),
          mapsUri: item['mapsUri'] as String?,
          rating: (item['rating'] as num?)?.toDouble(),
          open: item['open'] as bool?,
        );
      }).toList();
      hospitals.sort((first, second) {
        final firstDistance = _distance(latitude, longitude, first);
        final secondDistance = _distance(latitude, longitude, second);
        return firstDistance.compareTo(secondDistance);
      });
      return hospitals;
    } on PlatformException catch (error) {
      throw HospitalSearchException(error.message ?? 'Unable to load nearby hospitals right now.');
    } on FormatException {
      return const [];
    }
  }

  double _distance(double latitude, double longitude, Hospital hospital) {
    const earthRadiusKm = 6371.0;
    final lat1 = latitude * math.pi / 180;
    final lat2 = hospital.latitude * math.pi / 180;
    final deltaLat = (hospital.latitude - latitude) * math.pi / 180;
    final deltaLng = (hospital.longitude - longitude) * math.pi / 180;
    final a = math.sin(deltaLat / 2) * math.sin(deltaLat / 2) +
        math.cos(lat1) *
            math.cos(lat2) *
            math.sin(deltaLng / 2) *
            math.sin(deltaLng / 2);
    return earthRadiusKm * 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a));
  }
}

class HospitalSearchException implements Exception {
  const HospitalSearchException(this.message);
  final String message;
}