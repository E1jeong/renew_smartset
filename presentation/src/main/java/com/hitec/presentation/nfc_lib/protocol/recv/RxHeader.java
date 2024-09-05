/**
 * <pre>
 * RxHeader 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class RxHeader extends NfcRxMessage {

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        return parseCompleted();
    }
}
