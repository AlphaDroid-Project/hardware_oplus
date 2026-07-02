package com.oplus.content;

/**
 * Minimal port surface for the OPlus framework interface.
 *
 * Only the nested {@link FeatureID} enum is required by the port: OplusAppPlatform's AppFeature
 * action observer (com.oplus.customize.appfeature.n) declares
 *   onFeaturesActionUpdate(String, String, IOplusFeatureConfigManager$FeatureID)
 * and calls Enum.ordinal() on the FeatureID argument. For that class to verify and load inside
 * system_server (the provider runs in process="system"), this type must resolve and be an enum.
 *
 * The concrete enum constants are irrelevant on this port: registerFeatureActionObserver() is a
 * no-op, so onFeaturesActionUpdate is never actually invoked. We keep the real first constant so
 * ordinal() == 0 stays meaningful if anything ever does fire.
 */
public interface IOplusFeatureConfigManager {
    enum FeatureID {
        DEFAULT
    }
}
