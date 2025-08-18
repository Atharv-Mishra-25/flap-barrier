package com.dpdtech.application.util;

import android.util.Log;
import android.view.KeyEvent;

import com.tgw.BuildConfig;

public class ScanReaderHelper {

    private static String barcode = "";

    public static void clearBarcode() {
        ScanReaderHelper.barcode = "";
        Log.d("ScanReaderHelper", "CLEAR");
    }

    public static boolean readHardwareScan(KeyEvent e, OnScanListener onScan) {
        if (e.getAction() == KeyEvent.ACTION_DOWN) {
            char pressedKey = (char) e.getUnicodeChar();

            String keyString = String.valueOf(pressedKey);
            if (keyString.matches("^[A-Za-z0-9+\\=]+$") ||
                    keyString.equals("-") ||
                    keyString.equals("/") ||
                    keyString.equals(" ")) {

                barcode += pressedKey;
            }

            if ((e.getKeyCode() == KeyEvent.KEYCODE_ENTER || e.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || e.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) && !barcode.isEmpty()) {
                onScan.onScan(barcode);
                barcode = "";
                return true;
            }
        }

        return !barcode.isEmpty();
    }

    public interface OnScanListener {
        void onScan(String barcode);
    }

    public static boolean isEnteredPressed(KeyEvent e) {
        return (e.getKeyCode() == KeyEvent.KEYCODE_ENTER || e.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || e.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER);
    }
}
