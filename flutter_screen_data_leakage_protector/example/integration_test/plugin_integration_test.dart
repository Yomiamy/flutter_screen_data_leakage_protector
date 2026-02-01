import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:flutter_screen_data_leakage_protector/flutter_screen_data_leakage_protector.dart';

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('plugin instance creation test', (WidgetTester tester) async {
    final FlutterScreenDataLeakageProtector plugin =
        FlutterScreenDataLeakageProtector();
    expect(plugin, isNotNull);
  });
}
