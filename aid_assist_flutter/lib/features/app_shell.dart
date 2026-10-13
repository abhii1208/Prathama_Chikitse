import 'package:aid_assist_flutter/app/theme/app_theme.dart';
import 'package:aid_assist_flutter/core/localization/app_strings.dart';
import 'package:aid_assist_flutter/core/services/device_services.dart';
import 'package:aid_assist_flutter/core/settings_controller.dart';
import 'package:aid_assist_flutter/data/models/emergency.dart';
import 'package:aid_assist_flutter/data/repositories/emergency_repository.dart';
import 'package:aid_assist_flutter/data/repositories/hospital_repository.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class AppShell extends ConsumerStatefulWidget {
  const AppShell({super.key});
  @override
  ConsumerState<AppShell> createState() => _AppShellState();
}

class _AppShellState extends ConsumerState<AppShell> {
  int index = 0;
  final device = DeviceService();

  @override
  Widget build(BuildContext context) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final isRtl = langCode == 'ur';

    final pages = [
      HomePage(device: device),
      HospitalPage(device: device),
      const KitPage(),
      ProfilePage(device: device)
    ];

    final bodyContent = Scaffold(
      body: IndexedStack(index: index, children: pages),
      bottomNavigationBar: NavigationBar(
        selectedIndex: index,
        onDestinationSelected: (value) => setState(() => index = value),
        destinations: [
          NavigationDestination(
              icon: const Icon(Icons.home_outlined), label: strings.home),
          NavigationDestination(
              icon: const Icon(Icons.local_hospital_outlined),
              label: strings.hospitals),
          NavigationDestination(
              icon: const Icon(Icons.medical_services_outlined), label: strings.kit),
          NavigationDestination(
              icon: const Icon(Icons.person_outline), label: strings.profile)
        ],
      ),
    );

    if (isRtl) {
      return Directionality(
        textDirection: TextDirection.rtl,
        child: bodyContent,
      );
    }
    return bodyContent;
  }
}

class BrandBar extends ConsumerWidget {
  const BrandBar({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Container(
      height: 58,
      padding: const EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
          border: Border(
              bottom: BorderSide(
                  color: Theme.of(context).colorScheme.outlineVariant))),
      child: Row(children: [
        Image.asset('assets/logo.png', width: 34, height: 34),
        const SizedBox(width: 8),
        Text('AID-ASSIST',
            style: Theme.of(context)
                .textTheme
                .headlineSmall
                ?.copyWith(color: AppColors.primary)),
        const Spacer(),
        PopupMenuButton<String>(
            icon: const Icon(Icons.translate),
            tooltip: AppStrings.of(ref.watch(settingsProvider).languageCode).chooseLanguage,
            onSelected: (code) =>
                ref.read(settingsProvider.notifier).setLanguage(code),
            itemBuilder: (_) => languageNames.entries
                .map((entry) =>
                    PopupMenuItem(value: entry.key, child: Text(entry.value)))
                .toList())
      ]),
    );
  }
}

class HomePage extends ConsumerStatefulWidget {
  const HomePage({super.key, required this.device});
  final DeviceService device;
  @override
  ConsumerState<HomePage> createState() => _HomePageState();
}

class _HomePageState extends ConsumerState<HomePage> {
  final repo = const EmergencyRepository();
  String query = '';
  String categoryFilter = 'ALL';
  bool whistle = false;
  bool torch = false;

