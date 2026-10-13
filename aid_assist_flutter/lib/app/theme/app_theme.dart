import 'package:flutter/material.dart';

abstract final class AppColors {
  static const primary = Color(0xff050066);
  static const primaryContainer = Color(0xff10069f);
  static const secondary = Color(0xff3d38e6);
  static const surface = Color(0xfffbf8ff);
  static const lowest = Color(0xffffffff);
  static const low = Color(0xfff6f2fa);
  static const outline = Color(0xff767685);
  static const outlineVariant = Color(0xffc7c5d6);
  static const error = Color(0xffba1a1a);
  static const success = Color(0xff18864b);
}

abstract final class AppSpacing {
  static const xs = 4.0;
  static const sm = 8.0;
  static const md = 16.0;
  static const lg = 24.0;
  static const xl = 32.0;
}

abstract final class AppTheme {
  static ThemeData get light => _theme(Brightness.light);
  static ThemeData get dark => _theme(Brightness.dark);
  static ThemeData _theme(Brightness brightness) {
    final scheme = ColorScheme.fromSeed(
        seedColor: AppColors.primaryContainer,
        brightness: brightness,
        primary: AppColors.primary,
        error: AppColors.error,
        surface: brightness == Brightness.light
            ? AppColors.surface
            : const Color(0xff15131c));
    final text = ThemeData(brightness: brightness).textTheme;
    return ThemeData(
      useMaterial3: true,
      colorScheme: scheme,
      scaffoldBackgroundColor: scheme.surface,
      textTheme: text.copyWith(
        displaySmall: text.displaySmall?.copyWith(
            fontFamily: 'serif',
            fontSize: 32,
            height: 38 / 32,
            fontWeight: FontWeight.w700),
        headlineSmall: text.headlineSmall?.copyWith(
            fontFamily: 'serif',
            fontSize: 24,
            height: 30 / 24,
            fontWeight: FontWeight.w600),
        titleLarge: text.titleLarge?.copyWith(
            fontFamily: 'serif',
            fontSize: 22,
            height: 28 / 22,
            fontWeight: FontWeight.w600),
        titleMedium: text.titleMedium?.copyWith(
            fontSize: 18, height: 24 / 18, fontWeight: FontWeight.w600),
        bodyLarge: text.bodyLarge?.copyWith(fontSize: 16, height: 24 / 16),
        bodyMedium: text.bodyMedium?.copyWith(fontSize: 14, height: 20 / 14),
        labelLarge: text.labelLarge?.copyWith(
            fontSize: 15, height: 20 / 15, fontWeight: FontWeight.w600),
      ),
      cardTheme: CardTheme(
          color: brightness == Brightness.light
              ? AppColors.lowest
              : const Color(0xff211f29),
          elevation: 0,
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
              side: BorderSide(color: scheme.outlineVariant))),
      inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: brightness == Brightness.light
              ? AppColors.lowest
              : const Color(0xff211f29),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(color: scheme.outlineVariant))),
    );
  }
}
