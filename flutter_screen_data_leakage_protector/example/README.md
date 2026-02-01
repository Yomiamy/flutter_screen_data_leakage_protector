# Leakage Protector Example

This example demonstrates how the `flutter_screen_data_leakage_protector` plugin works in a real-world scenario (a mock banking application) to protect sensitive data.

## Features Demonstrated

- **Security Shield**: The app automatically hides its content when you enter the App Switcher.
- **Mock Sensitive UI**: A dashboard showing financial data to visualize why protection is needed.
- **Platform Info**: Displays the current operating system and version.

## Verification Steps

1.  **Launch the app**: You'll see a mocked financial dashboard.
2.  **Trigger the protection**: Minimize the app or open the App Switcher. 
    - On **iOS**: The screen will turn black immediately as you swipe up.
    - On **Android**: The screen overlay appears when the Home or Recent apps button/gesture is used.
3.  **Return to app**: Tap back into the app, and the content is instantly restored.

## Implementation Details

The core protection logic is handled natively, so no additional Flutter code is strictly required for the protection to function. This example simply provides a visual environment to test the behavior.

```dart
// The plugin is initialized and works automatically
final _protectorPlugin = FlutterScreenDataLeakageProtector();
```
