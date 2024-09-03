/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class FwVerReport extends NfcRxMessage {

    private String strFwVer = "";

    public String GetFwVersion() {
        return strFwVer;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }
        strFwVer = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        return parseCompleted();
    }
}
