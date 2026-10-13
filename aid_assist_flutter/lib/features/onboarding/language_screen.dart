import 'package:aid_assist_flutter/app/theme/app_theme.dart';
import 'package:aid_assist_flutter/core/localization/app_strings.dart';
import 'package:aid_assist_flutter/core/settings_controller.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

class LanguageScreen extends ConsumerStatefulWidget {
  const LanguageScreen({super.key});
  @override
  ConsumerState<LanguageScreen> createState() => _LanguageScreenState();
}

class _LanguageScreenState extends ConsumerState<LanguageScreen> {
  late String selected;

  @override
  void initState() {
    super.initState();
    selected = ref.read(settingsProvider).languageCode;
  }

  @override
  Widget build(BuildContext context) {
    final strings = AppStrings.of(selected);
    final isRtl = selected == 'ur';

    final page = Scaffold(
      body: SafeArea(
        child: CustomScrollView(
          slivers: [
            SliverPadding(
              padding: const EdgeInsets.fromLTRB(16, 32, 16, 24),
              sliver: SliverList(
                delegate: SliverChildListDelegate([
                  Center(
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(24),
                      child: Image.asset('assets/logo.png', width: 104, height: 104),
                    ),
                  ),
                  const SizedBox(height: 24),
                  Text(
                    strings.chooseLanguage,
                    style: Theme.of(context).textTheme.displaySmall,
                  ),
                  const SizedBox(height: 8),
                  Text(strings.languageHelp,
                      style: Theme.of(context).textTheme.bodyLarge),
                  const SizedBox(height: 24),
                ]),
              ),
            ),
            SliverPadding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              sliver: SliverGrid(
                delegate: SliverChildBuilderDelegate(
                  (context, index) {
                    final entry = languageNames.entries.elementAt(index);
                    final active = entry.key == selected;
                    return Semantics(
                      button: true,
                      selected: active,
                      label: '${entry.value}, ${entry.key.toUpperCase()}',
                      child: InkWell(
                        onTap: () => setState(() => selected = entry.key),
                        borderRadius: BorderRadius.circular(12),
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 160),
                          padding: const EdgeInsets.all(16),
                          decoration: BoxDecoration(
                            color: active
                                ? AppColors.primaryContainer
                                : Theme.of(context).colorScheme.surfaceContainerLowest,
                            border: Border.all(
                              color: active ? AppColors.secondary : AppColors.outlineVariant,
                              width: active ? 2 : 1,
                            ),
                            borderRadius: BorderRadius.circular(12),
                            boxShadow: active
                                ? [
                                    BoxShadow(
                                      color: AppColors.primaryContainer.withOpacity(.18),
                                      blurRadius: 12,
                                      offset: const Offset(0, 4),
                                    )
                                  ]
                                : null,
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                entry.value,
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                                style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                      color: active ? Colors.white : null,
                                    ),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                entry.key.toUpperCase(),
                                style: TextStyle(
                                  color: active ? Colors.white70 : AppColors.outline,
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    );
                  },
                  childCount: languageNames.length,
                ),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  crossAxisSpacing: 12,
                  mainAxisSpacing: 12,
                  mainAxisExtent: 112,
                ),
              ),
            ),
            const SliverPadding(padding: EdgeInsets.only(bottom: 32)),
          ],
        ),
      ),
      bottomNavigationBar: SafeArea(
        minimum: const EdgeInsets.fromLTRB(16, 8, 16, 12),
        child: SizedBox(
          height: 56,
          width: double.infinity,
          child: FilledButton(
            onPressed: selected.isEmpty
                ? null
                : () async {
                    await ref.read(settingsProvider.notifier).setLanguage(selected);
                    if (context.mounted) context.go('/');
                  },
            style: FilledButton.styleFrom(
              backgroundColor: AppColors.primaryContainer,
              disabledBackgroundColor: AppColors.outlineVariant,
              shape: const StadiumBorder(),
            ),
            child: Text(strings.continueLabel),
          ),
        ),
      ),
    );

    if (isRtl) {
      return Directionality(textDirection: TextDirection.rtl, child: page);
    }
    return page;
  }
}
