/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class NfcHeaderParse extends NfcRxMessage {

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        return parseCompleted();
    }
}
