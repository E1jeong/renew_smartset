/**
 * <pre>
 * 상수 정의 클래스.
 * </pre>
 */
package com.hitec.presentation.nfc_lib.util;

public interface ConstMeter {
    //계량기 공급 종류
    String UTILTY_METER_WATER = "1"; //수도
    String UTILTY_METER_GAS = "2"; //가스
    String UTILTY_METER_CALORI = "3"; //열량
    String UTILTY_METER_HOTWATER = "4"; //온수
    String UTILTY_METER_ELECTRONIC = "5"; //전기

    int UTILTY_METER_WATER_NUM = 1; //수도
    int UTILTY_METER_GAS_NUM = 2; //가스
    int UTILTY_METER_CALORI_NUM = 3; //열량
    int UTILTY_METER_HOTWATER_NUM = 4; //온수
    int UTILTY_METER_ELECTRONIC_NUM = 5; //전기

    //계량기 포트 TYPE 종류
    String METER_PORT_TYPE_UART1 = "1";
    String METER_PORT_TYPE_UART2 = "2";
    String METER_PORT_TYPE_PULSE1 = "11";
    String METER_PORT_TYPE_PULSE2 = "12";
    String METER_PORT_TYPE_RS232 = "21";
    String METER_PORT_TYPE_RS485 = "31";
    String METER_PORT_TYPE_DPLC = "41";

    //계량기 포트 INDEX 종류
    String METER_PORT_INDEX_UART = "1";
    String METER_PORT_INDEX_PULSE = "2";
    String METER_PORT_INDEX_RS232 = "3";
    String METER_PORT_INDEX_RS485 = "4";
    String METER_PORT_INDEX_DPLC = "5";

    /*
     * WATER
     */
    String METER_WATER_StandardDigital = "01";
    String METER_WATER_HitecDigital = "10";
    String METER_WATER_HitecShDigital = "11";
    String METER_WATER_ShinhanDigital = "20";
    String METER_WATER_MnSDigital = "30";
    String METER_WATER_ShinhanDigitalBig = "22";
    String METER_WATER_Primo = "40";
    String METER_WATER_Badger = "42";
    String METER_WATER_ModbusYk = "43";
    String METER_WATER_OneTLDigital = "50";
    String METER_WATER_IcmDigital = "B2";
    String METER_WATER_Pulse_1000L = "71";
    String METER_WATER_Pulse_500L = "72";
    String METER_WATER_Pulse_100L = "73";
    String METER_WATER_Pulse_50L = "74";
    String METER_WATER_Pulse_10L = "75";
    String METER_WATER_Pulse_5L = "76";
    String METER_WATER_Pulse_1L = "77";
    String METER_WATER_Pulse_05L = "78";
    String METER_WATER_Protocol_Default =
            METER_WATER_StandardDigital;
    String METER_WATER_Port_Default = METER_PORT_TYPE_UART1;

    int METER_WATER_NUM_StandardDigital = 1;

    /*
     * GAS
     */
    String METER_GAS_StandardDigital = "02";
    String METER_GAS_OneTLDigital = "51";
    String METER_GAS_IcmDigital = "B4";
    String METER_GAS_Pulse_1000L = "A1";
    String METER_GAS_Pulse_500L = "A2";
    String METER_GAS_Pulse_100L = "A3";
    String METER_GAS_Pulse_50L = "A4";
    String METER_GAS_Pulse_10L = "A5";
    String METER_GAS_Pulse_5L = "A6";
    String METER_GAS_Pulse_1L = "A7";
    String METER_GAS_Pulse_05L = "A8";
    String METER_GAS_Protocol_Default =
            METER_GAS_StandardDigital;
    String METER_GAS_Port_Default = METER_PORT_TYPE_UART1;

    /*
     * Calori
     */
    String METER_CALORI_StandardDigital = "03";
    String METER_CALORI_OneTLDigital = "52";
    String METER_CALORI_IcmDigital = "B5";
    String METER_CALORI_Pulse_1000L = "91";
    String METER_CALORI_Pulse_500L = "92";
    String METER_CALORI_Pulse_100L = "93";
    String METER_CALORI_Pulse_50L = "94";
    String METER_CALORI_Pulse_10L = "95";
    String METER_CALORI_Pulse_5L = "96";
    String METER_CALORI_Pulse_1L = "97";
    String METER_CALORI_Pulse_05L = "98";
    String METER_CALORI_Protocol_Default =
            METER_CALORI_StandardDigital;
    String METER_CALORI_Port_Default = METER_PORT_TYPE_UART1;

    /*
     * Hotwater
     */
    String METER_HOTWATER_StandardDigital = "04";
    String METER_HOTWATER_OneTLDigital = "52";
    String METER_HOTWATER_IcmDigital = "B3";
    String METER_HOTWATER_Pulse_1000L = "81";
    String METER_HOTWATER_Pulse_500L = "82";
    String METER_HOTWATER_Pulse_100L = "83";
    String METER_HOTWATER_Pulse_50L = "84";
    String METER_HOTWATER_Pulse_10L = "85";
    String METER_HOTWATER_Pulse_5L = "86";
    String METER_HOTWATER_Pulse_1L = "87";
    String METER_HOTWATER_Pulse_05L = "88";
    String METER_HOTWATER_Protocol_Default =
            METER_HOTWATER_StandardDigital;
    String METER_HOTWATER_Port_Default =
            METER_PORT_TYPE_UART1;

