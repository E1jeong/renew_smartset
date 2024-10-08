/*
 ****************************************************************************
 * Copyright(c) 2014 NXP Semiconductors                                     *
 * All rights are reserved.                                                 *
 *                                                                          *
 * Software that is described herein is for illustrative purposes only.     *
 * This software is supplied "AS IS" without any warranties of any kind,    *
 * and NXP Semiconductors disclaims any and all warranties, express or      *
 * implied, including all implied warranties of merchantability,            *
 * fitness for a particular purpose and non-infringement of intellectual    *
 * property rights.  NXP Semiconductors assumes no responsibility           *
 * or liability for the use of the software, conveys no license or          *
 * rights under any patent, copyright, mask work right, or any other        *
 * intellectual property rights in or to any products. NXP Semiconductors   *
 * reserves the right to make changes in the software without notification. *
 * NXP Semiconductors also makes no representation or warranty that such    *
 * application will be suitable for the specified use without further       *
 * testing or modification.                                                 *
 *                                                                          *
 * Permission to use, copy, modify, and distribute this software and its    *
 * documentation is hereby granted, under NXP Semiconductors' relevant      *
 * copyrights in the software, without fee, provided that it is used in     *
 * conjunction with NXP Semiconductor products(UCODE I2C, NTAG I2C).        *
 * This  copyright, permission, and disclaimer notice must appear in all    *
 * copies of this code.                                                     *
 ****************************************************************************
 */
package com.hitec.presentation.nfc_lib.reader;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.util.Log;

import com.hitec.presentation.nfc_lib.crypto.CRC32Calculator;
import com.hitec.presentation.nfc_lib.exceptions.CommandNotSupportedException;
import com.hitec.presentation.nfc_lib.exceptions.NotPlusTagException;
import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.reader.I2C_Enabled_Commands.Access_Offset;
import com.hitec.presentation.nfc_lib.reader.I2C_Enabled_Commands.PT_I2C_Offset;
import com.hitec.presentation.nfc_lib.util.ConstNfc;
import com.hitec.presentation.nfc_lib.util.bLog;

import org.ndeftools.EmptyRecord;
import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.AndroidApplicationRecord;
import org.ndeftools.wellknown.SmartPosterRecord;
import org.ndeftools.wellknown.TextRecord;
import org.ndeftools.wellknown.UriRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Class for the different Demos.
 *
 * @author NXP67729
 */

public class Ntag_I2C_Connect implements ConstNfc {

    /**
     * DEFINES.
     */
    private static final int LAST_FOUR_BYTES = 4;
    private static final int DELAY_TIME = 100;
    private I2C_Enabled_Commands reader;
    private Tag tag;
    private static final String TAG = "Ntag_I2C_Connect";

