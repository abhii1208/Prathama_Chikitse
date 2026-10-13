import 'package:aid_assist_flutter/app/theme/app_theme.dart';
import 'package:aid_assist_flutter/core/settings_controller.dart';
import 'package:aid_assist_flutter/features/app_shell.dart';
import 'package:aid_assist_flutter/features/onboarding/language_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

class AidAssistApp extends ConsumerWidget {
  const AidAssistApp({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final settings = ref.watch(settingsProvider);
    final router = GoRouter(
      initialLocation: settings.onboarded ? '/' : '/language',
      routes: [
        GoRoute(path: '/', builder: (_, __) => const AppShell()),
        GoRoute(path: '/language', builder: (_, __) => const LanguageScreen()),
      ],
    );
    return MaterialApp.router(
      title: 'AID-ASSIST',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light,
      darkTheme: AppTheme.dark,
      themeMode: settings.darkMode ? ThemeMode.dark : ThemeMode.light,
      locale: Locale(settings.languageCode),
      supportedLocales: supportedLocales,
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate
      ],
      routerConfig: router,
    );
  }
}
