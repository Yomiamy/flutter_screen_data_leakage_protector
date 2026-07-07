## 1.1.2

### Fixed

*   Show the privacy overlay on iOS when entering the App Switcher. During `willResignActive` the scene is no longer `foregroundActive`, so the key window is now resolved across all window scenes, ensuring the overlay (custom image or black fallback) is applied on resign.

## 1.1.1

### Fixed

*   Fix the overlay detection flow when switching via the Recent apps key on Android, so the security overlay is always shown instead of being toggled off.

## 1.1.0

*   Add custom privacy overlay image for screen data leakage protector.

## 1.0.0

*   Initial release.
*   Prevent sensitive data leakage when the app enters the background or App Switcher.
*   Automatically shows a black security overlay on iOS when the application resigns active.
*   Automatically shows a black security overlay on Android when the HOME or RECENT keys are pressed.
*   Support for Android and iOS.
