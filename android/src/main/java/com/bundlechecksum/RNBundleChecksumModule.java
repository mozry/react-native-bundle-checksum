
package com.bundlechecksum;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RNBundleChecksumModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;


    public RNBundleChecksumModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBundleChecksum";
    }

    @ReactMethod
    public void getChecksum(Promise promise) {
        BufferedReader br = null;
        String bundle = null;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getReactApplicationContext().getAssets();
            InputStream stream = assetManager.open("index.android.bundle");
            if (stream == null) {
                promise.resolve("");
                return;
            }

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            bundle = new String(buffer);

            if (bundle == null || bundle.equals("")) {
                promise.resolve("");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.resolve("");
            return;
        }

        MessageDigest md = null;
        sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bundle.getBytes("UTF-8"));
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }

            promise.resolve(sb.toString());
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        }
    }

    @ReactMethod
    public void getChecksumCert(String certName, Promise promise) {
        BufferedReader br = null;
        String bundle = null;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getReactApplicationContext().getAssets();
            InputStream stream = assetManager.open(certName.toString() + ".cer");
            if (stream == null) {
                promise.resolve("");
                return;
            }

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            bundle = new String(buffer);

            if (bundle == null || bundle.equals("")) {
                promise.resolve("");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.resolve("");
            return;
        }

        MessageDigest md = null;
        sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bundle.getBytes("UTF-8"));
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }

            promise.resolve(sb.toString());
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        }
    }

    @ReactMethod
    public void getSumMETA(Promise promise) {
        try {
            Signature sigs = reactContext.getPackageManager().getPackageInfo(reactContext.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];

            promise.resolve(sigs.hashCode());
        }catch (Exception e){
            promise.resolve("");
        }

        return;
    }


}
