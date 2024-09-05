/**
 * <pre>
 * NFC 프로토콜 클래스
 * </pre>
 */
package com.hitec.presentation.nfc_lib.util;

import java.util.Calendar;

public class Meter {

    public static final int eUnknown = 0;
    public static final int eWater = 1;

    //Decimal data check
    public static boolean CheckMeterValueValid(
            int[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int i;
        int srcBuff;
        int byHigh, byLow;

        for (i = 0; i < nLen; i++) {
            srcBuff = (int) pBuff[nOffSet + i];
            byHigh = (int) ((srcBuff & 0xf0) >> 4);
            byLow = (int) (srcBuff & 0x0f);

            if (byHigh > 9 || byLow > 9) return false;
        }

        return true;
    }

    //Event Type
    public static int GetEventType(int byType) {
        int nEventType;

        if ((byType & 0x01) == 0x01) {
            nEventType = g_emEventType.eJoin;
        } else if ((byType & 0x02) == 0x02) {
            nEventType = g_emEventType.eFlood;
        } else if ((byType & 0x04) == 0x04) {
            nEventType = g_emEventType.eTemper;
        } else if ((byType & 0x08) == 0x08) {
            nEventType = g_emEventType.eMeterDown;
        } else if ((byType & 0x10) == 0x10) {
            nEventType = g_emEventType.eSubError;
        } else {
            nEventType = g_emEventType.eUnknown;
        }

        return nEventType;
    }

    //계량기 종류
    public static int GetMeterUtility(int byType) {
        int eUtility;

        if (CheckMeterWaterType(byType) == true) {
            eUtility = g_emMeterUtility.eWater;
        } else if (CheckMeterGasType(byType) == true) {
            eUtility = g_emMeterUtility.eGas;
        } else if (CheckMeterCaloriType(byType) == true) {
            eUtility = g_emMeterUtility.eCalori;
        } else if (CheckMeterHotwaterType(byType) == true) {
            eUtility = g_emMeterUtility.eHotwater;
        } else if (CheckMeterElectronicType(byType) == true) {
            eUtility = g_emMeterUtility.eElectronic;
        } else {
            eUtility = g_emMeterUtility.eElectronic;
        }

        return eUtility;
    }

    public static String GetMeterTypeString(int eMeterType) {
        String strMeterType = "";
        int eUtility = GetMeterUtility(eMeterType);

        switch (eUtility) {
            case g_emMeterUtility.eWater:
                strMeterType = GetMeterWaterTypeString(eMeterType);
                break;
            case g_emMeterUtility.eGas:
                strMeterType = GetMeterGasTypeString(eMeterType);
                break;
            case g_emMeterUtility.eCalori:
                strMeterType = GetMeterCaloriTypeString(eMeterType);
                break;
            case g_emMeterUtility.eHotwater:
                strMeterType = GetMeterHotwaterTypeString(eMeterType);
                break;
            case g_emMeterUtility.eElectronic:
                strMeterType = GetMeterElectronicTypeString(eMeterType);
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    //수도 계량기
    public static String GetMeterWaterTypeString(int eMeterType) {
        String strMeterType = "";

        switch (eMeterType) {
            case (int) g_emMeterWaterType.eStandardDigital:
                strMeterType = "STD Digital";
                break;
            case (int) g_emMeterWaterType.eHitecDigital:
                strMeterType = "HT Digital";
                break;
            case (int) g_emMeterWaterType.eHitecShDigital:
                strMeterType = "H-SH Digital";
                break;
            case (int) g_emMeterWaterType.eShinhanDigital:
                strMeterType = "SH Digital";
                break;
            case (int) g_emMeterWaterType.eShinhanDigitalBig:
                strMeterType = "SH Big Dig";
                break;
            case (int) g_emMeterWaterType.eMnSDigital:
                strMeterType = "MnS Digital";
                break;
            case (int) g_emMeterWaterType.ePrimo:
                strMeterType = "Primo";
                break;
            case (int) g_emMeterWaterType.eBadger:
                strMeterType = "Badger";
                break;
            case (int) g_emMeterWaterType.eModbusYk:
                strMeterType = "Modbus YK Flow";
                break;
            case (int) g_emMeterWaterType.eOneTLDigital:
                strMeterType = "OneTL Digital";
                break;
            case (int) g_emMeterWaterType.eIcmDplc:
                strMeterType = "ICM DPLC";
                break;
            case (int) g_emMeterWaterType.ePulse_1000L:
                strMeterType = "W-Pulse 1000L";
                break;
            case (int) g_emMeterWaterType.ePulse_500L:
                strMeterType = "W-Pulse 500L";
                break;
            case (int) g_emMeterWaterType.ePulse_100L:
                strMeterType = "W-Pulse 100L";
                break;
            case (int) g_emMeterWaterType.ePulse_50L:
                strMeterType = "W-Pulse 50L";
                break;
            case (int) g_emMeterWaterType.ePulse_10L:
                strMeterType = "W-Pulse 10L";
                break;
            case (int) g_emMeterWaterType.ePulse_5L:
                strMeterType = "W-Pulse 5L";
                break;
            case (int) g_emMeterWaterType.ePulse_1L:
                strMeterType = "W-Pulse 1L";
                break;
            case (int) g_emMeterWaterType.ePulse_05L:
                strMeterType = "W-Pulse 0.5L";
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    //가스 계량기
    public static String GetMeterGasTypeString(int eMeterType) {
        String strMeterType = "";

        switch (eMeterType) {
            case (int) g_emMeterGasType.eStandardDigital:
                strMeterType = "G-STD Digital";
                break;
            case (int) g_emMeterGasType.eOneTLDigital:
                strMeterType = "G-OneTL Digital";
                break;
            case (int) g_emMeterGasType.eIcmDplc:
                strMeterType = "G-ICM DPLC";
                break;
            case (int) g_emMeterGasType.ePulse_1000L:
                strMeterType = "G-Pulse 1000L";
                break;
            case (int) g_emMeterGasType.ePulse_500L:
                strMeterType = "G-Pulse 500L";
                break;
            case (int) g_emMeterGasType.ePulse_100L:
                strMeterType = "G-Pulse 100L";
                break;
            case (int) g_emMeterGasType.ePulse_50L:
                strMeterType = "G-Pulse 50L";
                break;
            case (int) g_emMeterGasType.ePulse_10L:
                strMeterType = "G-Pulse 10L";
                break;
            case (int) g_emMeterGasType.ePulse_5L:
                strMeterType = "G-Pulse 5L";
                break;
            case (int) g_emMeterGasType.ePulse_1L:
                strMeterType = "G-Pulse 1L";
                break;
            case (int) g_emMeterGasType.ePulse_05L:
                strMeterType = "G-Pulse 0.5L";
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    //열량 계량기
    public static String GetMeterCaloriTypeString(int eMeterType) {
        String strMeterType = "";

        switch (eMeterType) {
            case (int) g_emMeterCaloriType.eStandardDigital:
                strMeterType = "C-STD Digital";
                break;
            case (int) g_emMeterCaloriType.eOneTLDigital:
                strMeterType = "C-OneTL Digital";
                break;
            case (int) g_emMeterCaloriType.eIcmDplc:
                strMeterType = "C-ICM DPLC";
                break;
            case (int) g_emMeterCaloriType.ePulse_1000L:
                strMeterType = "C-Pulse 1000L";
                break;
            case (int) g_emMeterCaloriType.ePulse_500L:
                strMeterType = "C-Pulse 500L";
                break;
            case (int) g_emMeterCaloriType.ePulse_100L:
                strMeterType = "C-Pulse 100L";
                break;
            case (int) g_emMeterCaloriType.ePulse_50L:
                strMeterType = "C-Pulse 50L";
                break;
            case (int) g_emMeterCaloriType.ePulse_10L:
                strMeterType = "C-Pulse 10L";
                break;
            case (int) g_emMeterCaloriType.ePulse_5L:
                strMeterType = "C-Pulse 5L";
                break;
            case (int) g_emMeterCaloriType.ePulse_1L:
                strMeterType = "C-Pulse 1L";
                break;
            case (int) g_emMeterCaloriType.ePulse_05L:
                strMeterType = "C-Pulse 0.5L";
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    //열량 계량기
    public static String GetMeterHotwaterTypeString(int eMeterType) {
        String strMeterType = "";

        switch (eMeterType) {
            case (int) g_emMeterHotwaterType.eStandardDigital:
                strMeterType = "H-STD Digital";
                break;
            case (int) g_emMeterHotwaterType.eOneTLDigital:
                strMeterType = "H-OneTL Digital";
                break;
            case (int) g_emMeterHotwaterType.eIcmDplc:
                strMeterType = "H-ICM DPLC";
                break;
            case (int) g_emMeterHotwaterType.ePulse_1000L:
                strMeterType = "H-Pulse 1000L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_500L:
                strMeterType = "H-Pulse 500L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_100L:
                strMeterType = "H-Pulse 100L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_50L:
                strMeterType = "H-Pulse 50L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_10L:
                strMeterType = "H-Pulse 10L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_5L:
                strMeterType = "H-Pulse 5L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_1L:
                strMeterType = "H-Pulse 1L";
                break;
            case (int) g_emMeterHotwaterType.ePulse_05L:
                strMeterType = "H-Pulse 0.5L";
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    //열량 계량기
    public static String GetMeterElectronicTypeString(int eMeterType) {
        String strMeterType = "";

        switch (eMeterType) {
            case (int) g_emMeterElectronicType.eStandardDigital:
                strMeterType = "E-STD Digital";
                break;
            case (int) g_emMeterElectronicType.ePulse_1000W:
                strMeterType = "E-Pulse 1000W";
                break;
            case (int) g_emMeterElectronicType.ePulse_100W:
                strMeterType = "E-Pulse 100W";
                break;
            case (int) g_emMeterElectronicType.ePulse_10W:
                strMeterType = "E-Pulse 10W";
                break;
            case (int) g_emMeterElectronicType.ePulse_8W:
                strMeterType = "E-Pulse 8W";
                break;
            case (int) g_emMeterElectronicType.ePulse_4W:
                strMeterType = "E-Pulse 4W";
                break;
            case (int) g_emMeterElectronicType.ePulse_2W:
                strMeterType = "E-Pulse 2W";
                break;
            case (int) g_emMeterElectronicType.ePulse_1W:
                strMeterType = "E-Pulse 1W";
                break;
            case (int) g_emMeterElectronicType.ePulse_04W:
                strMeterType = "E-Pulse 0.4W";
                break;
            case (int) g_emMeterElectronicType.ePulse_02W:
                strMeterType = "E-Pulse 0.2W";
                break;
            case (int) g_emMeterElectronicType.ePulse_01W:
                strMeterType = "E-Pulse 0.1W";
                break;
            default:
                strMeterType = "Unknown";
                break;
        }
        return strMeterType;
    }

    public static int parseMeterType(int eUtility, int byType) {
        int nMeterType = 0;

        switch (eUtility) {
            case g_emMeterUtility.eWater:
            case g_emMeterUtility.eGas:
            case g_emMeterUtility.eCalori:
            case g_emMeterUtility.eHotwater:
            case g_emMeterUtility.eElectronic:
                nMeterType = byType;
                break;
            default:
                nMeterType = (int) g_emMeterWaterType.eUnknown;
                break;
        }
        return nMeterType;
    }

    //*************************************
    //*********** Parser Meter data *******
    //*************************************

    //수도계량기
    public static boolean CheckMeterWaterType(int byType) {
        switch (byType) {
            case (int) g_emMeterWaterType.eStandardDigital:
            case (int) g_emMeterWaterType.eHitecDigital:
            case (int) g_emMeterWaterType.eHitecShDigital:
            case (int) g_emMeterWaterType.eShinhanDigital:
            case (int) g_emMeterWaterType.eShinhanDigitalBig:
            case (int) g_emMeterWaterType.eMnSDigital:
            case (int) g_emMeterWaterType.ePrimo:
            case (int) g_emMeterWaterType.eBadger:
            case (int) g_emMeterWaterType.eModbusYk:
            case (int) g_emMeterWaterType.eOneTLDigital:
            case (int) g_emMeterWaterType.eIcmDplc:
            case (int) g_emMeterWaterType.ePulse_1000L:
            case (int) g_emMeterWaterType.ePulse_500L:
            case (int) g_emMeterWaterType.ePulse_100L:
            case (int) g_emMeterWaterType.ePulse_50L:
            case (int) g_emMeterWaterType.ePulse_10L:
            case (int) g_emMeterWaterType.ePulse_5L:
            case (int) g_emMeterWaterType.ePulse_1L:
            case (int) g_emMeterWaterType.ePulse_05L:
                return true;
            default:
                return false;
        }
    }

    //가스계량기
    public static boolean CheckMeterGasType(int byType) {
        switch (byType) {
            case (int) g_emMeterGasType.eStandardDigital:
            case (int) g_emMeterGasType.eOneTLDigital:
            case (int) g_emMeterGasType.eIcmDplc:
            case (int) g_emMeterGasType.ePulse_1000L:
            case (int) g_emMeterGasType.ePulse_500L:
            case (int) g_emMeterGasType.ePulse_100L:
            case (int) g_emMeterGasType.ePulse_50L:
            case (int) g_emMeterGasType.ePulse_10L:
            case (int) g_emMeterGasType.ePulse_5L:
            case (int) g_emMeterGasType.ePulse_1L:
            case (int) g_emMeterGasType.ePulse_05L:
                return true;
            default:
                return false;
        }
    }

    //열량계량기
    public static boolean CheckMeterCaloriType(int byType) {
        switch (byType) {
            case (int) g_emMeterCaloriType.eStandardDigital:
            case (int) g_emMeterCaloriType.eOneTLDigital:
            case (int) g_emMeterCaloriType.eIcmDplc:
            case (int) g_emMeterCaloriType.ePulse_1000L:
            case (int) g_emMeterCaloriType.ePulse_500L:
            case (int) g_emMeterCaloriType.ePulse_100L:
            case (int) g_emMeterCaloriType.ePulse_50L:
            case (int) g_emMeterCaloriType.ePulse_10L:
            case (int) g_emMeterCaloriType.ePulse_5L:
            case (int) g_emMeterCaloriType.ePulse_1L:
            case (int) g_emMeterCaloriType.ePulse_05L:
                return true;
            default:
                return false;
        }
    }

    //온수계량기
    public static boolean CheckMeterHotwaterType(int byType) {
        switch (byType) {
            case (int) g_emMeterHotwaterType.eStandardDigital:
            case (int) g_emMeterHotwaterType.eOneTLDigital:
            case (int) g_emMeterHotwaterType.eIcmDplc:
            case (int) g_emMeterHotwaterType.ePulse_1000L:
            case (int) g_emMeterHotwaterType.ePulse_500L:
            case (int) g_emMeterHotwaterType.ePulse_100L:
            case (int) g_emMeterHotwaterType.ePulse_50L:
            case (int) g_emMeterHotwaterType.ePulse_10L:
            case (int) g_emMeterHotwaterType.ePulse_5L:
            case (int) g_emMeterHotwaterType.ePulse_1L:
            case (int) g_emMeterHotwaterType.ePulse_05L:
                return true;
            default:
                return false;
        }
    }

    //전기계량기
    public static boolean CheckMeterElectronicType(int byType) {
        switch (byType) {
            case (int) g_emMeterElectronicType.eStandardDigital:
            case (int) g_emMeterElectronicType.ePulse_1000W:
            case (int) g_emMeterElectronicType.ePulse_100W:
            case (int) g_emMeterElectronicType.ePulse_10W:
            case (int) g_emMeterElectronicType.ePulse_8W:
            case (int) g_emMeterElectronicType.ePulse_4W:
            case (int) g_emMeterElectronicType.ePulse_2W:
            case (int) g_emMeterElectronicType.ePulse_1W:
            case (int) g_emMeterElectronicType.ePulse_04W:
            case (int) g_emMeterElectronicType.ePulse_02W:
            case (int) g_emMeterElectronicType.ePulse_01W:
                return true;
            default:
                return false;
        }
    }

    public static int CheckHitecShMeterType(int byStatus) {
        int nDstMeterType;
        if ((byStatus & 0xC5) == 0xC5) {
            nDstMeterType = (int) g_emMeterWaterType.eHitecShDigital;
        } else {
            nDstMeterType = (int) g_emMeterWaterType.eShinhanDigital;
        }

        return nDstMeterType;
    }

    //계량기 소숫점 자리
    public static int GetMeterValuePoint(int eUtility, int eMeterType) {
        int nValuePoint = 3;

        switch (eUtility) {
            case g_emMeterUtility.eWater:
                return GetMeterWaterValuePoint(eMeterType);
            case g_emMeterUtility.eGas:
                return GetMeterGasValuePoint(eMeterType);
            case g_emMeterUtility.eCalori:
                return GetMeterCaloriValuePoint(eMeterType);
            case g_emMeterUtility.eHotwater:
                return GetMeterHotwaterValuePoint(eMeterType);
            case g_emMeterUtility.eElectronic:
                return GetMeterElectronicValuePoint(eMeterType);
            default:
                break;
        }

        return nValuePoint;
    }

    //수도
    public static int GetMeterWaterValuePoint(int eMeterType) {
        int nValuePoint = 3;

        if (
                eMeterType == (int) g_emMeterWaterType.ePulse_1000L ||
                        eMeterType == (int) g_emMeterWaterType.ePulse_500L
        ) {
            nValuePoint = 0;
        } else if (
                eMeterType == (int) g_emMeterWaterType.ePulse_100L ||
                        eMeterType == (int) g_emMeterWaterType.ePulse_50L
        ) {
            nValuePoint = 1;
        } else if (
                eMeterType == (int) g_emMeterWaterType.ePulse_10L ||
                        eMeterType == (int) g_emMeterWaterType.ePulse_5L
        ) {
            nValuePoint = 2;
        } else if (
                eMeterType == (int) g_emMeterWaterType.ePulse_1L ||
                        eMeterType == (int) g_emMeterWaterType.ePulse_05L
        ) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterWaterType.eOneTLDigital) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterWaterType.eIcmDplc) {
            nValuePoint = 2;
        } else {
            nValuePoint = 3;
        }

        return nValuePoint;
    }

    //가스
    public static int GetMeterGasValuePoint(int eMeterType) {
        int nValuePoint = 3;

        if (
                eMeterType == (int) g_emMeterGasType.ePulse_1000L ||
                        eMeterType == (int) g_emMeterGasType.ePulse_500L
        ) {
            nValuePoint = 0;
        } else if (
                eMeterType == (int) g_emMeterGasType.ePulse_100L ||
                        eMeterType == (int) g_emMeterGasType.ePulse_50L
        ) {
            nValuePoint = 1;
        } else if (
                eMeterType == (int) g_emMeterGasType.ePulse_10L ||
                        eMeterType == (int) g_emMeterGasType.ePulse_5L
        ) {
            nValuePoint = 2;
        } else if (
                eMeterType == (int) g_emMeterGasType.ePulse_1L ||
                        eMeterType == (int) g_emMeterGasType.ePulse_05L
        ) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterGasType.eOneTLDigital) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterGasType.eIcmDplc) {
            nValuePoint = 2;
        } else {
            nValuePoint = 3;
        }

        return nValuePoint;
    }

    //열량
    public static int GetMeterCaloriValuePoint(int eMeterType) {
        int nValuePoint = 3;

        if (
                eMeterType == (int) g_emMeterCaloriType.ePulse_1000L ||
                        eMeterType == (int) g_emMeterCaloriType.ePulse_500L
        ) {
            nValuePoint = 0;
        } else if (
                eMeterType == (int) g_emMeterCaloriType.ePulse_100L ||
                        eMeterType == (int) g_emMeterCaloriType.ePulse_50L
        ) {
            nValuePoint = 1;
        } else if (
                eMeterType == (int) g_emMeterCaloriType.ePulse_10L ||
                        eMeterType == (int) g_emMeterCaloriType.ePulse_5L
        ) {
            nValuePoint = 2;
        } else if (
                eMeterType == (int) g_emMeterCaloriType.ePulse_1L ||
                        eMeterType == (int) g_emMeterCaloriType.ePulse_05L
        ) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterCaloriType.eOneTLDigital) {
            nValuePoint = 3; //0 = KWH, 3 = MWH
        } else if (eMeterType == (int) g_emMeterCaloriType.eIcmDplc) {
            nValuePoint = 2; //0 = KWH, 2 = MWH
        } else {
            nValuePoint = 3;
        }

        return nValuePoint;
    }

    //온수
    public static int GetMeterHotwaterValuePoint(int eMeterType) {
        int nValuePoint = 3;

        if (
                eMeterType == (int) g_emMeterHotwaterType.ePulse_1000L ||
                        eMeterType == (int) g_emMeterHotwaterType.ePulse_500L
        ) {
            nValuePoint = 0;
        } else if (
                eMeterType == (int) g_emMeterHotwaterType.ePulse_100L ||
                        eMeterType == (int) g_emMeterHotwaterType.ePulse_50L
        ) {
            nValuePoint = 1;
        } else if (
                eMeterType == (int) g_emMeterHotwaterType.ePulse_10L ||
                        eMeterType == (int) g_emMeterHotwaterType.ePulse_5L
        ) {
            nValuePoint = 2;
        } else if (
                eMeterType == (int) g_emMeterHotwaterType.ePulse_1L ||
                        eMeterType == (int) g_emMeterHotwaterType.ePulse_05L
        ) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterHotwaterType.eOneTLDigital) {
            nValuePoint = 3;
        } else if (eMeterType == (int) g_emMeterHotwaterType.eIcmDplc) {
            nValuePoint = 2;
        } else {
            nValuePoint = 3;
        }

        return nValuePoint;
    }

    //전기
    public static int GetMeterElectronicValuePoint(int eMeterType) {
        int nValuePoint = 2;

        return nValuePoint;
    }

    //Meter Data
    public static String ParserMeterVal(byte[] pBuff, int nOffset, int nLen) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        String strIntPart = strTmpMeter.substring(0, 5);
        String strDecimalPart = strTmpMeter.substring(5, 8);
        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        String strMeterVal = strIntPart + "." + strDecimalPart;
        return strMeterVal;
    }

    //Meter Data
    public static String ParserMeterVal(
            byte[] pBuff,
            int nOffSet,
            int nLen,
            int nPoint
    ) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffSet + nIdx]);

        int MAX_LEN = 8;
        int nSub;

        if (nPoint < MAX_LEN) nSub = (int) nPoint;
        else nSub = (int) MAX_LEN;

        String strIntPart;
        String strDecimalPart;

        strIntPart = strTmpMeter.substring(0, MAX_LEN - nSub);
        if (nPoint > 0) {
            strDecimalPart = strTmpMeter.substring(MAX_LEN - nSub, MAX_LEN);
        } else {
            strDecimalPart = "0";
        }

        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);
        String strMeterVal = strIntPart + "." + strDecimalPart;
        return strMeterVal;
    }