  @override
  Widget build(BuildContext context) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);

    final rawCategories = repo.all.map((item) => item.getCategory(langCode)).toSet().toList();
    final categories = ['ALL', ...rawCategories];

    final guides = repo.all.where((item) =>
        item.matchesQuery(query, langCode) &&
        (categoryFilter == 'ALL' || item.getCategory(langCode) == categoryFilter));

    return SafeArea(
      child: Column(children: [
        const BrandBar(),
        Expanded(
          child: ListView(padding: const EdgeInsets.all(16), children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                  color: AppColors.primaryContainer,
                  borderRadius: BorderRadius.circular(16)),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(children: [
                    Text(strings.offline,
                        style: TextStyle(
                            color: Colors.white.withOpacity(.8),
                            fontWeight: FontWeight.bold,
                            fontSize: 11)),
                    const Spacer(),
                    Text(strings.clinicalTrauma,
                        style: TextStyle(
                            color: Colors.white.withOpacity(.72),
                            fontWeight: FontWeight.bold,
                            fontSize: 11))
                  ]),
                  Text('AID-ASSIST',
                      style: Theme.of(context)
                          .textTheme
                          .displaySmall
                          ?.copyWith(color: Colors.white)),
                  Text(strings.tagline,
                      style: const TextStyle(color: Colors.white)),
                  const SizedBox(height: 14),
                  TextField(
                    onChanged: (value) => setState(() => query = value),
                    decoration: InputDecoration(
                        prefixIcon: const Icon(Icons.search),
                        hintText: strings.search),
                  )
                ],
              ),
            ),
            const SizedBox(height: 14),
            SizedBox(
              width: double.infinity,
              height: 56,
              child: FilledButton.icon(
                onPressed: () => widget.device.dial('112'),
                icon: const Icon(Icons.call),
                label: Text(strings.call112,
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                style: FilledButton.styleFrom(
                    backgroundColor: AppColors.error,
                    shape: const StadiumBorder()),
              ),
            ),
            const SizedBox(height: 18),
            Text(strings.rapid,
                style: Theme.of(context)
                    .textTheme
                    .titleMedium
                    ?.copyWith(color: AppColors.primary)),
            const SizedBox(height: 8),
            Row(children: [
              Expanded(
                child: _Quick(
                  icon: Icons.volume_up,
                  label: whistle ? strings.stopWhistle : strings.whistle,
                  onTap: () async {
                    final ok = await widget.device.setWhistle(!whistle);
                    if (mounted && ok) setState(() => whistle = !whistle);
                  },
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _Quick(
                  icon: Icons.flashlight_on,
                  label: torch ? strings.flashOn : strings.flashlight,
                  onTap: () async {
                    final ok = await widget.device.setTorch(!torch);
                    if (mounted && ok) setState(() => torch = !torch);
                  },
                ),
              ),
            ]),
            const SizedBox(height: 20),
            Text(strings.protocols,
                style: Theme.of(context)
                    .textTheme
                    .titleMedium
                    ?.copyWith(color: AppColors.primary)),
            const SizedBox(height: 8),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: categories
                    .map((item) => Padding(
                          padding: const EdgeInsets.only(right: 8),
                          child: ChoiceChip(
                            label: Text(item == 'ALL' ? strings.all : item),
                            selected: categoryFilter == item,
                            onSelected: (_) => setState(() => categoryFilter = item),
                          ),
                        ))
                    .toList(),
              ),
            ),
            const SizedBox(height: 8),
            ...guides.map((guide) => EmergencyCard(guide: guide, device: widget.device)),
          ]),
        )
      ]),
    );
  }
}

class _Quick extends StatelessWidget {
  const _Quick({required this.icon, required this.label, this.onTap});
  final IconData icon;
  final String label;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) => InkWell(
        onTap: onTap,
        child: Ink(
          height: 76,
          decoration: BoxDecoration(
              color: AppColors.low,
              border: Border.all(color: AppColors.outlineVariant),
              borderRadius: BorderRadius.circular(10)),
          child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
            Icon(icon, color: AppColors.primary),
            Text(label,
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.labelSmall)
          ]),
        ),
      );
}

class EmergencyCard extends ConsumerWidget {
  const EmergencyCard({super.key, required this.guide, required this.device});
  final Emergency guide;
  final DeviceService device;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final isCritical = guide.severity == EmergencySeverity.critical;

    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Card(
        child: ListTile(
          onTap: () => Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => SopPage(guide: guide, device: device),
            ),
          ),
          leading: Icon(guide.icon, color: AppColors.primary, size: 32),
          title: Text(guide.getTitle(langCode),
              style: Theme.of(context).textTheme.titleMedium),
          subtitle: Text(guide.getSummary(langCode)),
          trailing: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(isCritical ? strings.critical : strings.urgent,
                  style: TextStyle(
                      color: isCritical
                          ? AppColors.error
                          : const Color(0xffb54708),
                      fontSize: 10,
                      fontWeight: FontWeight.bold)),
              const Icon(Icons.arrow_forward, color: AppColors.secondary)
            ],
          ),
        ),
      ),
    );
  }
}

