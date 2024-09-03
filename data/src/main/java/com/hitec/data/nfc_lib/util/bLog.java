/**
 * <pre>
 * 로그 관련 클래스(DEBUG true(Constents클래스에서 설정) 시 로그 사용)
 * </pre>
 */
package com.hitec.data.nfc_lib.util;

import android.util.Log;

public class bLog {

    private static boolean DEBUG = true;

    public static void on() {
        DEBUG = true;
    }

    public static void off() {
        DEBUG = false;
    }

    public static void e(String tag, String msg) {
        if (DEBUG == false) {
            return;
        }
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        Log.e(tag + " ERROR:", msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG == false) {
            return;
        }
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        Log.d(tag + " DEBUG: ", msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG == false) {
            return;
        }
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        Log.i(tag + " INFO: ", msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG == false) {
            return;
        }
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        Log.v(tag + " VERBOSE: ", msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG == false) {
            return;
        }
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        Log.w(tag + " WARN: ", msg);
    }

    //yikim 2013.05.14
    public static void i_hex(String tag, String msg, byte[] pBuff, int nLen) {
        if (DEBUG == false) {
            return;
        }

        String strText = new String(pBuff, 0, nLen);
        strText = msg + " " + DevUtil.debugAsciiToString(pBuff, nLen) + "\n";
        Log.i(tag, strText);
    }
}