    //*************************************
    //*********** 계량기 소숫점 자리 *******
    //*************************************

    /**
     * <pre>
     * Standard Meter value parser
     * </pre>
     */
    public static String ParserStandardMeterVal(
            byte[] pBuff,
            int nOffset,
            int nLen,
            int nPoint
    ) {
        String strTmpMeter = "";
        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        String strMeterVal = "";

        int MAX_LEN = 8;
        int nSub;

        if (nPoint < MAX_LEN) nSub = (int) nPoint;
        else nSub = (int) MAX_LEN;

        String strIntPart;
        String strDecimalPart;

        strIntPart = strTmpMeter.substring(0, MAX_LEN - nSub);
        if (nPoint > 0) {
            strDecimalPart = strTmpMeter.substring(MAX_LEN - nSub, MAX_LEN);
        } else {
            strDecimalPart = "0";
        }

        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        strMeterVal = strIntPart + "." + strDecimalPart;

        return strMeterVal;
    }

    public static String PulseMeterValPoint(String strPulseVal, int nPoint) {
        int MAX_LEN = 8;
        String strMeterVal = "0.0";
        String strDecimalPart;
        String strIntPart;
        bLog.i(
                "Meter",
                " PulseMeterValPoint ==>01 strPulseVal:" +
                        strPulseVal +
                        " nPoint:" +
                        nPoint
        );

        if (nPoint == 0) {
            strDecimalPart = "0";
            strIntPart = strPulseVal.substring(0, MAX_LEN);
        } else if (nPoint == 1) {
            strIntPart = strPulseVal.substring(0, MAX_LEN - 1);
            strDecimalPart = strPulseVal.substring(MAX_LEN - 1, MAX_LEN);
        } else if (nPoint == 2) {
            strIntPart = strPulseVal.substring(0, MAX_LEN - 2);
            strDecimalPart = strPulseVal.substring(MAX_LEN - 2, MAX_LEN);
        } else if (nPoint == 3) {
            strIntPart = strPulseVal.substring(0, MAX_LEN - 3);
            strDecimalPart = strPulseVal.substring(MAX_LEN - 3, MAX_LEN);
        } else {
            strIntPart = strPulseVal.substring(0, MAX_LEN - 3);
            strDecimalPart = strPulseVal.substring(MAX_LEN - 3, MAX_LEN);
        }
        bLog.i(
                "Meter",
                " PulseMeterValPoint ==>02 strIntPart:" +
                        strIntPart +
                        " strDecimalPart:" +
                        strDecimalPart
        );
        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        strMeterVal = strIntPart + "." + strDecimalPart;
        bLog.i(
                "Meter",
                " PulseMeterValPoint ==>03 strMeterVal:" +
                        strMeterVal +
                        " strIntPart:" +
                        strIntPart
        );
        return strMeterVal;
    }