class SopPage extends ConsumerStatefulWidget {
  const SopPage({super.key, required this.guide, required this.device});
  final Emergency guide;
  final DeviceService device;
  @override
  ConsumerState<SopPage> createState() => _SopPageState();
}

class _SopPageState extends ConsumerState<SopPage> {
  int step = 0;

  @override
  Widget build(BuildContext context) {
    final guide = widget.guide;
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final isRtl = langCode == 'ur';

    final currentStep = guide.steps[step];

    final pageContent = Scaffold(
      body: SafeArea(
        child: Column(children: [
          Container(
            color: AppColors.primary,
            padding: const EdgeInsets.all(12),
            child: Row(children: [
              IconButton(
                onPressed: () => Navigator.pop(context),
                tooltip: strings.back,
                icon: const Icon(Icons.arrow_back, color: Colors.white),
              ),
              Expanded(
                child: Column(children: [
                  Text(strings.offlineProtocol,
                      style: const TextStyle(color: Color(0xffc0c1ff), fontSize: 11)),
                  Text(guide.getTitle(langCode).toUpperCase(),
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                          color: Colors.white,
                          fontSize: 18,
                          fontWeight: FontWeight.bold))
                ]),
              ),
              IconButton(
                onPressed: () => widget.device.speak(
                  '${currentStep.getInstruction(langCode)}. ${currentStep.getDescription(langCode)}',
                  langCode,
                ),
                tooltip: strings.readAloud,
                icon: const Icon(Icons.volume_up, color: Colors.white),
              )
            ]),
          ),
          if (guide.severity == EmergencySeverity.critical)
            Container(
              color: const Color(0xffffdad6),
              padding: const EdgeInsets.all(10),
              child: Row(children: [
                const Icon(Icons.circle, size: 12, color: AppColors.error),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    '${strings.criticalTimeSensitive}\n${guide.getImmediateAction(langCode)}',
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                FilledButton(
                  onPressed: () => widget.device.dial('112'),
                  style: FilledButton.styleFrom(backgroundColor: AppColors.error),
                  child: const Text('112'),
                )
              ]),
            ),
          Expanded(
            child: ListView(padding: const EdgeInsets.all(16), children: [
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '${strings.step} ${step + 1} ${strings.ofWord} ${guide.steps.length}: ${currentStep.getTitle(langCode).toUpperCase()}',
                        style: Theme.of(context)
                            .textTheme
                            .labelLarge
                            ?.copyWith(color: AppColors.primary),
                      ),
                      const SizedBox(height: 8),
                      LinearProgressIndicator(
                        value: (step + 1) / guide.steps.length,
                        color: AppColors.primaryContainer,
                      )
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 12),
              _Visual(cpr: currentStep.illustration == 'cpr'),
              const SizedBox(height: 12),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(currentStep.getInstruction(langCode).toUpperCase(),
                          style: Theme.of(context)
                              .textTheme
                              .titleLarge
                              ?.copyWith(color: AppColors.primary)),
                      const SizedBox(height: 10),
                      Text(currentStep.getDescription(langCode),
                          style: Theme.of(context).textTheme.bodyLarge)
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 10),
              _Safety(
                title: strings.doLabel,
                items: guide.getDoList(langCode),
                color: AppColors.success,
              ),
              const SizedBox(height: 10),
              _Safety(
                title: strings.dont,
                items: guide.getDontList(langCode),
                color: AppColors.error,
              ),
              const SizedBox(height: 12),
              Row(children: [
                Expanded(
                  child: OutlinedButton(
                    onPressed: step == 0 ? null : () => setState(() => step--),
                    child: Text(strings.previous),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: FilledButton(
                    onPressed: () => setState(() =>
                        step = step == guide.steps.length - 1 ? 0 : step + 1),
                    style: FilledButton.styleFrom(
                        backgroundColor: AppColors.primaryContainer),
                    child: Text(step == guide.steps.length - 1
                        ? strings.restart
                        : strings.next),
                  ),
                )
              ])
            ]),
          ),
          Padding(
            padding: const EdgeInsets.all(12),
            child: SizedBox(
              width: double.infinity,
              height: 56,
              child: FilledButton.icon(
                onPressed: () => widget.device.dial('112'),
                icon: const Icon(Icons.call),
                label: Text(strings.call112Now,
                    style:
                        const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                style: FilledButton.styleFrom(
                    backgroundColor: AppColors.error,
                    shape: const StadiumBorder()),
              ),
            ),
          )
        ]),
      ),
    );

    if (isRtl) {
      return Directionality(textDirection: TextDirection.rtl, child: pageContent);
    }
    return pageContent;
  }
}

