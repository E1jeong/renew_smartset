package com.hitec.presentation.nfc_lib.reader;

public class Ntag_Auth {

    public enum AuthStatus {
        Disabled(0),
        Unprotected(1),
        Authenticated(2),
        Protected_W(3),
        Protected_RW(4),
        Protected_W_SRAM(5),
        Protected_RW_SRAM(6);

        private int status;

        private AuthStatus(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum Pwds {
        PWD0(new byte[]{(byte) 0x3A, (byte) 0x5A, (byte) 0xA5, (byte) 0xA3});

        private byte[] pwd;

        private Pwds(byte[] value) {
            this.pwd = value;
        }

        public byte[] getValue() {
            return pwd;
        }
    }
}