    //펄스 계량기
    public static String ParserPulseMeterVal(
            byte[] pBuff,
            int nOffset,
            int nLen,
            int nMeterValuePoint
    ) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        return PulseMeterValPoint(strTmpMeter, nMeterValuePoint);
    }

    public static String ParserShinhanDigitalBigMeterVal(
            byte[] pBuff,
            int nOffset,
            int nLen
    ) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        String strIntPart = strTmpMeter.substring(0, 6);
        String strDecimalPart = strTmpMeter.substring(6, 8);
        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        String strMeterVal = strIntPart + "." + strDecimalPart;
        return strMeterVal;
    }

    public static String ParserPrimoMeterVal(
            byte[] pBuff,
            int nOffset,
            int nLen
    ) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        String strIntPart = strTmpMeter.substring(0, 8);
        String strDecimalPart = strTmpMeter.substring(8, 12);
        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        String strMeterVal = strIntPart + "." + strDecimalPart;
        return strMeterVal;
    }

    public static String ParserPrimoBackflowVal(
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        String strMeterBackflow = "0.0";

        byte[] byValue = new byte[4];

        byValue[3] = pBuff[nOffSet + 3];
        byValue[2] = pBuff[nOffSet + 2];
        byValue[1] = pBuff[nOffSet + 1];
        byValue[0] = pBuff[nOffSet + 0];

        byte byUpbuff = (byte) byValue[3];
        byte flagFract = (byte) ((byUpbuff >> 6) & 0x03);

        byValue[3] = (byte) (byUpbuff & 0x3F);

        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < 4; nIdx++)
            strTmpMeter +=
                    String.format("%02X", byValue[nIdx]);

        long nTmpBackflow = DevUtil.convertStringLongToLong(strTmpMeter);
        long lBackflow;

        if (flagFract == 0x03) {
            lBackflow = (long) nTmpBackflow * 1000;
        } else if (flagFract == 0x02) {
            lBackflow = (long) nTmpBackflow * 100;
        } else if (flagFract == 0x01) {
            lBackflow = (long) nTmpBackflow * 10;
        } else {
            lBackflow = (long) nTmpBackflow;
        }

        long nIntPart = lBackflow / 10000;
        long nDecimalPart = lBackflow % 10000;
        String strIntPart = "";

        if (1000 <= nDecimalPart && nDecimalPart < 10000) strIntPart =
                String.format("%2d", nDecimalPart);
        else if (
                100 <= nDecimalPart && nDecimalPart < 1000
        ) strIntPart = String.format("%02d", nDecimalPart);
        else if (
                10 <= nDecimalPart && nDecimalPart < 100
        ) strIntPart = String.format("%03d", nDecimalPart);
        else strIntPart =
                    String.format("%04d", nDecimalPart);

        strMeterBackflow = strIntPart + "." + String.format("%d", nDecimalPart);
        return strMeterBackflow;
    }

    //기본 Meter BCD Data
    public static String ParserMeterValBCD(
            byte[] pBuff,
            int nOffSet,
            int nLen,
            int nPoint
    ) {
        String strTmpMeter = "";
        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffSet + nIdx]);

        String strMeterVal = "";

        int MAX_LEN = 8;
        int nSub;

        if (nPoint < MAX_LEN) nSub = (int) nPoint;
        else nSub = (int) MAX_LEN;

        String strIntPart;
        String strDecimalPart;

        strIntPart = strTmpMeter.substring(0, MAX_LEN - nSub);
        if (nPoint > 0) {
            strDecimalPart = strTmpMeter.substring(MAX_LEN - nSub, MAX_LEN);
        } else {
            strDecimalPart = "0";
        }

        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        strMeterVal = strIntPart + "." + strDecimalPart;

        return strMeterVal;
    }

    //Meter Data Long 형식
    public static String ParserMeterValLong(
            byte[] pBuff,
            int nOffSet,
            int nLen,
            int nPoint
    ) {
        int i;
        int nTotal, nSub;
        long nDiv;
        String strMeterVal = "";
        long nMeterVal;

        nMeterVal =
                (int) (
                        (long) (pBuff[nOffSet + 3] << 24) +
                                (long) (pBuff[nOffSet + 2] << 16) +
                                (int) (pBuff[nOffSet + 1] << 8) +
                                (int) pBuff[nOffSet + 0]
                );

        nTotal = 8; //8자리
        if (nTotal > nPoint) nSub = (int) nPoint;
        else nSub = (int) nTotal;

        nDiv = 1;
        if (nSub > 0) {
            for (i = 0; i < nSub; i++) {
                nDiv *= 10;
            }
        }

        long nIntPart = nMeterVal / nDiv;
        long nDecimalPart = nMeterVal % nDiv;

        String strIntPart;
        String strDecimalPart;
        String strZeroPart = "";

        strIntPart = String.valueOf(nIntPart);
        if (nPoint > 0) {
            strDecimalPart = String.valueOf(nDecimalPart);
        } else {
            strDecimalPart = "0";
        }

        int nDecimalLen;
        nDecimalLen = strDecimalPart.length();

        for (i = nDecimalLen; i < nSub; i++) {
            strZeroPart += "0";
        }

        strMeterVal = strIntPart + "." + strZeroPart + strDecimalPart;

        return strMeterVal;
    }

    //Meter Data Integer16 형식
    public static String ParserMeterValInt16(
            byte[] pBuff,
            int nOffSet,
            int nLen,
            int nPoint
    ) {
        int i;
        int nTotal, nSub;
        int nDiv;
        String strMeterVal = "";
        int nMeterVal;

        nMeterVal =
                (int) ((int) (pBuff[nOffSet + 1] << 8) + (int) pBuff[nOffSet + 0]);

        nTotal = 8; //8자리
        if (nTotal > nPoint) nSub = (int) nPoint;
        else nSub = (int) nTotal;

        nDiv = 1;
        if (nSub > 0) {
            for (i = 0; i < nSub; i++) {
                nDiv *= 10;
            }
        }

        int nIntPart = nMeterVal / nDiv;
        int nDecimalPart = nMeterVal % nDiv;

        String strIntPart;
        String strDecimalPart;
        String strZeroPart = "";

        strIntPart = String.valueOf(nIntPart);
        if (nPoint > 0) {
            strDecimalPart = String.valueOf(nDecimalPart);
        } else {
            strDecimalPart = "0";
        }

        int nDecimalLen;
        nDecimalLen = strDecimalPart.length();

        for (i = nDecimalLen; i < nSub; i++) {
            strZeroPart += "0";
        }

        strMeterVal = strIntPart + "." + strZeroPart + strDecimalPart;

        return strMeterVal;
    }

    public static int ParserHitecMeterCaliber(String strSerial) {
        String strCaliber = "";
        int nCaliber = 0;

        if (strSerial.length() > 3) {
            //3번째 자리로 구경 구분
            //예) 8자리일련번호 : 11212345  20mm
            //1=15, 2=20, 3=25, 4=32, 5=40, 6->50
            strCaliber = strSerial.substring(2, 3);
            nCaliber = DevUtil.convertStringIntegerToInteger(strCaliber);
        }

        return nCaliber;
    }

    public static String ParserHitecMeterCaliberString(int code) {
        switch (code) {
            case 1:
                return "15"; //mm
            case 2:
                return "20";
            case 3:
                return "25";
            case 4:
                return "32";
            case 5:
                return "40";
            case 6:
                return "50";
        }
        return "";
    }

    public static String ParserStandardMeterCaliberString(int code) {
        switch (code) {
            case 1:
                return "15"; //mm
            case 2:
                return "20";
            case 3:
                return "25";
            case 4:
                return "32";
            case 5:
                return "40";
            case 6:
                return "50";
            case 7:
                return "80";
            case 8:
                return "100";
            case 9:
                return "150";
            case 10:
                return "200";
            case 11:
                return "250";
            case 12:
                return "300";
        }
        return "15";
    }

    public static int ParserShinhanMeterCaliber(String strSerial) {
        String strCaliber = "";
        int nCaliber = 0;

        if (strSerial.length() > 3) {
            //2번째 자리로 구경 구분
            //예) 8자리일련번호 : 92012345  20mm
            strCaliber = strSerial.substring(1, 2);
            nCaliber = DevUtil.convertStringIntegerToInteger(strCaliber);
        }

        return nCaliber;
    }

    public static String ParserShinhanMeterCaliberString(int code) {
        switch (code) {
            case 1:
                return "15"; //mm
            case 2:
                return "20";
            case 3:
                return "25";
            case 4:
                return "32";
            case 5:
                return "40";
            case 6:
                return "50";
        }
        return "";
    }

    public static String ParserShinhanBigMeterCaliberString(int code) {
        switch (code) {
            case 1:
                return "80";
            case 2:
                return "100";
            case 3:
                return "150";
            case 4:
                return "200";
            case 5:
                return "250";
            case 6:
                return "300";
        }
        return "";
    }

    public static String ParserMeterDefaultUseBcdVal(
            byte[] pBuff,
            int nOffset,
            int nLen,
            int nPoint
    ) {
        String strMeterUseVal;
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        int nTotal, nSub;
        nTotal = 6; //6자리
        if (nTotal > nPoint) nSub = (int) nPoint;
        else nSub = (int) nTotal;

        String strDecimalPart = strTmpMeter.substring(nTotal - nSub, nTotal);
        String strIntPart = strTmpMeter.substring(0, nTotal - nSub);

        if (strDecimalPart.length() == 0) {
            strDecimalPart = "0";
        }

        strMeterUseVal = strIntPart + "." + strDecimalPart;
        return strMeterUseVal;
    }

    //*********************************************
    //*********** 펄스 계량기 추가 사용량 계산 ****
    //*********************************************
    //펄스 계량기
    public static String ParserPulseMeterUseVal(
            byte[] pBuff,
            int nOffset,
            int nLen,
            int nMeterValuePoint
    ) {
        String strTmpMeter = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        return PulseMeterUseValPoint(strTmpMeter, nMeterValuePoint);
    }

    //펄스 계량기 추가 사용량 계산
    public static String PulseMeterUseValPoint(String strUseVal, int nPoint) {
        int MAX_LEN = 6;
        String strMeterVal = "0.0";
        String strDecimalPart;
        String strIntPart;

        if (nPoint == 0) {
            strDecimalPart = "0";
            strIntPart = strUseVal.substring(0, MAX_LEN);
        } else if (nPoint == 1) {
            strDecimalPart = strUseVal.substring(MAX_LEN - 1, MAX_LEN);
            strIntPart = strUseVal.substring(0, MAX_LEN - 1);
        } else if (nPoint == 2) {
            strDecimalPart = strUseVal.substring(MAX_LEN - 2, MAX_LEN);
            strIntPart = strUseVal.substring(0, MAX_LEN - 2);
        } else if (nPoint == 3) {
            strDecimalPart = strUseVal.substring(MAX_LEN - 3, MAX_LEN);
            strIntPart = strUseVal.substring(0, MAX_LEN - 3);
        } else {
            strDecimalPart = strUseVal.substring(MAX_LEN - 3, MAX_LEN);
            strIntPart = strUseVal.substring(0, MAX_LEN - 3);
        }

        strMeterVal = strIntPart + "." + strDecimalPart;
        return strMeterVal;
    }

    //*************************************
    //*********** 계량기 종류 재설정 *******
    //*************************************
    public static int GetMeterType(int nMeterType) {
        int nMtType;
        if (nMeterType == (int) g_emMeterWaterType.eHitecShDigital) {
            //하이텍 계량기에 신한프로토콜이 적용된 경우
            nMtType = (int) g_emMeterWaterType.eShinhanDigital;
        } else {
            nMtType = nMeterType;
        }
        return nMtType;
    }

    /*
     *  소숫점 제거
     */
    public static String ConvMeterValOnlyNum(String strMeterVal) {
        int i;
        String strTempVal = "";
        String strConvMeterVal = "";
        for (i = 0; i < strMeterVal.length(); i++) {
            strTempVal = strMeterVal.substring(i, i + 1);
            if (!strTempVal.equals(".")) {
                strConvMeterVal += strTempVal;
            }
        }

        return strConvMeterVal;
    }

    //Meter add value
    public static String ParserMeterAddVal(
            String strMeterVal,
            String strMeterUseVal,
            int nMeterValuePoint
    ) {
        int i;
        long lnMeterVal = 0, lnDiffVal = 0;
        long lnAddVal = 0;
        String strMeterAddVal = "";
        String strIntPart = "";
        String strDecimalPart = "";
        String strAddVal = "";
        String strZero = "";
        String strConvMeterVal = "";
        String strConvMeterUseVal = "";
        long nDevid = 1;

        int nTotal, nSubPoint;
        nTotal = 6; //6자리
        if (nTotal > nMeterValuePoint) nSubPoint =
                (int) nMeterValuePoint;
        else nSubPoint = (int) nTotal;

        for (i = 0; i < nSubPoint; i++) {
            nDevid *= 10;
        }

        //소숫점 제거
        strConvMeterVal = ConvMeterValOnlyNum(strMeterVal);
        strConvMeterUseVal = ConvMeterValOnlyNum(strMeterUseVal);

        try {
            lnMeterVal = Long.parseLong(strConvMeterVal);
            lnDiffVal = Long.parseLong(strConvMeterUseVal);
        } catch (Exception e) {
        }

        lnAddVal = lnMeterVal + lnDiffVal;
        strAddVal = String.valueOf(lnAddVal);

        bLog.i(
                "meter",
                "ParserMeterAddVal ==> " +
                        " strMeterVal:" +
                        strMeterVal +
                        " strMeterUseVal:" +
                        strMeterUseVal
        );
        bLog.i(
                "meter",
                "ParserMeterAddVal ==> " +
                        " lnMeterVal:" +
                        lnMeterVal +
                        " lnDiffVal:" +
                        lnDiffVal
        );
        bLog.i(
                "meter",
                "ParserMeterAddVal ==> " +
                        " nSubPoint:" +
                        nSubPoint +
                        " nMeterValuePoint:" +
                        nMeterValuePoint
        );
        bLog.i(
                "meter",
                "ParserMeterAddVal ==> " +
                        " lnAddVal:" +
                        lnAddVal +
                        " strAddVal:" +
                        strAddVal
        );

        if (strAddVal.length() > nSubPoint) {
            strIntPart = strAddVal.substring(0, strAddVal.length() - nSubPoint);
            strDecimalPart =
                    strAddVal.substring(strAddVal.length() - nSubPoint, strAddVal.length());
        } else {
            strIntPart = "0";
            for (i = strAddVal.length(); i < nSubPoint; i++) {
                strZero += "0";
            }

            strDecimalPart = strZero + strAddVal;
        }

        if (strDecimalPart.length() == 0) {
            strDecimalPart = "0";
        }

        strMeterAddVal = strIntPart + "." + strDecimalPart;
        //bLog.i("meter", "ParserMeterAddVal ==> " + " strIntPart:" + strIntPart + " strDecimalPart:" + strDecimalPart);
        //bLog.i("meter", "ParserMeterAddVal ==> " + " strMeterAddVal:" + strMeterAddVal);

        return strMeterAddVal;
    }

    //Meter add value
    public static String ParserMeterAddValInt(
            String strMeterVal,
            int nMeterUseVal,
            int nMeterValuePoint
    ) {
        int i;
        long lnAddVal = 0;
        long lnMeterVal = 0;
        String strMeterAddVal = "";
        String strIntPart = "";
        String strDecimalPart = "";
        String strAddVal = "";
        String strZero = "";
        String strConvMeterVal = "";

        long nDevid = 1;

        int nTotal, nSubPoint;
        nTotal = 6; //6자리
        if (nTotal > nMeterValuePoint) nSubPoint =
                (int) nMeterValuePoint;
        else nSubPoint = (int) nTotal;

        for (i = 0; i < nSubPoint; i++) {
            nDevid *= 10;
        }

        //소숫점 제거
        strConvMeterVal = ConvMeterValOnlyNum(strMeterVal);

        try {
            lnMeterVal = Long.parseLong(strConvMeterVal);
        } catch (Exception e) {
        }

        lnAddVal = lnMeterVal + (long) nMeterUseVal;
        strAddVal = String.valueOf(lnAddVal);

        if (strAddVal.length() > nSubPoint) {
            strIntPart = strAddVal.substring(0, strAddVal.length() - nSubPoint);
            strDecimalPart =
                    strAddVal.substring(strAddVal.length() - nSubPoint, strAddVal.length());
        } else {
            strIntPart = "0";
            for (i = strAddVal.length(); i < nSubPoint; i++) {
                strZero += "0";
            }

            strDecimalPart = strZero + strAddVal;
        }

        if (strDecimalPart.length() == 0) {
            strDecimalPart = "0";
        }

        strMeterAddVal = strIntPart + "." + strDecimalPart;
        return strMeterAddVal;
    }

    //기간검침 추가 사용량이 시간당 1000 이상인 경우 오류 처리
    public static boolean CheckMeterAddValue(int nValue, int nHour) {
        boolean fResult = true;

        if (nValue > 1000 * nHour) {
            fResult = false;
        }

        return fResult;
    }

    //*********************************************
    //*********** 기간검침 추가 사용량 계산 ****
    //*********************************************

    /**
     * <pre>
     * Meter Serial BCD형태에서 변환
     * </pre>
     */
    public static String ParserMeterSerialBCD(
            byte[] pBuff,
            int nOffset,
            int nLen
    ) {
        String strMeterSerial = "";
        String strBuff = "";
        for (int nIdx = 0; nIdx < nLen; nIdx++) {
            strBuff = String.format("%02X", pBuff[nOffset + nIdx]);

            strMeterSerial += strBuff;
        }

        return strMeterSerial;
    }

    /**
     * <pre>
     * Meter Serial ASCII형태에서 변환
     * </pre>
     */
    public static String ParserMeterSerialASCII(
            String readMessage,
            int nOffset,
            int nLen
    ) {
        String strMeterSerial = "";
        String strBuff = "";
        String cBuff;

        for (int nIdx = 0; nIdx < nLen; nIdx += 2) {
            strBuff = readMessage.substring(nOffset + nIdx, nOffset + nIdx + 2);
            cBuff = DevUtil.convertStrHexToStrInteger(strBuff);

            strMeterSerial += cBuff;
        }

        return strMeterSerial;
    }

    public static String ParserMeterSerialHEX(
            int[] pBuff,
            int nOffSet,
            int nLen
    ) {
        String strMeterSerial = "";

        for (int nIdx = 0; nIdx < nLen; nIdx++) {
            strMeterSerial += (int) (pBuff[nOffSet + nIdx]);
        }
        return strMeterSerial;
    }

    //계량기 일련번호 변환 12345678 => 12-345678
    public static String ConvMeterSerialDefault(String srcSn) {
        String dstSn = srcSn;
        if (srcSn.length() == 8) {
            dstSn = srcSn.substring(0, 2) + "-" + srcSn.substring(2, 8);
        }

        return dstSn;
    }

    public static String ConvMeterSerialShinhan(String srcSn) {
        String dstSn = srcSn;

        if (srcSn.length() == 8) {
            Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR) - 2000;
            int nInt = year / 10;
            int nDec = year % 10;

            String strYear = srcSn.substring(0, 1);
            int nYear = DevUtil.convertStringIntegerToInteger(strYear);

            if (nYear <= nDec) {
                strYear = DevUtil.convertIntegerToStrInteger(nInt) + strYear;
            } else {
                nInt = nInt - 1;
                strYear = DevUtil.convertIntegerToStrInteger(nInt) + strYear;
            }
            dstSn = strYear + "-" + srcSn.substring(2, 8);
        }

        return dstSn;
    }

    //계량기 누수플래그 확인
    public static boolean GetMeterLeakStatus(int nMeterType, int nMeterStatus) {
        boolean fLeakStauts = false;

        if (nMeterType == (int) g_emMeterWaterType.eStandardDigital) {
            if ((nMeterStatus & 0x20) == 0x20) fLeakStauts = true;
        } else if (nMeterType == (int) g_emMeterWaterType.eHitecDigital) {
            if ((nMeterStatus & 0x02) == 0x02) fLeakStauts = true;
        } else if (nMeterType == (int) g_emMeterWaterType.eShinhanDigital) {
            if ((nMeterStatus & 0x08) == 0x08) fLeakStauts = true;
        } else if (nMeterType == (int) g_emMeterWaterType.eShinhanDigitalBig) {
            if ((nMeterStatus & 0x08) == 0x08) fLeakStauts = true;
        }

        return fLeakStauts;
    }

    /*
     * Decimal data check
     */
    public static boolean CheckDecValid(byte[] pBuff, int nOffSet, int nLen) {
        int i;
        byte srcBuff;
        byte byHigh, byLow;

        for (i = 0; i < nLen; i++) {
            srcBuff = (byte) pBuff[nOffSet + i];
            byHigh = (byte) ((srcBuff & 0xf0) >> 4);
            byLow = (byte) (srcBuff & 0x0f);

            if (byHigh > 9 || byLow > 9) return false;
        }

        return true;
    }

    //Calori Temperature
    public static String ParserMeterCaloriTemperatureBCD(
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        String strTemperature = "";
        String strLow = "";
        String strHigh = "";

        strHigh +=
                DevUtil.ConvByteToBCDString((byte) ((pBuff[nOffSet + 0] >> 4) & 0x0f));
        strHigh +=
                DevUtil.ConvByteToBCDString((byte) ((pBuff[nOffSet + 0]) & 0x0f));

        strLow +=
                DevUtil.ConvByteToBCDString((byte) ((pBuff[nOffSet + 1] >> 4) & 0x0f));
        strLow += DevUtil.ConvByteToBCDString((byte) ((pBuff[nOffSet + 1]) & 0x0f));

        strTemperature = strHigh + "." + strLow;
        return strTemperature;
    }

    //*************************************
    //*********** 계량기 일련번호 *******
    //*************************************

    //*************************************
    //*********** Parser WMU Meter data *******
    //*************************************
    //WMU Meter Data
    public static String ParserWmuMeterVal(byte[] pBuff, int nOffSet, int nLen) {
        int i;
        int nPoint;
        int nTotal, nSub;
        int nDiv;
        String strMeterVal = "0.0";
        int nMeterVal;

        nMeterVal =
                (int) (
                        (int) ((pBuff[nOffSet] & 0x0f) << 24) +
                                (int) ((pBuff[nOffSet + 1] & 0xff) << 16) +
                                (int) ((pBuff[nOffSet + 2] & 0xff) << 8) +
                                (int) (pBuff[nOffSet + 3] & 0xff)
                );

        nPoint = (int) (((pBuff[nOffSet] & 0xff) >> 4) & 0x0f);
        nTotal = 8; //8자리
        if (nTotal > nPoint) nSub = (int) nPoint;
        else nSub = (int) nTotal;

        nDiv = 1;
        if (nSub > 0) {
            for (i = 0; i < nSub; i++) {
                nDiv *= 10;
            }
        }
        //bLog.i("meter", "ParserWmuMeterVal ==> " + " nMeterVal:" + nMeterVal + " nPoint:" + nPoint);
        //bLog.i("meter", "ParserWmuMeterVal ==> " + " nDiv:" + nDiv);

        int nIntPart = nMeterVal / nDiv;
        int nDecimalPart = nMeterVal % nDiv;

        String strIntPart = String.format("%d", nIntPart);
        String strDecimalPart = String.format("%d", nDecimalPart);
        String strZeroPart = "";
        int nDecimalLen;
        //bLog.i("meter", "ParserWmuMeterVal ==> " + " strIntPart:" + strIntPart + " strDecimalPart:" + strDecimalPart);

        nDecimalLen = strDecimalPart.length();

        for (i = nDecimalLen; i < nSub; i++) {
            strZeroPart += "0";
        }

        strMeterVal =
                String.format("%s.%s%s", strIntPart, strZeroPart, strDecimalPart);
        //bLog.i("meter", "ParserWmuMeterVal ==> " + " strMeterVal:" + strMeterVal);
        return strMeterVal;
    }

    /**
     * <pre>
     * Smart Meter value parser
     * </pre>
     */
    public static String ParserSmartMeterVal(
            byte[] pBuff,
            int nOffset,
            int nLen,
            int nPoint
    ) {
        String strTmpMeter = "";
        for (int nIdx = 0; nIdx < nLen; nIdx++)
            strTmpMeter +=
                    String.format("%02X", pBuff[nOffset + nIdx]);

        String strMeterVal = "";

        int MAX_LEN = 12;
        int nSub;

        if (nPoint < MAX_LEN) nSub = (int) nPoint;
        else nSub = (int) MAX_LEN;

        String strIntPart;
        String strDecimalPart;

        strIntPart = strTmpMeter.substring(0, MAX_LEN - nSub);
        if (nPoint > 0) {
            strDecimalPart = strTmpMeter.substring(MAX_LEN - nSub, MAX_LEN);
        } else {
            strDecimalPart = "0";
        }

        strIntPart = DevUtil.convertStrIntegerToStrInteger(strIntPart);

        strMeterVal = strIntPart + "." + strDecimalPart;

        return strMeterVal;
    }

    public interface g_emMeterUtility {
        int eUnknown = 0;
        int eWater = 1;
        int eGas = 2;
        int eCalori = 3;
        int eHotwater = 4;
        int eElectronic = 5;
    }

    public interface g_emMeterPort {
        int eUnknown = 0;
        int eUart1 = 0x01;
        int eUart2 = 0x02;
        int ePulse1 = 0x11;
        int ePulse2 = 0x12;
        int eRS232 = 0x21;
        int eRS485 = 0x31;
        int eDPLC = 0x41;
    }

    //수도 계량기
    public interface g_emMeterWaterType {
        int eUnknown = 0;
        int eStandardDigital = 0x01;
        int eHitecDigital = 0x10;
        int eHitecShDigital = 0x11;
        int eShinhanDigital = 0x20;
        int eShinhanDigitalBig = 0x22;
        int eMnSDigital = 0x30;
        int ePrimo = 0x40;
        int eBadger = 0x42;
        int eModbusYk = 0x43;
        int eOneTLDigital = 0x50;
        int eIcmDplc = 0xB2;
        int ePulse_1000L = 0x71;
        int ePulse_500L = 0x72;
        int ePulse_100L = 0x73;
        int ePulse_50L = 0x74;
        int ePulse_10L = 0x75;
        int ePulse_5L = 0x76;
        int ePulse_1L = 0x77;
        int ePulse_05L = 0x78;
    }

    //가스 계량기
    public interface g_emMeterGasType {
        int eUnknown = 0x0;
        int eStandardDigital = 0x02;
        int eOneTLDigital = 0x51;
        int eIcmDplc = 0xB4;
        int ePulse_1000L = 0xA1;
        int ePulse_500L = 0xA2;
        int ePulse_100L = 0xA3;
        int ePulse_50L = 0xA4;
        int ePulse_10L = 0xA5;
        int ePulse_5L = 0xA6;
        int ePulse_1L = 0xA7;
        int ePulse_05L = 0xA8;
    }

    //열량계량기
    public interface g_emMeterCaloriType {
        int eUnknown = 0x0;
        int eStandardDigital = 0x03;
        int eOneTLDigital = 0x52;
        int eIcmDplc = 0xB5;
        int ePulse_1000L = 0x91;
        int ePulse_500L = 0x92;
        int ePulse_100L = 0x93;
        int ePulse_50L = 0x94;
        int ePulse_10L = 0x95;
        int ePulse_5L = 0x96;
        int ePulse_1L = 0x97;
        int ePulse_05L = 0x98;
    }

    //*************************************
    //*********** 열량계 온도   *******
    //*************************************

    //온수계량기
    public interface g_emMeterHotwaterType {
        int eUnknown = 0x0;
        int eStandardDigital = 0x04;
        int eOneTLDigital = 0x53;
        int eIcmDplc = 0xB3;
        int ePulse_1000L = 0x81;
        int ePulse_500L = 0x82;
        int ePulse_100L = 0x83;
        int ePulse_50L = 0x84;
        int ePulse_10L = 0x85;
        int ePulse_5L = 0x86;
        int ePulse_1L = 0x87;
        int ePulse_05L = 0x88;
    }

    //전기 계량기
    public interface g_emMeterElectronicType {
        int eUnknown = 0x0;
        int eStandardDigital = 0x05;
        int ePulse_1000W = 0x61;
        int ePulse_100W = 0x62;
        int ePulse_10W = 0x63;
        int ePulse_8W = 0x64;
        int ePulse_4W = 0x65;
        int ePulse_2W = 0x66;
        int ePulse_1W = 0x67;
        int ePulse_04W = 0x68;
        int ePulse_02W = 0x69;
        int ePulse_01W = 0x6A;
    }

    //EventType
    public interface g_emEventType {
        int eUnknown = 0x0;
        int eJoin = 0x01;
        int eFlood = 0x02;
        int eTemper = 0x03;
        int eMeterDown = 0x04;
        int eSubError = 0x05;
    }
}