class _Visual extends StatelessWidget {
  const _Visual({required this.cpr});
  final bool cpr;

  @override
  Widget build(BuildContext context) => Container(
        height: 180,
        decoration: BoxDecoration(
            color: const Color(0xfff0efff),
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: AppColors.outlineVariant)),
        child: Center(
          child: Icon(
            cpr ? Icons.accessibility_new : Icons.health_and_safety_outlined,
            size: 88,
            color: AppColors.primaryContainer,
          ),
        ),
      );
}

class _Safety extends StatelessWidget {
  const _Safety(
      {required this.title, required this.items, required this.color});
  final String title;
  final List<String> items;
  final Color color;

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
            color: color.withOpacity(.06),
            border: Border(
                left: BorderSide(color: color, width: 4),
                top: BorderSide(color: color),
                right: BorderSide(color: color),
                bottom: BorderSide(color: color)),
            borderRadius: BorderRadius.circular(12)),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Text(title,
              style: TextStyle(color: color, fontWeight: FontWeight.bold)),
          const SizedBox(height: 5),
          ...items.map((item) => Text('• $item'))
        ]),
      );
}

class HospitalPage extends ConsumerStatefulWidget {
  const HospitalPage({super.key, required this.device});
  final DeviceService device;
  @override
  ConsumerState<HospitalPage> createState() => _HospitalPageState();
}

class _HospitalPageState extends ConsumerState<HospitalPage> {
  Position? position;
  final hospitalRepository = HospitalRepository();
  List<Hospital> hospitals = [];
  bool loading = false;
  bool configured = false;
  String? selectedHospital;
  String? customMessage;

