/*
 * Copyright (C) 2026 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */
package com.spacemit.policy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Client entry point to the SpacemiT device-policy service. Bind once, call the
 * policy methods while connected, unbind when done. Callers need
 * {@link SpacemitPolicy#PERMISSION_MANAGE} for the mutating calls.
 *
 * <pre>
 *   SpacemitPolicyManager pm = SpacemitPolicyManager.create(context);
 *   pm.bind(new SpacemitPolicyManager.ConnectionListener() {
 *       public void onConnected()    { pm.setVendorProperty(PolicyKeys.KIOSK_MODE, "1"); }
 *       public void onDisconnected() { }
 *   });
 * </pre>
 */
public final class SpacemitPolicyManager {

    /** Delivered on the main thread when the service connects or drops. */
    public interface ConnectionListener {
        void onConnected();
        void onDisconnected();
    }

    private final Context mContext;
    private ISpacemitPolicyManager mService;
    private ConnectionListener mListener;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder binder) {
            mService = ISpacemitPolicyManager.Stub.asInterface(binder);
            if (mListener != null) mListener.onConnected();
        }
        @Override public void onServiceDisconnected(ComponentName name) {
            mService = null;
            if (mListener != null) mListener.onDisconnected();
        }
    };

    private SpacemitPolicyManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static SpacemitPolicyManager create(Context context) {
        return new SpacemitPolicyManager(context);
    }

    /** Bind to the service; {@code listener} fires on connect/disconnect. */
    public boolean bind(ConnectionListener listener) {
        mListener = listener;
        Intent intent = new Intent(SpacemitPolicy.ACTION_BIND)
                .setPackage(SpacemitPolicy.SERVICE_PACKAGE);
        return mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        try {
            mContext.unbindService(mConnection);
        } catch (IllegalArgumentException ignored) {
            // not bound
        }
        mService = null;
        mListener = null;
    }

    public boolean isConnected() {
        return mService != null;
    }

    private ISpacemitPolicyManager require() {
        if (mService == null) {
            throw new IllegalStateException("SpacemitPolicyManager is not connected");
        }
        return mService;
    }

    public int getApiVersion() {
        try { return require().getApiVersion(); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public boolean setVendorProperty(String key, String value) {
        try { return require().setVendorProperty(key, value); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public String getVendorProperty(String key, String defValue) {
        try { return require().getVendorProperty(key, defValue); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public boolean setDeviceSetting(int namespace, String key, String value) {
        try { return require().setDeviceSetting(namespace, key, value); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public String getDeviceSetting(int namespace, String key) {
        try { return require().getDeviceSetting(namespace, key); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public Bundle getAppliedPolicies() {
        try { return require().getAppliedPolicies(); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }

    public void reapplyAll() {
        try { require().reapplyAll(); }
        catch (RemoteException e) { throw e.rethrowFromSystemServer(); }
    }
}
