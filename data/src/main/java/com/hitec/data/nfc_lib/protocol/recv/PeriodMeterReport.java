/**
 * <pre>
 * PeriodMeterReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

import android.util.Log;

import com.hitec.data.nfc_lib.util.Meter;

public class PeriodMeterReport extends NfcRxMessage {

    private static final int MAX_NUM_OF_DATA = 4;
    private static int SECOND_METER_VALUE_OK = 0; //2번째 검침값 정상
    private static int SECOND_METER_VALUE_ERROR = 1; //2번째 검침값 오류
    private static int SECOND_METER_VALUE_FIRST = 2; //2번째 검침값 1번째 검침 위치에 저장됨
    // Total Block
    private int m_nTotalBlock = 0;
    // Current Block
    private int m_nCurrentBlock = 0;
    private int m_nMeterCount = 0;
    private int[] m_stUtility = new int[MAX_METER_PER_TERMINAL]; //종류
    private int[] m_stMeterType = new int[MAX_METER_PER_TERMINAL]; //계량기 코드
    private int[] m_stMeterValuePoint = new int[MAX_METER_PER_TERMINAL]; //자릿수
    private int[] m_nMeterDataFlag = new int[MAX_METER_PER_TERMINAL]; //계량기상태
    private int[] m_nMeterValid = new int[MAX_METER_PER_TERMINAL]; //계량기 데이타 유효성 정보
    private int m_nMeterInterval = 0;
    private int m_nNumOfData = 0;
    private String[] m_strFirstMeteredTime = new String[MAX_NUM_OF_DATA];
    private String[] m_strSecondMeteredTime = new String[MAX_NUM_OF_DATA];
    // Term battery level
    private int[] m_nTermBattery = new int[MAX_NUM_OF_DATA];
    // Meter battery level
    private int[] m_nMeterBattery = new int[MAX_NUM_OF_DATA];
    //내부적으로 사용
    private int m_nMeterConnection = 0;
    private String m_strFirstMeterValue = "";
    private String m_strSecondMeterValue = "";
    private int[] m_stMeterConnection0 = new int[MAX_NUM_OF_DATA];
    private int[] m_stMeterConnection1 = new int[MAX_NUM_OF_DATA];
    private int[] m_stMeterConnection2 = new int[MAX_NUM_OF_DATA];
    private String[] m_stFirstMeterValue0 = new String[MAX_NUM_OF_DATA];
    private String[] m_stFirstMeterValue1 = new String[MAX_NUM_OF_DATA];
    private String[] m_stFirstMeterValue2 = new String[MAX_NUM_OF_DATA];
    private String[] m_stSecondMeterValue0 = new String[MAX_NUM_OF_DATA];
    private String[] m_stSecondMeterValue1 = new String[MAX_NUM_OF_DATA];
    private String[] m_stSecondMeterValue2 = new String[MAX_NUM_OF_DATA];
    private String m_strNWK = "";

    public int GetTotalBlock() {
        return m_nTotalBlock;
    }

    public int GetCurrentBlock() {
        return m_nCurrentBlock;
    }

    public int GetMeterCount() {
        return m_nMeterCount;
    }

    public int GetMeterUtility(int nIdx) {
        if (nIdx >= MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_stUtility[nIdx];
    }

    public int GetMeterType(int nIdx) {
        if (nIdx >= MAX_METER_PER_TERMINAL) {
            return 0;
        }
        return m_stMeterType[nIdx];
    }

    public int GetMeterValuePoint(int nIdx) {
        if (nIdx >= MAX_METER_PER_TERMINAL) {
            return 3;
        }
        return m_stMeterValuePoint[nIdx];
    }

    public boolean GetMeterValid(int nIdx) {
        if (nIdx >= MAX_METER_PER_TERMINAL) {
            return false;
        }

        if (m_nMeterDataFlag[nIdx] == 0x01) {
            return false;
        }

        if (m_nMeterValid[nIdx] == METER_VALID_OK) {
            return true;
        } else {
            return false;
        }
    }

    public int GetMeterInterval() {
        return m_nMeterInterval;
    }

    public int GetNumOfData() {
        return m_nNumOfData;
    }

    public String GetFirstMeteredTime(int n) {
        if (n >= MAX_NUM_OF_DATA) return "";
        return m_strFirstMeteredTime[n];
    }

    public String GetSecondMeteredTime(int n) {
        if (n >= MAX_NUM_OF_DATA) return "";
        return m_strSecondMeteredTime[n];
    }

    public String GetTermBattery(int n) {
        if (n >= MAX_NUM_OF_DATA) return "";
        return String.format(
                "%d.%d",
                m_nTermBattery[n] / 10,
                m_nTermBattery[n] % 10
        );
    }

    public int GetMeterBattery(int n) {
        if (n >= MAX_NUM_OF_DATA) return 0;
        return m_nMeterBattery[n];
    }

    public int GetMeterConnection(int nPos, int nData) {
        if (nData >= MAX_NUM_OF_DATA) return METER_VALID_VALUE_ERROR;
        if (nPos == 0) return m_stMeterConnection0[nData];
        else if (
                nPos == 1
        ) return m_stMeterConnection1[nData];
        else if (
                nPos == 2
        ) return m_stMeterConnection2[nData];
        else return METER_VALID_VALUE_ERROR;
    }

    public String GetFirstMeterValue(int nPos, int nData) {
        if (nData >= MAX_NUM_OF_DATA) return "";
        if (nPos == 0) return m_stFirstMeterValue0[nData];
        else if (
                nPos == 1
        ) return m_stFirstMeterValue1[nData];
        else if (
                nPos == 2
        ) return m_stFirstMeterValue2[nData];
        else return "";
    }

    public String GetSecondMeterValue(int nPos, int nData) {
        if (nData >= MAX_NUM_OF_DATA) return "";
        if (nPos == 0) return m_stSecondMeterValue0[nData];
        else if (
                nPos == 1
        ) return m_stSecondMeterValue1[nData];
        else if (
                nPos == 2
        ) return m_stSecondMeterValue2[nData];
        else return "";
    }

    private void initArrayMember() {
        for (int i = 0; i < MAX_NUM_OF_DATA; i++) {
            m_strFirstMeteredTime[i] = "";
            m_strSecondMeteredTime[i] = "";
            m_nTermBattery[i] = 0;
            m_nMeterBattery[i] = 0;

            m_stMeterConnection0[i] = METER_VALID_OK;
            m_stMeterConnection1[i] = METER_VALID_OK;
            m_stMeterConnection2[i] = METER_VALID_OK;

            m_stFirstMeterValue0[i] = "";
            m_stFirstMeterValue1[i] = "";
            m_stFirstMeterValue2[i] = "";

            m_stSecondMeterValue0[i] = "";
            m_stSecondMeterValue1[i] = "";
            m_stSecondMeterValue2[i] = "";
        }
    }

    public String GetNWK() {
        return m_strNWK;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        initArrayMember();

        m_nTotalBlock = getHexData(m_nOffset++);
        m_nCurrentBlock = getHexData(m_nOffset++);
        Log.e(
                "meter",
                "PdaPeriodMultiDataReport ==> " +
                        " m_nTotalBlock:" +
                        m_nTotalBlock +
                        " m_nCurrentBlock:" +
                        m_nCurrentBlock
        );

        if (m_nTotalBlock > 0 && m_nCurrentBlock > 0) {
            ParserMeterBlock();

            m_nMeterInterval = getHexData(m_nOffset++);
            m_nNumOfData = getHexData(m_nOffset++);
            if (m_nNumOfData == 0 || m_nNumOfData > MAX_NUM_OF_DATA) {
                return parseFailed("0 or too many data");
            }
            //Log.i("meter", "PdaPeriodMultiDataReport ==> " + " m_nNumOfData:" + m_nNumOfData );

            for (int i = 0; i < m_nNumOfData; i++) {
                m_strFirstMeteredTime[i] = parseCompressedTime(m_nOffset);
                m_nOffset += 3;
                m_strSecondMeteredTime[i] =
                        addHour(m_strFirstMeteredTime[i], m_nMeterInterval);

                for (int cnt = 0; cnt < m_nMeterCount; cnt++) {
                    ParserMeterValue(
                            hexData,
                            m_nOffset,
                            m_stUtility[cnt],
                            m_stMeterType[cnt],
                            m_stMeterValuePoint[cnt]
                    );
                    m_nOffset += 6;

                    if (cnt == 0) {
                        m_stMeterConnection0[i] = m_nMeterConnection;
                        m_stFirstMeterValue0[i] = m_strFirstMeterValue;
                        m_stSecondMeterValue0[i] = m_strSecondMeterValue;
                    } else if (cnt == 1) {
                        m_stMeterConnection1[i] = m_nMeterConnection;
                        m_stFirstMeterValue1[i] = m_strFirstMeterValue;
                        m_stSecondMeterValue1[i] = m_strSecondMeterValue;
                    } else if (cnt == 2) {
                        m_stMeterConnection2[i] = m_nMeterConnection;
                        m_stFirstMeterValue2[i] = m_strFirstMeterValue;
                        m_stSecondMeterValue2[i] = m_strSecondMeterValue;
                    }
                }

                registerMeterBattery(i, m_nOffset, 1);
                m_nOffset += 1;
                //Log.i("meter", "PdaPeriodMultiDataReport ==> " + " i:" + i );

            }
        }

        return parseCompleted();
    }

    //pasre data
    private void ParserMeterBlock() {
        int i;
        int nMeterType;
        int nVifDif;

        m_nMeterCount = getHexData(m_nOffset++);

        if (m_nMeterCount > 3) m_nMeterCount = 3;

        for (i = 0; i < m_nMeterCount; i++) {
            nMeterType = getHexData(m_nOffset++);
            m_stMeterType[i] = nMeterType;

            m_stUtility[i] = Meter.GetMeterUtility(nMeterType);
            m_stMeterValuePoint[i] =
                    Meter.GetMeterValuePoint(m_stUtility[i], nMeterType);

            if (
                    nMeterType == Meter.g_emMeterWaterType.eStandardDigital ||
                            nMeterType == Meter.g_emMeterGasType.eStandardDigital ||
                            nMeterType == Meter.g_emMeterCaloriType.eStandardDigital ||
                            nMeterType == Meter.g_emMeterHotwaterType.eStandardDigital ||
                            nMeterType == Meter.g_emMeterWaterType.eModbusYk
            ) {
                nVifDif = getHexData(m_nOffset++);
                //VIF, DIF 먼저 분석함
                m_stMeterValuePoint[i] = GetStandardMeterValuePoint(nVifDif);
            }
        }
    }

    private int GetStandardMeterValuePoint(int nVifDif) {
        int nValuePoint = 3;

        if (nVifDif != 0x00) {
            nValuePoint = nVifDif & 0x0F;
            if (nValuePoint > 7) nValuePoint = 7;
        }

        return nValuePoint;
    }

    private void registerMeterBattery(int nPos, int nOffSet, int nLen) {
        int nBuffer = getHexData(nOffSet++);
        m_nMeterBattery[nPos] = (nBuffer >> 6) & 0x03;
        m_nTermBattery[nPos] = nBuffer & 0x3F;
    }

    //계량기 MT_DOWN 확인
    private boolean checkMeterValid(byte[] pBuff, int nOffSet, int nLen) {
        boolean fResult = false;
        int temp;

        for (int i = 0; i < nLen; i++) {
            temp = getHexData(nOffSet++);
            if (temp != 0xff) {
                fResult = true;
                break;
            }
        }

        return fResult;
    }

    //계량기 MT_DOWN 확인
    private int checkSecondMeterValueValid(byte[] pBuff, int nOffSet) {
        int nResult = SECOND_METER_VALUE_OK;
        int tempHigh, tempLow;

        tempLow = getHexData(nOffSet++);
        tempHigh = getHexData(nOffSet++);

        if (tempHigh == 0x0ff && tempLow == 0xff) {
            nResult = SECOND_METER_VALUE_ERROR;
        } else if (tempHigh == 0x0ff && tempLow == 0xfe) {
            nResult = SECOND_METER_VALUE_FIRST;
        }

        return nResult;
    }

    private void ParserMeterValue(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        boolean fFirstValid = checkMeterValid(pBuff, nOffSet, 4);

        if (fFirstValid == false) {
            m_nMeterConnection = METER_VALID_ERROR;
            m_strFirstMeterValue = "";
            m_strSecondMeterValue = "";
            return;
        }

        if (nUtility == Meter.g_emMeterUtility.eWater) {
            registerWaterMeter(
                    pBuff,
                    nOffSet,
                    nUtility,
                    nMeterType,
                    nMeterValuePoint
            );
        } else if (nUtility == Meter.g_emMeterUtility.eGas) {
            registerGasMeter(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else if (nUtility == Meter.g_emMeterUtility.eCalori) {
            registerCaloriMeter(
                    pBuff,
                    nOffSet,
                    nUtility,
                    nMeterType,
                    nMeterValuePoint
            );
        } else if (nUtility == Meter.g_emMeterUtility.eHotwater) {
            registerHotwaterMeter(
                    pBuff,
                    nOffSet,
                    nUtility,
                    nMeterType,
                    nMeterValuePoint
            );
        } else if (nUtility == Meter.g_emMeterUtility.eElectronic) {
            registerElectronicMeter(
                    pBuff,
                    nOffSet,
                    nUtility,
                    nMeterType,
                    nMeterValuePoint
            );
        } else {
            registerWaterMeter(
                    pBuff,
                    nOffSet,
                    nUtility,
                    nMeterType,
                    nMeterValuePoint
            );
        }
    }

    /*
     * 수도
     */
    private void registerWaterMeter(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterWaterType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (nMeterType == Meter.g_emMeterWaterType.eShinhanDigitalBig) {
            nMeterValuePoint = 2;
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterWaterType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterWaterType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        }
    }

    /*
     * 가스
     */
    private void registerGasMeter(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterGasType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterGasType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterGasType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        }
    }

    /*
     * 열량
     */
    private void registerCaloriMeter(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterCaloriType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType == Meter.g_emMeterCaloriType.eOneTLDigital ||
                        nMeterType == Meter.g_emMeterCaloriType.eIcmDplc
        ) {
            parserMeterCaloriOneTLDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterCaloriType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterCaloriType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        }
    }

    /*
     * 온수
     */
    private void registerHotwaterMeter(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterHotwaterType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType == Meter.g_emMeterHotwaterType.eOneTLDigital ||
                        nMeterType == Meter.g_emMeterHotwaterType.eIcmDplc
        ) {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterHotwaterType.ePulse_1000L &&
                        nMeterType <= Meter.g_emMeterHotwaterType.ePulse_05L
        ) {
            parserMeterPulse(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        }
    }

    /*
     * 전기
     */
    private void registerElectronicMeter(
            byte[] pBuff,
            int nOffSet,
            int nUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        if (nMeterType == Meter.g_emMeterElectronicType.eStandardDigital) {
            parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
        } else if (
                nMeterType >= Meter.g_emMeterElectronicType.ePulse_1000W &&
                        nMeterType <= Meter.g_emMeterElectronicType.ePulse_01W
        ) {
            parserMeterPulse(pBuff, nOffSet, nUtility, nMeterType, nMeterValuePoint);
        } else {
            parserMeterDefault(pBuff, nOffSet, nMeterValuePoint);
        }
    }

    //기본 계량기 데이터
    private int parserMeterDefault(
            byte[] pBuff,
            int nOffSet,
            int nMeterValuePoint
    ) {
        m_strSecondMeterValue = "";
        //Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strFirstMeterValue = "";
            m_strSecondMeterValue = "";

            return nOffSet;
        } else {
            m_strFirstMeterValue =
                    Meter.ParserMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        }
        nOffSet += 4;

        //2번째 검침 데이터 확인
        int nSecondValid = checkSecondMeterValueValid(pBuff, nOffSet);
        if (nSecondValid == SECOND_METER_VALUE_OK) {
            int nMeterAddVal = parseMeterHexAddVal(nOffSet);
            if (Meter.CheckMeterAddValue(nMeterAddVal, m_nMeterInterval) == true) {
                m_strSecondMeterValue =
                        Meter.ParserMeterAddValInt(
                                m_strFirstMeterValue,
                                nMeterAddVal,
                                nMeterValuePoint
                        );
            } else {
                m_nMeterConnection = METER_VALID_VALUE_ERROR;
                m_strSecondMeterValue = "";
            }
        } else if (nSecondValid == SECOND_METER_VALUE_ERROR) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strSecondMeterValue = "";
            return nOffSet;
        } else if (nSecondValid == SECOND_METER_VALUE_FIRST) {
            m_strSecondMeterValue = m_strFirstMeterValue;
            m_strFirstMeterValue = "";
        }

        nOffSet += 2;

        return nOffSet;
    }

    //펄스 계량기
    private int parserMeterPulse(
            byte[] pBuff,
            int nOffSet,
            int eUtility,
            int nMeterType,
            int nMeterValuePoint
    ) {
        m_strSecondMeterValue = "";
        //Pulse Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strFirstMeterValue = "";
            m_strSecondMeterValue = "";

            return nOffSet;
        } else {
            m_strFirstMeterValue =
                    Meter.ParserPulseMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        }

        nOffSet += 4;

        //2번째 검침 데이터 확인
        int nSecondValid = checkSecondMeterValueValid(pBuff, nOffSet);
        if (nSecondValid == SECOND_METER_VALUE_OK) {
            int nMeterAddVal = parseMeterHexAddVal(nOffSet);
            if (Meter.CheckMeterAddValue(nMeterAddVal, m_nMeterInterval) == true) {
                m_strSecondMeterValue =
                        Meter.ParserMeterAddValInt(
                                m_strFirstMeterValue,
                                nMeterAddVal,
                                nMeterValuePoint
                        );
            } else {
                m_nMeterConnection = METER_VALID_VALUE_ERROR;
                m_strSecondMeterValue = "";
            }
        } else if (nSecondValid == SECOND_METER_VALUE_ERROR) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strSecondMeterValue = "";
            return nOffSet;
        } else if (nSecondValid == SECOND_METER_VALUE_FIRST) {
            m_strSecondMeterValue = m_strFirstMeterValue;
            m_strFirstMeterValue = "";
        }
        nOffSet += 2;
        return nOffSet;
    }

    //표준 프로토콜
    private int parserMeterStandardDigital(
            byte[] pBuff,
            int nOffSet,
            int nMeterValuePoint
    ) {
        m_strSecondMeterValue = "";

        //Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strFirstMeterValue = "";
            m_strSecondMeterValue = "";

            return nOffSet;
        } else {
            m_strFirstMeterValue =
                    Meter.ParserStandardMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        }
        nOffSet += 4;

        //2번째 검침 데이터 확인
        int nSecondValid = checkSecondMeterValueValid(pBuff, nOffSet);
        if (nSecondValid == SECOND_METER_VALUE_OK) {
            int nMeterAddVal = parseMeterHexAddVal(nOffSet);
            if (Meter.CheckMeterAddValue(nMeterAddVal, m_nMeterInterval) == true) {
                m_strSecondMeterValue =
                        Meter.ParserMeterAddValInt(
                                m_strFirstMeterValue,
                                nMeterAddVal,
                                nMeterValuePoint
                        );
            } else {
                m_nMeterConnection = METER_VALID_VALUE_ERROR;
                m_strSecondMeterValue = "";
            }
        } else if (nSecondValid == SECOND_METER_VALUE_ERROR) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strSecondMeterValue = "";
            return nOffSet;
        } else if (nSecondValid == SECOND_METER_VALUE_FIRST) {
            m_strSecondMeterValue = m_strFirstMeterValue;
            m_strFirstMeterValue = "";
        }
        nOffSet += 2;

        return nOffSet;
    }

    //원티엘 열량계 프로토콜
    private int parserMeterCaloriOneTLDigital(
            byte[] pBuff,
            int nOffSet,
            int nMeterValuePoint
    ) {
        m_strSecondMeterValue = "";
        //Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strFirstMeterValue = "";
            m_strSecondMeterValue = "";

            return nOffSet;
        } else {
            //열량값
            m_strFirstMeterValue =
                    Meter.ParserStandardMeterVal(pBuff, nOffSet, 4, nMeterValuePoint); //소숫점2자리
        }

        nOffSet += 4;

        //2번째 검침 데이터 확인
        int nSecondValid = checkSecondMeterValueValid(pBuff, nOffSet);
        if (nSecondValid == SECOND_METER_VALUE_OK) {
            int nMeterAddVal = parseMeterHexAddVal(nOffSet);
            if (Meter.CheckMeterAddValue(nMeterAddVal, m_nMeterInterval) == true) {
                m_strSecondMeterValue =
                        Meter.ParserMeterAddValInt(
                                m_strFirstMeterValue,
                                nMeterAddVal,
                                nMeterValuePoint
                        );
            } else {
                m_nMeterConnection = METER_VALID_VALUE_ERROR;
                m_strSecondMeterValue = "";
            }
        } else if (nSecondValid == SECOND_METER_VALUE_ERROR) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strSecondMeterValue = "";
            return nOffSet;
        } else if (nSecondValid == SECOND_METER_VALUE_FIRST) {
            m_strSecondMeterValue = m_strFirstMeterValue;
            m_strFirstMeterValue = "";
        }
        nOffSet += 2;

        return nOffSet;
    }
}