  Future<void> locate(AppStrings strings) async {
    setState(() => loading = true);
    try {
      configured = await hospitalRepository.isConfigured();
      var permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
      }
      if (permission == LocationPermission.denied ||
          permission == LocationPermission.deniedForever) {
        setState(() {
          loading = false;
          customMessage = strings.locationDeniedMsg;
        });
        return;
      }
      if (!await Geolocator.isLocationServiceEnabled()) {
        setState(() {
          loading = false;
          customMessage = strings.gpsDisabledMsg;
        });
        return;
      }
      final next = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
        timeLimit: const Duration(seconds: 15),
      );
      setState(() {
        position = next;
        customMessage = strings.gpsAcquiredMsg;
      });
      if (configured) {
        final results = await hospitalRepository.nearby(
          latitude: next.latitude,
          longitude: next.longitude,
        );
        if (!mounted) return;
        setState(() {
          hospitals = results;
          loading = false;
          customMessage = results.isEmpty
              ? strings.noHospitalsMsg
              : strings.liveHospitalsSortedMsg;
        });
      } else {
        setState(() => loading = false);
      }
    } catch (_) {
      if (mounted) {
        setState(() {
          loading = false;
          customMessage = strings.offlineHospitalFallbackMsg;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final displayMessage = customMessage ?? strings.locationInitMsg;

    return SafeArea(
      child: Column(children: [
        const BrandBar(),
        Expanded(
          child: ListView(padding: const EdgeInsets.all(16), children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                  color: AppColors.primaryContainer,
                  borderRadius: BorderRadius.circular(16)),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(strings.triageLevel,
                      style: const TextStyle(
                          color: Color(0xffc0c1ff),
                          fontWeight: FontWeight.bold,
                          fontSize: 11)),
                  Text(strings.nearbyHospitals,
                      style: Theme.of(context)
                          .textTheme
                          .headlineSmall
                          ?.copyWith(color: Colors.white)),
                  Text(strings.gpsSub,
                      style: const TextStyle(color: Colors.white70))
                ],
              ),
            ),
            const SizedBox(height: 12),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(14),
                child: Column(children: [
                  Text(displayMessage),
                  const SizedBox(height: 8),
                  OutlinedButton.icon(
                      onPressed: () => locate(strings),
                      icon: const Icon(Icons.my_location),
                      label: Text(strings.useLocation))
                ]),
              ),
            ),
            const SizedBox(height: 12),
            if (position != null)
              SizedBox(
                height: 220,
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(16),
                  child: GoogleMap(
                    initialCameraPosition: CameraPosition(
                        target: LatLng(position!.latitude, position!.longitude),
                        zoom: 12),
                    myLocationEnabled: true,
                    myLocationButtonEnabled: true,
                    markers: {
                      Marker(
                          markerId: const MarkerId('current-location'),
                          position: LatLng(position!.latitude, position!.longitude),
                          icon: BitmapDescriptor.defaultMarkerWithHue(
                              BitmapDescriptor.hueViolet)),
                      ...hospitals.map((hospital) => Marker(
                          markerId: MarkerId(hospital.name),
                          position: LatLng(hospital.latitude, hospital.longitude),
                          icon: BitmapDescriptor.defaultMarkerWithHue(
                              BitmapDescriptor.hueRed),
                          onTap: () => setState(
                              () => selectedHospital = hospital.name),
                          infoWindow: InfoWindow(title: hospital.name)))
                    },
                  ),
                ),
              ),
            if (loading)
              const Padding(
                  padding: EdgeInsets.symmetric(vertical: 24),
                  child: Center(child: CircularProgressIndicator())),
            ...hospitals.map((hospital) {
              final distance = position == null
                  ? null
                  : Geolocator.distanceBetween(
                          position!.latitude,
                          position!.longitude,
                          hospital.latitude,
                          hospital.longitude) /
                      1000;
              return Card(
                color: selectedHospital == hospital.name
                    ? AppColors.primaryContainer.withOpacity(.1)
                    : null,
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(children: [
                        const Icon(Icons.local_hospital,
                            color: AppColors.error, size: 34),
                        const SizedBox(width: 10),
                        Expanded(
                            child: Text(hospital.name,
                                style:
                                    Theme.of(context).textTheme.titleLarge)),
                        if (distance != null)
                          Chip(
                              label:
                                  Text('${distance.toStringAsFixed(1)} km'))
                      ]),
                      const SizedBox(height: 8),
                      Text(hospital.address),
                      if (hospital.rating != null || hospital.open != null)
                        Padding(
                          padding: const EdgeInsets.only(top: 8),
                          child: Row(children: [
                            if (hospital.rating != null)
                              Text('${strings.rating} ${hospital.rating!.toStringAsFixed(1)}'),
                            if (hospital.rating != null && hospital.open != null)
                              const SizedBox(width: 12),
                            if (hospital.open != null)
                              Text(
                                hospital.open! ? strings.openNow : strings.closed,
                                style: TextStyle(
                                    color: hospital.open!
                                        ? AppColors.success
                                        : AppColors.error,
                                    fontWeight: FontWeight.w600),
                              )
                          ]),
                        ),
                      const SizedBox(height: 12),
                      Row(children: [
                        Expanded(
                          child: FilledButton.icon(
                            onPressed: hospital.phone.isEmpty
                                ? null
                                : () => widget.device.dial(hospital.phone),
                            icon: const Icon(Icons.call),
                            label: Text(strings.call),
                            style: FilledButton.styleFrom(
                                backgroundColor: AppColors.primary),
                          ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: OutlinedButton.icon(
                            onPressed: () => widget.device.directions(
                                hospital.latitude, hospital.longitude),
                            icon: const Icon(Icons.directions),
                            label: Text(strings.directions),
                          ),
                        )
                      ])
                    ],
                  ),
                ),
              );
            })
          ]),
        )
      ]),
    );
  }
}

class KitPage extends ConsumerWidget {
  const KitPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final state = ref.watch(settingsProvider);

    final items = [
      'Sterile gauze pads',
      'Adhesive bandages',
      'Antiseptic wipes',
      'Medical tape',
      'Disposable gloves',
      'Scissors'
    ];

