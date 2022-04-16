package com.iquadrat.sunmi;

import com.facebook.react.bridge.*;
import com.sunmi.scanner.IScanInterface;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SunmiBarcodeScannerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
    private static final String DATA = "data";
    private static final String SOURCE = "source_byte";

    private Promise mPromise;
    private static IScanInterface scanInterface;

    public SunmiBarcodeScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        try {
          bindScannerService();
          registerReceiver();
        } catch (Exception e) {
        }
    }

    @Override
    public String getName() {
        return "SunmiBarcodeScanner";
    }

    @ReactMethod
    public void scan(Promise promise) {
        if (scanInterface != null) {
            try {
                mPromise = promise;
                scanInterface.scan();
            } catch (Exception e) {
                e.printStackTrace();
                promise.reject("Scanner error");
            }
        } else {
            promise.reject("Scanner service is not bind");
        }
    }

    @ReactMethod
    public void stop() {
        if (scanInterface != null) {
            try {
                mPromise = null;
                scanInterface.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void bindScannerService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.scanner");
        intent.setAction("com.sunmi.scanner.IScanInterface");
        this.reactContext.bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DATA_CODE_RECEIVED);
        this.reactContext.registerReceiver(receiver, filter);
    }

    private static ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            scanInterface = IScanInterface.Stub.asInterface(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            scanInterface = null;
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            try {
                if (code != null && !code.isEmpty()) {
                    if (mPromise != null) {
                        mPromise.resolve(code);
                    }
                }
            } catch (Exception e) {
            }
        }
    };
}