    /**
     * Constructor.
     *
     * @param tag Tag with which the Demos should be performed
     */
    public Ntag_I2C_Connect(Tag tag, final byte[] passwd, final int authStatus) {
        try {
            Log.i(TAG, "Ntag_I2C_Connect - 00");
            if (tag == null) {
                Log.i(TAG, "Ntag_I2C_Connect - 01");
                this.tag = null;
                return;
            }
            this.tag = tag;

            reader = I2C_Enabled_Commands.get(tag);

            reader.connect();
            Log.i(TAG, "Ntag_I2C_Connect - 02");

            Ntag_Get_Version.Prod prod = reader.getProduct();

            if (!prod.equals(Ntag_Get_Version.Prod.Unknown)) {
                if (prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_1k_Plus) || prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_2k_Plus)) {
                    // Auth status gets lost after resetting the demo when we
                    // obtain the product we are dealing with
                    if (authStatus == Ntag_Auth.AuthStatus.Authenticated.getValue()) {
                        reader.authenticatePlus(passwd);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Log.i(TAG, "Ntag_I2C_Connect - 03");
        }
    }

    /**
     * Checks if the tag is still connected based on the tag.
     *
     * @return Boolean indicating tag presence
     */
    public static boolean isTagPresent(Tag tag) {
        final Ndef ndef = Ndef.get(tag);
        if (ndef != null && !ndef.getType().equals("android.ndef.unknown")) {
            try {
                ndef.connect();
                final boolean isConnected = ndef.isConnected();
                ndef.close();
                return isConnected;
            } catch (final IOException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        } else {
            final NfcA nfca = NfcA.get(tag);
            if (nfca != null) {
                try {
                    nfca.connect();
                    final boolean isConnected = nfca.isConnected();
                    nfca.close();

                    return isConnected;
                } catch (final IOException e) {
                    Log.e(TAG, e.toString());
                    return false;
                }
            } else {
                final NfcB nfcb = NfcB.get(tag);
                if (nfcb != null) {
                    try {
                        nfcb.connect();
                        final boolean isConnected = nfcb.isConnected();
                        nfcb.close();
                        return isConnected;
                    } catch (final IOException e) {
                        Log.e(TAG, e.toString());
                        return false;
                    }
                } else {
                    final NfcF nfcf = NfcF.get(tag);
                    if (nfcf != null) {
                        try {
                            nfcf.connect();
                            final boolean isConnected = nfcf.isConnected();
                            nfcf.close();
                            return isConnected;
                        } catch (final IOException e) {
                            Log.e(TAG, e.toString());
                            return false;
                        }
                    } else {
                        final NfcV nfcv = NfcV.get(tag);
                        if (nfcv != null) {
                            try {
                                nfcv.connect();
                                final boolean isConnected = nfcv.isConnected();
                                nfcv.close();
                                return isConnected;
                            } catch (final IOException e) {
                                Log.e(TAG, e.toString());
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the tag is still connected based on the previously detected reader.
     *
     * @return Boolean indicating tag connection
     */
    public boolean isConnected() {
        return reader.isConnected();
    }

    /**
     * Checks if the demo is ready to be executed.
     *
     * @return Boolean indicating demo readiness
     */
    public boolean isReady() {
        return tag != null && reader != null;
    }

    /**
     * Get the product from reader.
     *
     * @return product
     */
    public Ntag_Get_Version.Prod getProduct() throws IOException {
        return reader.getProduct();
    }

    /**
     * Resets the tag to its delivery values (including config registers).
     *
     * @return Boolean indicating success or error
     */
    public int resetTagMemory() {
        int bytesWritten;

        try {
            bytesWritten = reader.writeDeliveryNdef();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            bytesWritten = -1;
        }

        if (bytesWritten != 0) {
            byte NC_REG = (byte) 0x01;
            byte LD_Reg = (byte) 0x00;
            byte SM_Reg = (byte) 0xF8;
            byte WD_LS_Reg = (byte) 0x48;
            byte WD_MS_Reg = (byte) 0x08;
            byte I2C_CLOCK_STR = (byte) 0x01;
            // If we could reset the memory map, we should be able to write the config registers
            try {
                reader.writeConfigRegisters(
                        NC_REG,
                        LD_Reg,
                        SM_Reg,
                        WD_LS_Reg,
                        WD_MS_Reg,
                        I2C_CLOCK_STR
                );
            } catch (Exception e) {
                //Toast.makeText(main, "Error writing configuration registers", Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                bytesWritten = -1;
            }

            try {
                Ntag_Get_Version.Prod prod = reader.getProduct();

                if (prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_1k_Plus) || prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_2k_Plus)) {
                    byte AUTH0 = (byte) 0xFF;
                    byte ACCESS = (byte) 0x00;
                    byte PT_I2C = (byte) 0x00;
                    reader.writeAuthRegisters(AUTH0, ACCESS, PT_I2C);
                }
            } catch (Exception e) {
                //Toast.makeText(main, "Error writing authentication registers", Toast.LENGTH_LONG).show();
                Log.e(TAG, e.toString());
                bytesWritten = -1;
            }
        }
        return bytesWritten;
    }

    /**
     * Builds a String array for the NTAG I2C Plus Auth Register.
     *
     * @param auth0register Byte Array of the Registers
     */
    private Ntag_I2C_Plus_Registers getPlusAuth_Settings(
            byte[] auth0register,
            byte[] accessRegister,
            byte[] pti2cRegister
    ) {
        Ntag_I2C_Plus_Registers answerPlus = new Ntag_I2C_Plus_Registers();

        //Auth0 Register
        answerPlus.auth0 = (0x00000FF & auth0register[3]);

        //Access Register
        answerPlus.nfcProt = (0x0000080 & accessRegister[0]) >> Access_Offset.NFC_PROT.getValue() == 1;
        answerPlus.nfcDisSec1 = (0x0000020 & accessRegister[0]) >> Access_Offset.NFC_DIS_SEC1.getValue() == 1;
        answerPlus.authlim = (0x0000007 & accessRegister[0]);

        //PT I2C Register
        answerPlus.k2Prot = (0x0000008 & pti2cRegister[0]) >> PT_I2C_Offset.K2_PROT.getValue() == 1;
        answerPlus.sram_prot = (0x0000004 & pti2cRegister[0]) >> PT_I2C_Offset.SRAM_PROT.getValue() == 1;
        answerPlus.i2CProt = (0x0000003 & pti2cRegister[0]);
        return answerPlus;
    }

    /**
     * Reads the whole tag memory content.
     *
     * @return Boolean indicating success or error
     */
    public byte[] readTagContent() {
        byte[] bytes = null;
        try {
            // The user memory and the first four pages are displayed
            int memSize = reader.getProduct().getMemsize() + 16;
            // Read all the pages using the fast read method
            bytes = reader.readEEPROM(0, memSize / reader.getBlockSize());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return bytes;
    }

    /**
     * Resets the whole tag memory content (Memory to 00000...)
     *
     * @return Boolean indicating success or error
     */
    public boolean resetTagContent() {
        boolean success = true;
        try {
            byte[] d = new byte[reader.getProduct().getMemsize()];
            reader.writeEEPROM(d);
        } catch (IOException | FormatException e) {
            success = false;
            Log.e(TAG, e.toString());
        } catch (CommandNotSupportedException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.i(TAG, "resetTagContent - 05-1 Exception");
            Log.e(TAG, e.toString());
        }
        return success;
    }

    public String NfcReadNdef() {
        try {
            // Get the message Type
            NdefMessage msg = reader.readNDEF();

            // NDEF Reading time statistics
            Message highLevelMsg = new Message(msg);

            // Get the message type in order to deal with it appropriately
//            String type = "none";
//            if (!highLevelMsg.isEmpty()) {
//                type = highLevelMsg.get(0).getClass().getSimpleName();
//            }

            String message = getString(highLevelMsg);
            int bytes = msg.toByteArray().length;

            Log.i(TAG, "NfcReadNdef : message:" + message + " bytes:" + bytes);

            return message;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    private static String getString(Message highLevelMsg) {
        String message = "";

        for (Record rec : highLevelMsg) {
            if (rec instanceof EmptyRecord) {
                message = "";
            } else if (rec instanceof SmartPosterRecord smp) {
                message = smp.getUri().getUri().getHost() + smp.getUri().getUri().getPath();
            } else if (rec instanceof TextRecord) {
                message = ((TextRecord) rec).getText();
            } else if (rec instanceof AndroidApplicationRecord) {
                message = ((AndroidApplicationRecord) rec).getPackageName();
            } else if (rec instanceof UriRecord) {
                message = ((UriRecord) rec).getUri().getHost() + ((UriRecord) rec).getUri().getPath();
            } else if (rec instanceof MimeRecord) {
                message = ((MimeRecord) rec).getMimeType();
            } else {
                message = rec.getClass().getSimpleName();
            }
        }
        return message;
    }

    public boolean NfcWriteNdef(String writeText) {
        try {
            // NDEF Message to write in the tag
            NdefMessage msg;
            Log.i(TAG, "NfcWriteNdef - 01 writeText:" + writeText);

            String readText = NfcReadNdef();

            Log.i(TAG, "NfcWriteNdef - readText:" + readText);
            if (readText.equals(writeText)) {
                return true;
            }
            // Get the selected NDEF type since the creation of the NDEF Msg
            // will vary depending on the type
            msg = createNdefTextMessage(writeText);
            if (msg == null) {
                return false;
            }
            Log.v(TAG, "NfcWriteNdef - 03");
            reader.writeNDEF(msg);

            int bytes = msg.toByteArray().length;
            Log.i(TAG, "NfcWriteNdef : writeText:" + writeText + " bytes:" + bytes);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    private void waitI2CTime(int nfcType, int waitTime) {
        try {
            if (nfcType == NFC_TYPE_A) {
                Thread.sleep(waitTime);
                //reader.waitforI2Cwrite(waitTime);
            } else {
                reader.waitforI2Cwrite(waitTime);
            }
        } catch (Exception e) {
            Log.i(TAG, "waitI2CTime => Exception");
            Log.e(TAG, e.toString());
        }
    }

    public boolean NfcDataTransive(
            int nfcType,
            boolean fInitConnect,
            byte[] sendData,
            byte[] recvData,
            int respWaitTime,
            int startWaitTime
    ) {
        byte[] dataRx = new byte[reader.getSRAMSize()];

        // We have to make sure that the Pass-Through mode is activated
        long RegTimeOutStart = System.currentTimeMillis();
        boolean RTest;
        int i, j;
        try {
            Log.i(TAG, "NfcDataTransive : respWaitTime:" + respWaitTime + " startWaitTime:" + startWaitTime);

            // wait to prevent that a RF communication is
            // at the same time as 쨉C I2C
            //Thread.sleep(10);
            if (!fInitConnect) {
                do {
                    if (reader.checkPTwritePossible()) {
                        break;
                    }
                    long RegTimeOut = System.currentTimeMillis();
                    RegTimeOut = RegTimeOut - RegTimeOutStart;
                    RTest = (RegTimeOut < 5000);
                } while (RTest);

                waitI2CTime(nfcType, 100);
            } else {
                if (startWaitTime > 0) {
                    waitI2CTime(nfcType, startWaitTime);
                } else {
                    waitI2CTime(nfcType, 1);
                }
            }

            Log.i(TAG, "NfcDataTransive : device => Tag");
            reader.writeSRAMBlock(sendData);

            Log.i(TAG, "NfcDataTransive : device <= Tag");
            // wait to prevent that a RF communication is
            // at the same time as 쨉C I2C
            //Thread.sleep(100);
            waitI2CTime(nfcType, respWaitTime);

            int devCode = 0;
            int waitTime = 500;

            if (waitTime < respWaitTime) waitTime = respWaitTime;

            for (i = 0; i < waitTime; i += 50) {
                try {
                    waitI2CTime(nfcType, 50);
                    dataRx = reader.fast_readSRAMBlock();
                } catch (Exception e) {
                    Log.i(TAG, "NfcDataTransive => Exception : readSRAMBlock");
                    Log.e(TAG, e.toString());
                }
                devCode = dataRx[0] & 0xFF;
                if (devCode != DEVICE_CODE_SMART && devCode != 0x00 && dataRx.length > 10) {
                    //Copy
                    for (j = 0; j < dataRx.length; j++) {
                        recvData[j] = dataRx[j];
                    }
                    break;
                }
            }

            if (devCode == DEVICE_CODE_SMART || devCode == 0x00) {
                return false;
            }

            bLog.i_hex(TAG, "NfcDataTransive rx_data", dataRx, dataRx.length);
            return true;
        } catch (FormatException e) {
            Log.i(TAG, "NfcDataTransive => FormatException");
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.i(TAG, "NfcDataTransive => IOException");
            Log.e(TAG, e.toString());
        } catch (CommandNotSupportedException e) {
            Log.i(TAG, "NfcDataTransive => CommandNotSupportedException");
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.i(TAG, "NfcDataTransive => Exception");
            Log.e(TAG, e.toString());
        }

        return false;
    }

    public boolean NfcPeriodDataTransive(
            int nfcType,
            boolean fInitConnect,
            byte[] sendData,
            byte[] recvData,
            int respWaitTime,
            int startWaitTime
    ) {
        byte[] dataRx = new byte[reader.getSRAMSize()];

        // We have to make sure that the Pass-Through mode is activated
        long RegTimeOutStart = System.currentTimeMillis();
        boolean RTest;
        try {
            Log.i(TAG, "NfcPeriodDataTransive : respWaitTime:" + respWaitTime + " startWaitTime:" + startWaitTime);

            // wait to prevent that a RF communication is
            // at the same time as 쨉C I2C
            //Thread.sleep(10);
            if (!fInitConnect) {
                do {
                    if (reader.checkPTwritePossible()) {
                        break;
                    }
                    long RegTimeOut = System.currentTimeMillis();
                    RegTimeOut = RegTimeOut - RegTimeOutStart;
                    RTest = (RegTimeOut < 5000);
                } while (RTest);

                waitI2CTime(nfcType, 100);
            } else {
                if (startWaitTime > 0) {
                    waitI2CTime(nfcType, startWaitTime);
                } else {
                    waitI2CTime(nfcType, 1);
                }
            }

            Log.i(TAG, "NfcPeriodDataTransive : device => Tag");
            reader.writeSRAMBlock(sendData);

            Log.i(TAG, "NfcPeriodDataTransive : device <= Tag");
            // wait to prevent that a RF communication is
            // at the same time as 쨉C I2C
            //Thread.sleep(100);

            int devCode = 0;
            int msgType = 0;
            int startRxPos = 0;
            int defaultWaitTime = 500;
            int defaultReadTime = 20;
            int waitTime = defaultWaitTime;
            int totalBlock = 0; //기간검침용
            int currBlock = 0; //기간검침용
            int prevBlock = 0; //기간검침용
            int exceptionCnt = 0;

            if (waitTime < respWaitTime) waitTime = respWaitTime;

            //NFC 종류에 따라 wait 다름
            if (nfcType == NFC_TYPE_A) {
                defaultReadTime = 40;
            } else {
                defaultReadTime = 20;
            }

            for (int nTime = 0; nTime < waitTime; nTime += defaultReadTime) {
                try {
                    waitI2CTime(nfcType, defaultReadTime);
                    dataRx = reader.fast_readSRAMBlock();
                } catch (Exception e) {
                    Log.i(TAG, "NfcPeriodDataTransive => Exception : readSRAMBlock");
                    Log.e(TAG, e.toString());
                    exceptionCnt++;
                    if (exceptionCnt > 10) {
                        break;
                    }
                }

                Log.i(TAG, "NfcPeriodDataTransive Wait:" + nTime + " = waitTime:" + waitTime);

                if (dataRx.length > 10) {
                    devCode = dataRx[0] & 0xFF;
                    msgType = dataRx[10] & 0xFF;
                }

                if (devCode != DEVICE_CODE_SMART && devCode != 0x00 && dataRx.length > 10) {
                    Log.i(TAG, "NfcPeriodDataTransive msgType:" + msgType);
                    if (msgType == NfcConstant.NODE_RECV_PERIOD_METER_REPORT) {
                        //기간검침
                        totalBlock = dataRx[12] & 0xFF;
                        currBlock = dataRx[13] & 0xFF;
                        Log.i(TAG, "NfcPeriodDataTransive totalBlock:" + totalBlock + " currBlock:" + currBlock);
                        if (totalBlock == 0 && currBlock == 0) {
                            System.arraycopy(dataRx, 0, recvData, 0, dataRx.length);
                            break;
                        } else {
                            if (prevBlock != currBlock) {
                                nTime = 0;
                                prevBlock = currBlock;
                                Log.i(TAG, "startRxPos : " + startRxPos);
                                System.arraycopy(dataRx, 0, recvData, startRxPos, dataRx.length);
                                startRxPos += reader.getSRAMSize();
                            }

                            if (totalBlock == currBlock) {
                                break;
                            }
                        }
                    } else if (msgType == NfcConstant.NODE_RECV_FLASH_DATA_REPORT) {
                        //Flash Data 검침
                        totalBlock = dataRx[13] & 0xFF;
                        currBlock = dataRx[14] & 0xFF;
                        Log.i(TAG, "NfcFlashDataTransive totalBlock:" + totalBlock + " currBlock:" + currBlock);
                        if (totalBlock == 0 && currBlock == 0) {
                            System.arraycopy(dataRx, 0, recvData, 0, dataRx.length);
                            break;
                        } else {
                            if (prevBlock != currBlock) {
                                nTime = 0;
                                prevBlock = currBlock;
                                System.arraycopy(dataRx, 0, recvData, startRxPos, dataRx.length);
                                startRxPos += reader.getSRAMSize();
                            }

                            if (totalBlock == currBlock) {
                                break;
                            }
                        }
                    } else {
                        System.arraycopy(dataRx, 0, recvData, 0, dataRx.length);
                        break;
                    }
                }
            }

            if (devCode == DEVICE_CODE_SMART || devCode == 0x00) {
                return false;
            }

            bLog.i_hex(TAG, "NfcPeriodDataTransive rx_data:", dataRx, dataRx.length);
            return true;
        } catch (FormatException e) {
            Log.i(TAG, "NfcPeriodDataTransive => FormatException");
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.i(TAG, "NfcPeriodDataTransive => IOException");
            Log.e(TAG, e.toString());
        } catch (CommandNotSupportedException e) {
            Log.i(TAG, "NfcPeriodDataTransive => CommandNotSupportedException");
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.i(TAG, "NfcPeriodDataTransive => Exception");
            Log.e(TAG, e.toString());
        }

        return false;
    }

    /**
     * Retrieves the auth status of the tag
     *
     * @return int current auth status
     */
    public int ObtainAuthStatus() {
        try {
            Log.i(TAG, "ObtainAuthStatus - 01");
            Ntag_Get_Version.Prod prod = reader.getProduct();
            Log.i(TAG, "ObtainAuthStatus - prod:" + prod.getMemsize());

            if (!prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_1k_Plus) && !prod.equals(Ntag_Get_Version.Prod.NTAG_I2C_2k_Plus)) {
                Log.i(TAG, "ObtainAuthStatus - 03");
                return Ntag_Auth.AuthStatus.Disabled.getValue();
            } else {
                Log.i(TAG, "ObtainAuthStatus - 04");
                return reader.getProtectionPlus();
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "ObtainAuthStatus - 05");
        return Ntag_Auth.AuthStatus.Disabled.getValue();
    }

    /**
     * Performs the authentication operation on NTAG I2C Plus
     *
     * @param pwd        Byte Array containing the password
     * @param authStatus Current Authentication Status
     * @return Boolean operation result
     */
    public Boolean Auth(byte[] pwd, int authStatus) {
        Log.i(TAG, "Auth - 01 authStatus:" + authStatus);
        try {
            if (authStatus == Ntag_Auth.AuthStatus.Unprotected.getValue()) {
                Log.i(TAG, "Auth - 02");
                reader.protectPlus(pwd, Ntag_I2C_Commands.Register.Capability_Container.getValue());
            } else if (authStatus == Ntag_Auth.AuthStatus.Authenticated.getValue()) {
                Log.i(TAG, "Auth - 03");
                reader.unprotectPlus();
            } else if (authStatus == Ntag_Auth.AuthStatus.Protected_W.getValue() ||
                    authStatus == Ntag_Auth.AuthStatus.Protected_RW.getValue() ||
                    authStatus == Ntag_Auth.AuthStatus.Protected_W_SRAM.getValue() ||
                    authStatus == Ntag_Auth.AuthStatus.Protected_RW_SRAM.getValue()
            ) {
                byte[] pack = reader.authenticatePlus(pwd);
                bLog.i_hex(TAG, "Auth - pack:", pack, pack.length);

                if (pack.length < 2) {
                    Log.i(TAG, "Auth - 04");
                    return false;
                }
            }
            Log.i(TAG, "Auth - 05");
            return true;
        } catch (IOException | NotPlusTagException | FormatException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.i(TAG, "Auth - 05-1 Exception");
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Auth - 06");

        return false;
    }

    /***
     * Helper method to adjusts the number of bytes to be sent during the last flashing sector
     *
     * @param num to round up
     * @return number roundep up
     */
    int roundUp(int num) {
        if (num <= 256) {
            return 256;
        } else if (num <= 512) {
            return 512;
        } else if (num <= 1024) {
            return 1024;
        } else {
            return 4096;
        }
    }

    /**
     * Appends the CRC32 to the message transmitted during the SRAM SpeedTest.
     *
     * @param bytes The content to be transmitted to the NTAGI2C board
     * @return Byte Array with the CRC32s embedded in it
     */
    private byte[] appendCRC32(byte[] bytes) {
        byte[] temp = new byte[bytes.length - LAST_FOUR_BYTES];
        System.arraycopy(bytes, 0, temp, 0, temp.length);
        byte[] crc = CRC32Calculator.CRC32(temp);
        System.arraycopy(crc, 0, bytes, bytes.length - crc.length, crc.length);
        return bytes;
    }

    /**
     * Checks if the CRC32 value has been appended in the message.
     *
     * @param bytes The whole message received from the board
     * @return boolean that indicates the presence of the CRC32
     */
    private boolean isCRC32Appended(byte[] bytes) {
        for (int i = bytes.length - LAST_FOUR_BYTES; i < bytes.length; i++) {
            if (bytes[i] != 0x00) return true;
        }
        return false;
    }

    /**
     * Checks the received CRC32 value in the message received from the NTAG I2C
     * board during the SRAM SpeedTest.
     *
     * @param bytes The whole message received from the board
     * @return boolean with the result of the comparison between the CRC32
     * received and the CRC32 calculated
     */
    private boolean isValidCRC32(byte[] bytes) {
        byte[] receivedCRC = {
                bytes[bytes.length - LAST_FOUR_BYTES],
                bytes[bytes.length - 3],
                bytes[bytes.length - 2],
                bytes[bytes.length - 1],
        };

        byte[] temp = new byte[bytes.length - LAST_FOUR_BYTES];
        System.arraycopy(bytes, 0, temp, 0, bytes.length - LAST_FOUR_BYTES);
        byte[] calculatedCRC = CRC32Calculator.CRC32(temp);
        return Arrays.equals(receivedCRC, calculatedCRC);
    }

    protected byte[] concat(byte[] one, byte[] two) {
        if (one == null) {
            one = new byte[0];
        }
        if (two == null) {
            two = new byte[0];
        }
        byte[] combined = new byte[one.length + two.length];
        System.arraycopy(one, 0, combined, 0, one.length);
        System.arraycopy(two, 0, combined, one.length, two.length);
        return combined;
    }

    /**
     * Creates a NDEF Text Message.
     *
     * @param text Text to write
     * @return NDEF Message
     */
    private NdefMessage createNdefTextMessage(String text) {
        if (text.isEmpty()) {
            return null;
        }
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes(StandardCharsets.US_ASCII);
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
        NdefRecord record = new NdefRecord(
                NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT,
                new byte[0],
                payload
        );
        NdefRecord[] records = {record};
        return new NdefMessage(records);
    }
}