    /*
     * Electronic
     */
    String METER_ELECTRONIC_HitecDigital = "05";
    String METER_ELECTRONIC_Pulse_1000W = "61";
    String METER_ELECTRONIC_Pulse_100W = "62";
    String METER_ELECTRONIC_Pulse_10W = "63";
    String METER_ELECTRONIC_Pulse_8W = "64";
    String METER_ELECTRONIC_Pulse_4W = "65";
    String METER_ELECTRONIC_Pulse_2W = "66";
    String METER_ELECTRONIC_Pulse_1W = "67";
    String METER_ELECTRONIC_Pulse_04W = "68";
    String METER_ELECTRONIC_Pulse_02W = "69";
    String METER_ELECTRONIC_Pulse_01W = "6A";
    String METER_ELECTRONIC_Protocol_Default =
            METER_ELECTRONIC_HitecDigital;
    String METER_ELECTRONIC_Port_Default =
            METER_PORT_TYPE_RS485;

    //전기계량기 종류
    int E_METER_CALIBER_UNKNOWN = 0;
    int E_METER_CALIBER_1P2W = 1; //1상 2선식
    int E_METER_CALIBER_1P3W = 2; //1상 3선식
    int E_METER_CALIBER_3P3W = 3; //3상 3선식
    int E_METER_CALIBER_3P4W = 4; //3상 4선식

    //계량기 상태 정보에서 계량기 오류인 경우
    String METER_STATUS_METER_ERROR = "FF";

    //계량기 설정
    int METER_INFO_CHANGE_MODE_SN = 1; //일련번호 수정
    int METER_INFO_CHANGE_MODE_Q3 = 2; //Q3 수정
    int METER_INFO_CHANGE_MODE_SN_Q3 = 3; //일련번호+Q3 수정

    //계량기 구경
    String METER_CALIBER_15mm = "15";
    String METER_CALIBER_20mm = "20";
    String METER_CALIBER_25mm = "25";
    String METER_CALIBER_32mm = "32";
    String METER_CALIBER_40mm = "40";
    String METER_CALIBER_50mm = "50";

    int METER_Q3_INDEX = 0;
    int METER_Q2_INDEX = 1;
    int METER_Q1_INDEX = 2;
    int METER_Qt_INDEX = 3; //호환을 위헤 그냥둠
    int METER_Qs_INDEX = 4; //호환을 위헤 그냥둠
    int METER_Qn_MAX = 5;

    int METER_AUTO_COMP_MODE_CALIB = 1;
    int METER_AUTO_COMP_MODE_CHECK = 2;
    int METER_AUTO_COMP_MODE_STATUS = 3;

    //계량기 설정 응답
    int METER_INFO_RESULT_SUCCESS = 0; //0 : 설정 완료
    int METER_INFO_ERROR_SN = 1; //1 : 계량기 시리얼번호 오류
    int METER_INFO_ERROR_FLASH_OVER = 2; //2 : FLASH 보호시간 초과
    int METER_INFO_ERROR_OVER_30P = 3; //3 : 오차율 30% 초과 (설비용)
    int METER_INFO_ERROR_Q3_NOT = 4; //4 : Q3이 아님(설비용)
    int METER_INFO_ERROR_Q3_50P_OVER = 5; //5 : Q3cc 설정값 50% 초과
    int METER_INFO_ERROR_MAKER_MISSMATCH = 6; //6 : 계량기 제조사 다름
    int METER_INFO_ERROR_NO_RESPONSE = 11; //11: 계량기 응답없슴

    //계량기 설정 방법
    int METER_SET_CONFIG_TYPE_MANUAL = 0; //0 : 수동
    int METER_SET_CONFIG_TYPE_CERTIFICATION = 1; //1 : 인증
    int METER_SET_CONFIG_TYPE_AUTO = 2; //2 : 자동보정
    int METER_SET_CONFIG_TYPE_MAX =
            METER_SET_CONFIG_TYPE_AUTO;

    //설정 모드
    int METER_SELECT_COMP_Q3_READY = 0;
    int METER_SELECT_COMP_Q3_CALC = 1;
    int METER_SELECT_COMP_Q2_READY = 2;
    int METER_SELECT_COMP_Q2_CALC = 3;
    int METER_SELECT_COMP_Q1_READY = 4;
    int METER_SELECT_COMP_Q1_CALC = 5;
    int METER_SELECT_COMP_MAX = METER_SELECT_COMP_Q1_CALC;

    //보정 상태
    String METER_COMP_STATE_Q3_READY = "0";
    String METER_COMP_STATE_Q3_CALC = "1";
    String METER_COMP_STATE_Q2_CALC = "2";
    String METER_COMP_STATE_Q1_CALC = "3";

    //자동보정용
    String METER_STANDARD_Q3_DEFAULT = "100.0"; //unit:L
    String METER_STANDARD_Q2_DEFAULT = "10.0"; //unit:L
    String METER_STANDARD_Q1_DEFAULT = "10.0"; //unit:L

    //인증용 계량기 FW
    int METER_FW_CERTIFICATION_VERSION_MAX = 50; //50인하인 경우에만 진행
}
