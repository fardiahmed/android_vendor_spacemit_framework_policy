# SpacemiT Policy SDK

The API for the SpacemiT device‑policy service: the `ISpacemitPolicyManager`
AIDL contract plus a small `SpacemitPolicyManager` client that binds to the
service. Pure public‑API Java (no `@hide`), so the same sources build in Soong
**and** as a standalone AAR for JitPack.

## What it's for

An EMM/DPC (acting as device owner) manages **standard** device controls
(camera, mic, Bluetooth, NFC, USB) directly through `DevicePolicyManager` user
restrictions. For **SpacemiT/board‑specific** controls there is no standard API,
so those go through this SDK → the policy service → the device. The companion
OEMConfig app (`vendor/spacemit/apps/SpacemitOemConfig`) is the EMM‑facing front
end; this SDK is the programmatic path any signed/privileged app can use.

## API

```java
SpacemitPolicyManager pm = SpacemitPolicyManager.create(context);
pm.bind(new SpacemitPolicyManager.ConnectionListener() {
    @Override public void onConnected() {
        pm.setVendorProperty(PolicyKeys.KIOSK_MODE, "1");
        pm.setDeviceSetting(SpacemitPolicy.NS_GLOBAL, PolicyKeys.ADB_ENABLED, "0");
    }
    @Override public void onDisconnected() { }
});
```

Callers need `com.spacemit.policy.permission.MANAGE_DEVICE_POLICY`
(`signature|privileged`). `getAppliedPolicies()` returns the current applied set
for compliance reporting. Bump `SpacemitPolicy.API_VERSION` when the AIDL changes.

## Use it in the platform build

The two SpacemiT apps already `static_libs: ["spacemit-policy-sdk"]`. Any other
in‑tree app does the same.

## Use it from an external app (GitHub / JitPack)

Publish by tagging a release of this repo on GitHub; JitPack builds the AAR.

```gradle
// settings.gradle
dependencyResolutionManagement {
    repositories { google(); mavenCentral(); maven { url 'https://jitpack.io' } }
}
// app/build.gradle
implementation 'com.github.fardiahmed:android_vendor_spacemit_framework_policy:1.0.0'
```

One‑time, before the first tag, generate the Gradle wrapper so JitPack uses a
known Gradle version: `gradle wrapper --gradle-version 8.9`. The Gradle files
(`build.gradle`, `settings.gradle`, `jitpack.yml`) are already here.

Note: the AAR lets an app *compile* against the API; at runtime the call still
needs the policy service present and the `MANAGE_DEVICE_POLICY` permission.

## Hardening (later)

Promote this to `java_sdk_library` to freeze a tracked API (`api/*.txt`) and
generate stubs + Javadoc.
