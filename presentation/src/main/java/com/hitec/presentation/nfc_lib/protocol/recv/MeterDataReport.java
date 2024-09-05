/**
 * <pre>
 * MeterReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import com.hitec.presentation.nfc_lib.util.DevUtil;
import com.hitec.presentation.nfc_lib.util.Meter;
import com.hitec.presentation.nfc_lib.util.bLog;
import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class MeterDataReport extends NfcRxMessage {

    private int m_nMeterState = 0;
    private int m_nMeterCount = 0;
    private int[] m_nUtility = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //종류
    private int[] m_nMeterType = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기 코드
    private int[] m_nMeterPort = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기 Port
    private int[] m_nMeterDataFlag = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기상태
    private int[] m_nMeterValid = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기 데이타 유효성 정보
    private int m_nSubTermValid = 0;
    private String[] m_strMeterSn = new String[NfcConstant.MAX_METER_PER_TERMINAL]; //일련번호
    private int[] m_nMeterValuePoint = new int[NfcConstant.MAX_METER_PER_TERMINAL];
    private int[] m_nMeterCaliberCd = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //구경코드
    private String[] m_strMeterCaliber = new String[NfcConstant.MAX_METER_PER_TERMINAL]; //구경정보
    private String[] m_strMeterVal = new String[NfcConstant.MAX_METER_PER_TERMINAL]; //검침값
    private String[] m_strCaloriFlowVal = new String[NfcConstant.MAX_METER_PER_TERMINAL]; //검침값
    private int[] m_nMeterBatt = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기 배터리
    private int[] m_nMeterStatus = new int[NfcConstant.MAX_METER_PER_TERMINAL]; //계량기 상태 플래그
    private boolean[] m_fMeterLeak = new boolean[NfcConstant.MAX_METER_PER_TERMINAL]; //옥내누수
    private String m_strTermBattery = "";

    public int GetMeterState() {
        return m_nMeterState;
    }

    public int GetNumOfMeter() {
        return m_nMeterCount;
    }

    public int GetMeterUtility(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_nUtility[nIdx];
    }

    public int GetMeterType(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_nMeterType[nIdx];
    }

    public int GetMeterPort(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_nMeterPort[nIdx];
    }

    public boolean GetMeterValid(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return false;
        }

        if (m_nMeterDataFlag[nIdx] == 0x01) {
            return false;
        }

        if (m_nMeterValid[nIdx] == NfcConstant.METER_VALID_OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean GetSubTermValid() {
        if (m_nSubTermValid == NfcConstant.METER_VALID_SUB_ERROR) {
            return false;
        }
        return true;
    }

    public String GetMeterSerial(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return "";
        }
        return m_strMeterSn[nIdx];
    }

    public String GetMeterDigits(int nIdx) {
        if (m_nMeterValid[nIdx] == NfcConstant.METER_VALID_OK) {
            if (m_nMeterValuePoint[nIdx] == 1) {
                return "0.1";
            } else if (m_nMeterValuePoint[nIdx] == 2) {
                return "0.01";
            } else if (m_nMeterValuePoint[nIdx] == 3) {
                return "0.001";
            } else if (m_nMeterValuePoint[nIdx] == 4) {
                return "0.0001";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public int GetMeterCaliberCd(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_nMeterCaliberCd[nIdx];
    }

    public String GetMeterCaliber(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return "";
        }
        return m_strMeterCaliber[nIdx];
    }

    public String GetMeterValue(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return "";
        }
        return m_strMeterVal[nIdx];
    }

    public String GetCaloriFlowValue(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return "";
        }
        return m_strCaloriFlowVal[nIdx];
    }

    public int GetMeterBattery(int nIdx) {
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_nMeterBatt[nIdx];
    }

    public String GetMeterStatus(int nIdx) {
        int nStatus;
        if (nIdx >= NfcConstant.MAX_METER_PER_TERMINAL) {
            nStatus = 0;
        }
        nStatus = m_nMeterStatus[nIdx];
        return DevUtil.convertIntToHexLPad(nStatus, 2, "0");
    }

    public String GetTermBattery() {
        return m_strTermBattery;
    }

    public String GetMeteredTime() {
        return getMessageTime();
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_strTermBattery = parseBattery(m_nOffset++);

        nYear = parseYear(m_nOffset);
        m_nOffset += 2;
        nMon = getHexData(m_nOffset++);
        nDay = getHexData(m_nOffset++);
        nHour = getHexData(m_nOffset++);
        nMin = getHexData(m_nOffset++);
        nSec = getHexData(m_nOffset++);

        m_nMeterState = getHexData(m_nOffset++);
        m_nMeterCount = getHexData(m_nOffset++);
        if (m_nMeterCount == 0 || m_nMeterCount > NfcConstant.MAX_METER_PER_TERMINAL) {
            return parseFailed("NumberOfMeter Invalid");
        }

        m_nSubTermValid = 0;
        int i;
        int nMeterLen;

        for (i = 0; i < m_nMeterCount; i++) {
            nMeterLen = registerMeter(hexData, m_nOffset, i);
            m_nOffset += nMeterLen;
        }

        return parseCompleted();
    }

    //기본 계량기 데이터
    private void parserMeterDefault(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterValuePoint
    ) {
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == true) {
            m_strMeterVal[nIdx] =
                    Meter.ParserMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        } else {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
        nOffSet += 4;

        int nBattConfig = getHexData(nOffSet++);
        m_nMeterStatus[nIdx] = getHexData(nOffSet++);
        m_nMeterBatt[nIdx] = nBattConfig & 0x0F;

        //계량기 오류인경우
        if (m_nMeterStatus[nIdx] == 0xFF) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
    }

    //펄스 계량기
    private void parserMeterPulse(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterValuePoint
    ) {
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == true) {
            m_strMeterVal[nIdx] =
                    Meter.ParserPulseMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        } else {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
        nOffSet += 4;

        int nBattConfig = getHexData(nOffSet++);
        m_nMeterStatus[nIdx] = getHexData(nOffSet++);
        m_nMeterBatt[nIdx] = nBattConfig & 0x0F;

        //계량기 오류인경우
        if (m_nMeterStatus[nIdx] == 0xFF) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
    }

    //표준 프로토콜
    private void parserMeterStandardDigital(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterValuePoint
    ) {
        int nValuePoint = 0;
        boolean fValueValid = true;
        byte[] pMeterVal = new byte[4];

        bLog.i(
                TAG,
                " parserMeterStandardDigital ==>01 nOffSet:" + nOffSet + " nIdx:" + nIdx
        );
        bLog.i(
                TAG,
                " parserMeterStandardDigital ==>01 nMeterValuePoint:" + nMeterValuePoint
        );

        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            fValueValid = false;
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
        }
        for (int i = 0; i < 4; i++) {
            pMeterVal[i] = (byte) (pBuff[nOffSet + i] & 0xFF);
        }
        nOffSet += 4;

        int nBattConfig = getHexData(nOffSet++);
        m_nMeterStatus[nIdx] = getHexData(nOffSet++);

        m_nMeterCaliberCd[nIdx] = (nBattConfig >> 4) & 0x0F;
        m_nMeterBatt[nIdx] = 1;
        nValuePoint = nBattConfig & 0x0F;
        m_nMeterValuePoint[nIdx] = nValuePoint;

        if (m_nMeterType[nIdx] == Meter.g_emMeterWaterType.eStandardDigital) {
            if ((m_nMeterStatus[nIdx] & 0x04) == 0x04) {
                m_nMeterBatt[nIdx] = 0; //저전압
            } else {
                m_nMeterBatt[nIdx] = 1; //정상
            }
        }

        if (fValueValid) {
            m_strMeterVal[nIdx] =
                    Meter.ParserStandardMeterVal(pMeterVal, 0, 4, nValuePoint);
        }
        bLog.i(
                TAG,
                " parserMeterStandardDigital ==>02 nOffSet:" + nOffSet + " nIdx:" + nIdx
        );
        bLog.i(
                TAG,
                " parserMeterStandardDigital ==>02 m_strMeterVal[nIdx]:" +
                        m_strMeterVal[nIdx] +
                        " m_nMeterCaliberCd[nIdx]:" +
                        m_nMeterCaliberCd[nIdx]
        );

        //계량기 오류인경우
        if (m_nMeterStatus[nIdx] == 0xFF) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
        bLog.i(
                TAG,
                " parserMeterStandardDigital ==>03 nOffSet:" + nOffSet + " nIdx:" + nIdx
        );
    }

    //원티엘 열량계 프로토콜
    private void parserMeterCaloriOneTLDigital(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterValuePoint
    ) {
        int nValuePoint = 0;
        boolean fValueValid = true;
        byte[] pMeterVal = new byte[4];
        byte[] pFlowVal = new byte[4];
        //Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            fValueValid = false;
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
        }

        for (int i = 0; i < 4; i++) {
            pMeterVal[i] = (byte) (pBuff[nOffSet + i] & 0xFF);
        }
        nOffSet += 4;

        for (int i = 0; i < 4; i++) {
            pFlowVal[i] = (byte) (pBuff[nOffSet + i] & 0xFF);
        }
        nOffSet += 4;

        int nBattConfig = getHexData(nOffSet++);
        m_nMeterStatus[nIdx] = getHexData(nOffSet++);
        m_nMeterBatt[nIdx] = (byte) (nBattConfig & 0x0F);

        if (fValueValid) {
            //열량값
            m_strMeterVal[nIdx] =
                    Meter.ParserStandardMeterVal(pMeterVal, 0, 4, nValuePoint); //소숫점2자리
        }
        if (fValueValid) {
            //열량값
            m_strCaloriFlowVal[nIdx] =
                    Meter.ParserStandardMeterVal(pFlowVal, 0, 4, nValuePoint); //소숫점2자리
        }

        //계량기 오류인경우
        if (m_nMeterStatus[nIdx] == 0xFF) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
            m_strCaloriFlowVal[nIdx] = "";
        }
    }

    //ShinhanDigitalBig 프로토콜
    private void parserMeterShinhanDigitalBig(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterValuePoint
    ) {
        int nBattConfig;
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == true) {
            m_strMeterVal[nIdx] =
                    Meter.ParserShinhanDigitalBigMeterVal(pBuff, nOffSet, 4);
        } else {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
        nOffSet += 4;

        nBattConfig = getHexData(nOffSet++);
        m_nMeterStatus[nIdx] = getHexData(nOffSet++);
        m_nMeterBatt[nIdx] = (byte) (nBattConfig & 0x0F);

        //계량기 오류인경우
        if (m_nMeterStatus[nIdx] == 0xFF) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_VALUE_ERROR;
            m_strMeterVal[nIdx] = "";
        }
    }

    private void InitMeterValueInfo(int nIdx) {
        m_nMeterValid[nIdx] = NfcConstant.METER_VALID_OK;
        m_nUtility[nIdx] = Meter.g_emMeterUtility.eUnknown;
        m_nMeterType[nIdx] = 0;
        m_nMeterPort[nIdx] = 0;
        m_nMeterDataFlag[nIdx] = 0;
        m_strMeterSn[nIdx] = "";
        m_nMeterValuePoint[nIdx] = -1;
        m_strMeterVal[nIdx] = "";
        m_nMeterCaliberCd[nIdx] = 0;
        m_strMeterCaliber[nIdx] = "";
        m_nMeterBatt[nIdx] = 0;
        m_nMeterStatus[nIdx] = 0;
        m_fMeterLeak[nIdx] = false;
    }

    //pasre data
    private int registerMeter(byte[] pBuff, int nOffSet, int nIdx) {
        int nStart = nOffSet;
        int nUtility;
        int nMeterType;
        int nMeterValuePoint;

        //데이터 초기화
        InitMeterValueInfo(nIdx);

        nMeterType = getHexData(nOffSet++);

        nUtility = Meter.GetMeterUtility(nMeterType);
        nMeterValuePoint = Meter.GetMeterValuePoint(nUtility, nMeterType);
        m_nMeterValuePoint[nIdx] = nMeterValuePoint;

        m_nUtility[nIdx] = nUtility;
        m_nMeterType[nIdx] = nMeterType;
        m_nMeterPort[nIdx] = getHexData(nOffSet++);

        m_nMeterDataFlag[nIdx] = getHexData(nOffSet++);
        bLog.i(TAG, " registerMeter ==>01 nOffSet:" + nOffSet + " nIdx:" + nIdx);
        bLog.i(
                TAG,
                " registerMeter ==>01 nUtility:" +
                        nUtility +
                        " nMeterType:" +
                        nMeterType +
                        " nMeterValuePoint:" +
                        nMeterValuePoint
        );
        bLog.i(
                TAG,
                " registerMeter ==>01 m_nMeterDataFlag[nIdx]:" + m_nMeterDataFlag[nIdx]
        );

        if (m_nMeterDataFlag[nIdx] == 0x01) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_ERROR;
            nOffSet += 10; //이후 데이터는 0x00이므로 사용하지 않음
            if (
                    nMeterType == Meter.g_emMeterCaloriType.eOneTLDigital ||
                            nMeterType == Meter.g_emMeterCaloriType.eIcmDplc
            ) {
                nOffSet += 4;
            }
            return nOffSet - nStart;
        } else if (m_nMeterDataFlag[nIdx] == 0x02) {
            m_nMeterValid[nIdx] = NfcConstant.METER_VALID_SUB_ERROR;
            m_nSubTermValid = NfcConstant.METER_VALID_SUB_ERROR;
            nOffSet += 10; //이후 데이터는 0x00이므로 사용하지 않음
            if (
                    nMeterType == Meter.g_emMeterCaloriType.eOneTLDigital ||
                            nMeterType == Meter.g_emMeterCaloriType.eIcmDplc
            ) {
                nOffSet += 4;
            }
            return nOffSet - nStart;
        }
        bLog.i(TAG, " registerMeter ==>02 nOffSet:" + nOffSet + " nIdx:" + nIdx);

        //계량기 일련번호
        m_strMeterSn[nIdx] = Meter.ParserMeterSerialBCD(pBuff, nOffSet, 4);
        nOffSet += 4;
        bLog.i(TAG, " registerMeter ==>03 nOffSet:" + nOffSet + " nIdx:" + nIdx);
        bLog.i(
                TAG,
                " registerMeter ==>01 m_nMeterDataFlag[nIdx]:" +
                        m_nMeterDataFlag[nIdx] +
                        " m_strMeterSn[nIdx]:" +
                        m_strMeterSn[nIdx]
        );

        switch (nUtility) {
            case Meter.g_emMeterUtility.eWater:
                registerWaterMeter(pBuff, nOffSet, nIdx, nMeterType, nMeterValuePoint);
                break;
            case Meter.g_emMeterUtility.eGas:
                registerGasMeter(pBuff, nOffSet, nIdx, nMeterType, nMeterValuePoint);
                break;
            case Meter.g_emMeterUtility.eCalori:
                registerCaloriMeter(pBuff, nOffSet, nIdx, nMeterType, nMeterValuePoint);

                if (
                        nMeterType == Meter.g_emMeterCaloriType.eOneTLDigital ||
                                nMeterType == Meter.g_emMeterCaloriType.eIcmDplc
                ) {
                    nOffSet += 4;
                }
                break;
            case Meter.g_emMeterUtility.eHotwater:
                registerHotwaterMeter(
                        pBuff,
                        nOffSet,
                        nIdx,
                        nMeterType,
                        nMeterValuePoint
                );
                break;
            case Meter.g_emMeterUtility.eElectronic:
                registerElectronicMeter(
                        pBuff,
                        nOffSet,
                        nIdx,
                        nMeterType,
                        nMeterValuePoint
                );
                break;
            default:
                registerWaterMeter(pBuff, nOffSet, nIdx, nMeterType, nMeterValuePoint);
                break;
        }
        nOffSet += 6;

        return nOffSet - nStart;
    }

    //Water Meter
    private void registerWaterMeter(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterType,
            int nMeterValuePoint
    ) {
        bLog.i(
                TAG,
                " registerWaterMeter ==>01 nIdx:" + nIdx + " nMeterType:" + nMeterType
        );
        if (nMeterType == Meter.g_emMeterWaterType.eStandardDigital) {
            bLog.i(
                    TAG,
                    " registerWaterMeter ==>02 nOffSet:" +
                            nOffSet +
                            " nMeterValuePoint:" +
                            nMeterValuePoint
            );
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
            m_strMeterSn[nIdx] = Meter.ConvMeterSerialDefault(m_strMeterSn[nIdx]);
            m_strMeterCaliber[nIdx] =
                    Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd[nIdx]);
        } else if (nMeterType == Meter.g_emMeterWaterType.eHitecDigital) {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
            //구경먼저
            m_nMeterCaliberCd[nIdx] =
                    Meter.ParserHitecMeterCaliber(m_strMeterSn[nIdx]);
            m_strMeterCaliber[nIdx] =
                    Meter.ParserHitecMeterCaliberString(m_nMeterCaliberCd[nIdx]);
            m_strMeterSn[nIdx] = Meter.ConvMeterSerialDefault(m_strMeterSn[nIdx]);
        } else if (nMeterType == Meter.g_emMeterWaterType.eShinhanDigital) {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
            m_nMeterType[nIdx] = Meter.CheckHitecShMeterType(m_nMeterStatus[nIdx]);
            if (m_nMeterType[nIdx] == Meter.g_emMeterWaterType.eShinhanDigital) {
                //2019.09.02 번호변경없이 그대로 적용
                //구경 15mm고정
                m_nMeterCaliberCd[nIdx] = 1; //Meter.ParserShinhanMeterCaliber(m_strMeterSn[nIdx]);
                m_strMeterCaliber[nIdx] =
                        Meter.ParserShinhanMeterCaliberString(m_nMeterCaliberCd[nIdx]);
                //m_strMeterSn[nIdx] = Meter.ConvMeterSerialShinhan(m_strMeterSn[nIdx]);
            } else if (
                    m_nMeterType[nIdx] == Meter.g_emMeterWaterType.eHitecShDigital
            ) {
                //구경먼저
                m_nMeterCaliberCd[nIdx] =
                        Meter.ParserHitecMeterCaliber(m_strMeterSn[nIdx]);
                m_strMeterCaliber[nIdx] =
                        Meter.ParserHitecMeterCaliberString(m_nMeterCaliberCd[nIdx]);
                m_strMeterSn[nIdx] = Meter.ConvMeterSerialDefault(m_strMeterSn[nIdx]);
            }
        } else if (nMeterType == Meter.g_emMeterWaterType.eShinhanDigitalBig) {
            parserMeterShinhanDigitalBig(pBuff, nOffSet, nIdx, nMeterValuePoint);
            //구경먼저
            m_nMeterCaliberCd[nIdx] =
                    Meter.ParserShinhanMeterCaliber(m_strMeterSn[nIdx]);
            m_strMeterCaliber[nIdx] =
                    Meter.ParserShinhanBigMeterCaliberString(m_nMeterCaliberCd[nIdx]);
            m_strMeterSn[nIdx] = Meter.ConvMeterSerialShinhan(m_strMeterSn[nIdx]);
        } else if (nMeterType == Meter.g_emMeterWaterType.eMnSDigital) {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (nMeterType == Meter.g_emMeterWaterType.eBadger) {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (nMeterType == Meter.g_emMeterWaterType.eModbusYk) {
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
            m_strMeterSn[nIdx] = Meter.ConvMeterSerialDefault(m_strMeterSn[nIdx]);
            m_strMeterCaliber[nIdx] =
                    Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd[nIdx]);
        } else if (
                nMeterType >= Meter.g_emMeterWaterType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterWaterType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        }

        //누수 플래그
        m_fMeterLeak[nIdx] =
                Meter.GetMeterLeakStatus(nMeterType, m_nMeterStatus[nIdx]);
    }

    //Gas Meter
    private void registerGasMeter(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterGasType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterGasType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterGasType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        }
    }

    //Calori Meter
    private void registerCaloriMeter(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterCaloriType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (
                nMeterType == Meter.g_emMeterCaloriType.eOneTLDigital ||
                        nMeterType == Meter.g_emMeterCaloriType.eIcmDplc
        ) {
            parserMeterCaloriOneTLDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterCaloriType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterCaloriType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        }
    }

    //Hotwater Meter
    private void registerHotwaterMeter(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterHotwaterType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterHotwaterType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterHotwaterType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nIdx, nMeterValuePoint);
        }
    }

    //Electronic Meter
    private void registerElectronicMeter(
            byte[] pBuff,
            int nOffSet,
            int nIdx,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterElectronicType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nIdx, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterElectronicType.ePulse_1000W &&
                        nMeterType <= Meter.g_emMeterElectronicType.ePulse_01W
        ) {
            parserMeterPulse(pBuff, nOffSet, nIdx, nMeterValuePoint);
        }
    }
}
