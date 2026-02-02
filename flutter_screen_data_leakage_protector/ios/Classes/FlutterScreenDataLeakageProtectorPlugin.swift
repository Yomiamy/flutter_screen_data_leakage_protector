import Flutter
import UIKit

public class FlutterScreenDataLeakageProtectorPlugin: NSObject, FlutterPlugin {
    private static let securityOverlayTag = 999_999
    private var overlayImageName: String?

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_screen_data_leakage_protector", binaryMessenger: registrar.messenger())
        let instance = FlutterScreenDataLeakageProtectorPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)

        // Register for lifecycle notifications to protect screen data when app is inactive
        NotificationCenter.default.addObserver(
            instance,
            selector: #selector(instance.applicationWillResignActive),
            name: UIApplication.willResignActiveNotification,
            object: nil
        )
        NotificationCenter.default.addObserver(
            instance,
            selector: #selector(instance.applicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: nil
        )
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if call.method == "applyDataLeakageWithConfig" {
            if let args = call.arguments as? [String: Any] {
                self.overlayImageName = args["overlayImage"] as? String
            }
            result(nil)
        } else {
            result(FlutterMethodNotImplemented)
        }
    }

    @objc private func applicationWillResignActive() {
        showSecurityOverlay()
    }

    @objc private func applicationDidBecomeActive() {
        hideSecurityOverlay()
    }

    private func showSecurityOverlay() {
        guard let window = getActiveWindow() else { return }
        let tag = Self.securityOverlayTag

        if let overlay = window.viewWithTag(tag) {
            overlay.isHidden = false
            window.bringSubviewToFront(overlay)
        } else {
            let overlay: UIView
            if let imageName = overlayImageName, let image = UIImage(named: imageName) {
                let imageView = UIImageView(frame: window.bounds)
                imageView.image = image
                imageView.contentMode = .scaleAspectFill
                overlay = imageView
            } else {
                overlay = UIView(frame: window.bounds)
                overlay.backgroundColor = .black
            }
            overlay.tag = tag
            overlay.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            window.addSubview(overlay)
            overlay.isHidden = false
        }
    }

    private func hideSecurityOverlay() {
        getActiveWindow()?.viewWithTag(Self.securityOverlayTag)?.isHidden = true
    }

    private func getActiveWindow() -> UIWindow? {
        if #available(iOS 13.0, *) {
            return UIApplication.shared.connectedScenes
                .filter { $0.activationState == .foregroundActive }
                .first(where: { $0 is UIWindowScene })
                .flatMap { $0 as? UIWindowScene }?.windows
                .first(where: { $0.isKeyWindow })
                ?? UIApplication.shared.delegate?.window ?? nil
        } else {
            return UIApplication.shared.keyWindow ?? UIApplication.shared.delegate?.window ?? nil
        }
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