    return SafeArea(
      child: Column(children: [
        const BrandBar(),
        Expanded(
          child: ListView(padding: const EdgeInsets.all(16), children: [
            Text(strings.kitReadiness,
                style: Theme.of(context).textTheme.headlineSmall),
            const SizedBox(height: 12),
            LinearProgressIndicator(
                value: state.kitItems.length / items.length,
                color: AppColors.primaryContainer),
            const SizedBox(height: 12),
            ...items.map((item) => Card(
                  child: CheckboxListTile(
                    value: state.kitItems.contains(item),
                    onChanged: (_) =>
                        ref.read(settingsProvider.notifier).toggleKit(item),
                    title: Text(item),
                    controlAffinity: ListTileControlAffinity.leading,
                  ),
                ))
          ]),
        )
      ]),
    );
  }
}

class ProfilePage extends ConsumerStatefulWidget {
  const ProfilePage({super.key, required this.device});
  final DeviceService device;

  @override
  ConsumerState<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends ConsumerState<ProfilePage> {
  late final TextEditingController name;
  late final TextEditingController blood;
  final contactName = TextEditingController();
  final contactPhone = TextEditingController();

  @override
  void initState() {
    super.initState();
    final state = ref.read(settingsProvider);
    name = TextEditingController(text: state.name);
    blood = TextEditingController(text: state.bloodGroup);
  }

  @override
  void dispose() {
    name.dispose();
    blood.dispose();
    contactName.dispose();
    contactPhone.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final langCode = ref.watch(settingsProvider).languageCode;
    final strings = AppStrings.of(langCode);
    final state = ref.watch(settingsProvider);

    return SafeArea(
      child: Column(children: [
        const BrandBar(),
        Expanded(
          child: ListView(padding: const EdgeInsets.all(16), children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                  color: AppColors.primaryContainer,
                  borderRadius: BorderRadius.circular(16)),
              child: Row(children: [
                Image.asset('assets/logo.png', width: 54, height: 54),
                const SizedBox(width: 12),
                Text(strings.emergencyProfile,
                    style: Theme.of(context)
                        .textTheme
                        .headlineSmall
                        ?.copyWith(color: Colors.white))
              ]),
            ),
            const SizedBox(height: 16),
            TextField(
                controller: name,
                decoration: InputDecoration(labelText: strings.name)),
            const SizedBox(height: 8),
            TextField(
                controller: blood,
                decoration: InputDecoration(labelText: strings.bloodGroup)),
            const SizedBox(height: 12),
            FilledButton(
                onPressed: () => ref
                    .read(settingsProvider.notifier)
                    .saveProfile(name.text, blood.text),
                child: Text(strings.save)),
            const SizedBox(height: 8),
            SwitchListTile(
                value: state.darkMode,
                onChanged: (value) =>
                    ref.read(settingsProvider.notifier).setDarkMode(value),
                title: Text(strings.darkMode)),
            const SizedBox(height: 8),
            TextField(
                controller: contactName,
                decoration: InputDecoration(labelText: strings.contactName)),
            const SizedBox(height: 8),
            TextField(
                controller: contactPhone,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(labelText: strings.phoneNumber)),
            const SizedBox(height: 12),
            OutlinedButton(
                onPressed: () async {
                  await ref
                      .read(settingsProvider.notifier)
                      .addContact(contactName.text, contactPhone.text);
                  contactName.clear();
                  contactPhone.clear();
                },
                child: Text(strings.addContact)),
            const SizedBox(height: 12),
            ...state.contacts.map((contact) {
              final data = contact.split('|');
              return Card(
                child: ListTile(
                  title: Text(data.first),
                  subtitle: Text(data.length > 1 ? data[1] : ''),
                  trailing: Wrap(children: [
                    IconButton(
                        onPressed: () =>
                            widget.device.dial(data.length > 1 ? data[1] : ''),
                        icon: const Icon(Icons.call)),
                    IconButton(
                        onPressed: () => ref
                            .read(settingsProvider.notifier)
                            .removeContact(contact),
                        icon: const Icon(Icons.delete_outline)),
                  ]),
                ),
              );
            })
          ]),
        )
      ]),
    );
  }
}
