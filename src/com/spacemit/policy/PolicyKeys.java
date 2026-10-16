/*
 * Copyright (C) 2026 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */
package com.spacemit.policy;

/**
 * Well-known policy keys for the vertical slice. Standard device toggles
 * (camera, mic, Bluetooth, NFC, USB) are intentionally NOT here: those are set
 * through DevicePolicyManager user restrictions by the device owner directly.
 * Only SpacemiT/board-specific policies live here.
 */
public final class PolicyKeys {

    private PolicyKeys() {}

    /** Vendor property: "1" boots straight into the kiosk launcher, "0" normal. */
    public static final String KIOSK_MODE = "kiosk_mode";

    /** Vendor property: default state of the fake-camera provider ("1"/"0"). */
    public static final String FAKE_CAMERA = "fake_camera";

    /** Framework setting (NS_GLOBAL): "0" disables ADB. */
    public static final String ADB_ENABLED = "adb_enabled";
}
