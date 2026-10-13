import 'package:aid_assist_flutter/data/repositories/emergency_repository.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  const repository = EmergencyRepository();

  test('offline SOP library contains every migrated protocol', () {
    expect(
        repository.all.map((item) => item.id),
        containsAll(
            ['cpr', 'bleeding', 'burn', 'choking', 'snake_bite', 'fracture']));
  });

  test('search finds protocols by verified keywords', () {
    expect(repository.all.where((item) => item.matches('blood')).single.id,
        'bleeding');
    expect(repository.all.where((item) => item.matches('snake')).single.id,
        'snake_bite');
  });
}
