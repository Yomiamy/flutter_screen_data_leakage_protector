# Flutter Screen Data Leakage Protector

A Flutter plugin designed to prevent sensitive data leakage by obscuring the screen when the application is in the background or the App Switcher. 

This plugin provides a robust security layer by automatically covering your application's UI with a black overlay during transition states, protecting user privacy and corporate data from being visible in system-level previews.

## Features

- **Automatic Protection**: Seamlessly detects when the app is moving to the background or the app switcher.
- **Security Overlay**: Displays a solid black overlay by default or a custom image of your choice.
- **Customizable**: Add custom privacy overlay image for screen data leakage protector through simple API calls.
- **Auto-Recovery**: Automatically removes the overlay when the user returns to the app.
- **Cross-Platform**: Fully supports both Android and iOS with native implementations.
- **High Performance**: Uses native APIs for minimal overhead and immediate protection.

## Platform Support

| Android | iOS |
| :---: | :---: |
| ✅ | ✅ |

## Installation

Add this to your `pubspec.yaml`:

```yaml
dependencies:
  flutter_screen_data_leakage_protector: ^1.1.1
```

### Basic Usage
Simply including the plugin in your project activates its default protection behavior (black overlay).

```dart
import 'package:flutter_screen_data_leakage_protector/flutter_screen_data_leakage_protector.dart';
```

### Advanced Usage (Custom Overlay Image)
You can configure a custom image to be displayed instead of the default black screen.

```dart
@override
void initState() {
  super.initState();
  // Name of the image in your native assets (xcassets for iOS, drawable for Android)
  FlutterScreenDataLeakageProtector.applyDataLeakageWithConfig(
    overlayImage: 'PrivacyOverlay',
  );
}
```

### How it works

- **iOS**: Uses `UIApplication.willResignActiveNotification` and `UIApplication.didBecomeActiveNotification` to toggle a `UIView` overlay on the key window.
- **Android**: Utilizes `ActivityLifecycleCallbacks` and a `BroadcastReceiver` for `ACTION_CLOSE_SYSTEM_DIALOGS` to detect Home and Recent apps interactions, applying a black `View` overlay to the decor view.

## Verification Steps

1.  Run the application.
2.  Minimize the app or open the App Switcher (Multi-tasking view).
3.  Observe that the screen preview is obscured (blacked out).
4.  Return to the app and confirm the UI is restored immediately.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
