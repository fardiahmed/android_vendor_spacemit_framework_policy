package com.spacemit.policy;

import android.os.Bundle;

/**
 * Binder API of the SpacemiT device-policy service.
 * Every mutating call requires com.spacemit.policy.permission.MANAGE_DEVICE_POLICY.
 */
interface ISpacemitPolicyManager {
    /** API revision of the running service (see SpacemitPolicy.API_VERSION). */
    int getApiVersion();

    /** Set a vendor property persist.vendor.spacemit.policy.<key>; returns false if rejected. */
    boolean setVendorProperty(String key, String value);
    String getVendorProperty(String key, String defValue);

    /** Write a framework setting; namespace is SpacemitPolicy.NS_GLOBAL/SECURE/SYSTEM. */
    boolean setDeviceSetting(int namespace, String key, String value);
    String getDeviceSetting(int namespace, String key);

    /** Snapshot of everything the service has applied (key -> value), for compliance reporting. */
    Bundle getAppliedPolicies();

    /** Re-assert every persisted policy onto the device (called on boot). */
    void reapplyAll();
}
