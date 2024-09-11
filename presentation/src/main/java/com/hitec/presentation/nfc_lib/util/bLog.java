/**
 * <pre>
 * 로그 관련 클래스(DEBUG true(Constents클래스에서 설정) 시 로그 사용)
 * </pre>
 */
package com.hitec.presentation.nfc_lib.util;

import android.util.Log;

public class bLog {

    public static void i_hex(String tag, String msg, byte[] buffer, int length) {

        StringBuilder strText = new StringBuilder(msg);
        strText.append(" len[").append(length).append("]");
        for (int i = 0; i < length; i++) {
            strText.append(String.format(" %02X", buffer[i]));
        }

        Log.i(tag, strText.toString());
    }
}
