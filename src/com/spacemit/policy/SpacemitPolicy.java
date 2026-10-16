/*
 * Copyright (C) 2026 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */
package com.spacemit.policy;

/** Constants shared by the SpacemiT policy SDK, service and callers. */
public final class SpacemitPolicy {

    private SpacemitPolicy() {}

    /** Bump when the ISpacemitPolicyManager contract changes. */
    public static final int API_VERSION = 1;

    /** Package that hosts the bound policy service. */
    public static final String SERVICE_PACKAGE = "com.spacemit.policy.service";
    /** Action the service publishes; callers bind with this + SERVICE_PACKAGE. */
    public static final String ACTION_BIND = "com.spacemit.policy.action.BIND";

    /** Signature|privileged permission guarding every mutating call. */
    public static final String PERMISSION_MANAGE = "com.spacemit.policy.permission.MANAGE_DEVICE_POLICY";

    /**
     * Settings.Global key prefix the service uses to persist vendor-policy keys.
     * (A real vendor property for native/HAL consumers is a documented extension.)
     */
    public static final String PROP_PREFIX = "spacemit_policy_";

    /** Settings namespaces for setDeviceSetting(). */
    public static final int NS_GLOBAL = 0;
    public static final int NS_SECURE = 1;
    public static final int NS_SYSTEM = 2;
}
