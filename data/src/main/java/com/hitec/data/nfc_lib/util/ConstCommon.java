/**
 * <pre>
 * 상수 정의 클래스.
 * </pre>
 */
package com.hitec.data.nfc_lib.util;

public interface ConstCommon {
    /*
     * 휴대 중계기 상태 정보 표시
     */
    int BT_REPEATER_STATUS_REDAY = 0; //연결 준비
    int BT_REPEATER_STATUS_SUCCESS = 1; //연결 성공
    int BT_REPEATER_STATUS_ERROR = 2; //연결 오류
    int BT_REPEATER_STATUS_FAIL = 3; //연결 오류
    int BT_REPEATER_STATUS_LOST = 4; //연결 오류
    int BT_REPEATER_FREQ_BAND_ERROR = 5; //주파수대역 오류

    int BT_STATE_NONE = 0;
    int BT_STATE_ALIVE_IND = 1;
    int BT_STATE_RF_MODE = 2;
    int BT_STATE_MODEM_INFO = 3;
    int BT_STATE_MODEM_RSSI = 4;
    int BT_STATE_MODEM_CONFIG = 5;

    /*
     * 휴대중계기 설정 변경
     */
    int BT_SET_CONFIG_NONE = 0;
    int BT_SET_CONFIG_DEIVCE_NAME = 1;
    int BT_SET_CONFIG_ACTIVE_MODE = 2;
    int BT_SET_CONFIG_FREQ_BAND = 3;

    int BT_FW_VERSION_RF_MONITOR = 0x15;

    /*
     * NFC setting use
     */
    int NFC_USE_STATUS_DISABLE = 0; //NFC 사용안함
    int NFC_USE_STATUS_ENABLE = 1; //NFC 사용

    //통신방식 종류
    int COMMUNICATION_TYPE_UNKNOWN = 0; //
    int COMMUNICATION_TYPE_HAMI = 1; //HITEC-AMI
    int COMMUNICATION_TYPE_WMU = 2; //WMU
    int COMMUNICATION_TYPE_LORA = 3; //LORA
    int COMMUNICATION_TYPE_NB = 4; //NB-IOT
    int COMMUNICATION_TYPE_DISPLAY = 5; //DISPLAY	옥외표시
    int COMMUNICATION_TYPE_GSM = 6; //GSM

    String STR_COMMUNICATION_TYPE_UNKNOWN = "0"; //
    String STR_COMMUNICATION_TYPE_HAMI = "1"; //HITEC-AMI
    String STR_COMMUNICATION_TYPE_WMU = "2"; //WMU
    String STR_COMMUNICATION_TYPE_LORA = "3"; //LORA
    String STR_COMMUNICATION_TYPE_NB = "4"; //NB-IOT
    String STR_COMMUNICATION_TYPE_DISPLAY = "5"; //DISPLAY 옥외표시
    String STR_COMMUNICATION_TYPE_GSM = "6"; //GSM

    //통신사 종류
    int TELECOM_TYPE_UNKNOWN = 0; //
    int TELECOM_TYPE_HITEC = 1; //HITEC
    int TELECOM_TYPE_SKT = 2; //SKT
    int TELECOM_TYPE_KT = 3; //KT
    int TELECOM_TYPE_LGU = 4; //LGU+

    String STR_TELECOM_TYPE_UNKNOWN = "0"; //
    String STR_TELECOM_TYPE_HITEC = "1"; //HITEC
    String STR_TELECOM_TYPE_SKT = "2"; //SKT
    String STR_TELECOM_TYPE_KT = "3"; //KT
    String STR_TELECOM_TYPE_LGU = "4"; //LGU+

    //장비일련번호 통신사 코드
    String STR_TELECOM_SN_SKT = "S"; //SKT
    String STR_TELECOM_SN_HITEC = "H"; //HITEC
    String STR_TELECOM_SN_LOCAL = "K"; //KR920

    //IMSI 통신사 코드
    String STR_TELECOM_IMSI_SKT = "05"; //SKT
    String STR_TELECOM_IMSI_KT1 = "04"; //KT1
    String STR_TELECOM_IMSI_KT2 = "08"; //KT2
    String STR_TELECOM_IMSI_LGU = "06"; //LGU+

    //설치 장비 종류
    int NODE_TYPE_UNKNOWN = 0; //
    int NODE_TYPE_CONCEN = 1; //수집기
    int NODE_TYPE_REPEATER = 2; //중계기
    int NODE_TYPE_TERM = 3; //검침기
    int NODE_TYPE_WMU = 4; //WMU
    int NODE_TYPE_METER = 10; //METER
    int NODE_TYPE_HANDY = 21; //휴대중계기 설정

    String NODE_MODEL_CODE_HAC_12C = "1"; //수집기
    String NODE_MODEL_CODE_HAC_11C = "2"; //수집기
    String NODE_MODEL_CODE_HAR_11 = "3"; //중계기
    String NODE_MODEL_CODE_HAT_114W = "4"; //표시형 단말기
    String NODE_MODEL_CODE_HAT_514W = "9"; //표시형 단말기 우수제품
    String NODE_MODEL_CODE_HAT_124W = "5"; //비표시형 단말기
    String NODE_MODEL_CODE_HAT_524W = "10"; //비표시형 단말기 우수제품
    String NODE_MODEL_CODE_HAT_314W = "91"; //단순표시기
    String NODE_MODEL_CODE_HTM_115W = "7"; //일체형 단말기
    String NODE_MODEL_CODE_HTM_515U = "11"; //일체형 단말기 우수제품
    String NODE_MODEL_CODE_HTM_615U = "51"; //해외향 일체형 단말기 우수제품
    String NODE_MODEL_CODE_HAT_435W = "8"; //보조 중계기

    String NODE_MODEL_NAME_HAC_12C = "HAC-12C"; //수집기
    String NODE_MODEL_NAME_HAC_11C = "HAC-11C"; //수집기
    String NODE_MODEL_NAME_HAR_11 = "HAR-11"; //중계기
    String NODE_MODEL_NAME_HAT_114W = "HAT-114W"; //검침기
    String NODE_MODEL_NAME_HAT_124W = "HAT-124W"; //검침기
    String NODE_MODEL_NAME_HAT_314W = "HAT-314W"; //검침기
    String NODE_MODEL_NAME_HTM_115W = "HTM-115W"; //일체형 검침기
    String NODE_MODEL_NAME_HAT_435W = "HTM-435W"; //보조 중계기

    int CONV_SN_TERM_MODEL_MIN_YEAR = 16; //일련번호로 모델명 설정 최소 기준년도
    String CONV_SN_TERM_MODEL_HAT_114W = "1"; //검침기
    String CONV_SN_TERM_MODEL_HAT_124W = "2"; //검침기
    String CONV_SN_TERM_MODEL_HTM_115W = "3"; //검침기
    String CONV_SN_TERM_MODEL_HAT_435W = "4"; //보조 중계기

    String CONV_SN_TERM_MODEL_HAT_114W_EXT = "6"; //검침기(외부업체)
    String CONV_SN_TERM_MODEL_HAT_124W_EXT = "7"; //검침기(외부업체)
    String CONV_SN_TERM_MODEL_HTM_115W_EXT = "8"; //검침기(외부업체)
    String CONV_SN_TERM_MODEL_HAT_435W_EXT = "9"; //보조 중계기(외부업체)

    /*
     * Gateway
     */

    //GW
    String GW_MODEM_TYPE_NONE = "0";
    String GW_MODEM_TYPE_SK2G = "1";
    String GW_MODEM_TYPE_GSM = "2";
    String GW_MODEM_TYPE_SK3G = "3";
    String GW_MODEM_TYPE_KT3G = "10";
    String GW_MODEM_TYPE_VPN = "20";
    String GW_MODEM_TYPE_ETHERNET = "60";

    //GW 상태
    int GW_STATE_NONE = 0;
    int GW_STATE_OFF = 1; //Power Off
    int GW_STATE_ON = 2; //Power On
    int GW_STATE_TIME = 3; //시간 수신중
    int GW_STATE_SERVER = 4; //서버 접속 및 데이터 전송중
    int GW_STATE_ERROR = 0x10; //GW 동작 오류

    //New GW State
    int GW_NEW_STATE_NORMAL = 0; //정상
    int GW_NEW_STATE_ON = 1; //Power On
    int GW_NEW_STATE_TIME = 3; //시간 수신중
    int GW_NEW_STATE_SERVER = 4; //서버 접속 및 데이터 전송중
    int GW_NEW_STATE_ERROR = 0x10; //GW 동작 오류

    //GW 서버 접속 결과
    int GW_SERVER_SUCCESS = 0;
    int GW_SERVER_PAN_CHANGEED = 1;
    int GW_SERVER_MODEM_CONNECT_ERROR = 2;
    int GW_SERVER_CONNECT_ERROR = 3;
    int GW_SERVER_LOGIN_FAIL = 4;
    int GW_SERVER_DATA_INFO_ERROR = 5;
    int GW_SERVER_DATA_METER_ERROR = 6;
    int GW_SERVER_DATA_STATUS_ERROR = 7;
    int GW_SERVER_COMMAND_ERROR = 8;
    int GW_SERVER_MODEM_SIM_ERROR = 9;
    int GW_SERVER_MODEM_TIME_ERROR = 10;
    int GW_SERVER_MODEM_OPERATION_ERROR = 11;
    int GW_SERVER_BACKUP_CONNECT = 16;

    String GW_VERSION_NULL = "0.0";
    String GW_PHONE_NUM_NULL = "00000000000";

    //AMI 단말기 종류
    String ACTIVE_TYPE_AMI_NONE = "0";
    String ACTIVE_TYPE_AMI_AMI = "1";
    String ACTIVE_TYPE_AMI_PDA = "2";
    String ACTIVE_TYPE_AMI_DUAL = "3";
    String ACTIVE_TYPE_AMI_AMI_S = "4";
    String ACTIVE_TYPE_AMI_PDA_S = "5";
    String ACTIVE_TYPE_AMI_AMI_M = "6";
    String ACTIVE_TYPE_AMI_PDA_M = "7";
    String ACTIVE_TYPE_AMI_DUAL_M = "8";
    String ACTIVE_TYPE_AMI_DPLC = "9";

    String ACTIVE_TYPE_AMI_UNKNOWN_NAME = "Unknown";
    String ACTIVE_TYPE_AMI_AMI_NAME = "AMI";
    String ACTIVE_TYPE_AMI_PDA_NAME = "PDA";
    String ACTIVE_TYPE_AMI_DUAL_NAME = "DUAL";
    String ACTIVE_TYPE_AMI_AMI_S_NAME = "AMI-S";
    String ACTIVE_TYPE_AMI_PDA_S_NAME = "PDA-S";
    String ACTIVE_TYPE_AMI_AMI_M_NAME = "AMI-M";
    String ACTIVE_TYPE_AMI_PDA_M_NAME = "PDA-M";
    String ACTIVE_TYPE_AMI_DUAL_M_NAME = "DUAL-M";
    String ACTIVE_TYPE_AMI_DPLC_NAME = "DPLC";

    int ACTIVE_MODE_AMI_NONE = 0;
    int ACTIVE_MODE_AMI_AMI = 1;
    int ACTIVE_MODE_AMI_PDA = 2;
    int ACTIVE_MODE_AMI_DUAL = 3;
    int ACTIVE_MODE_AMI_AMI_S = 4;
    int ACTIVE_MODE_AMI_PDA_S = 5;
    int ACTIVE_MODE_AMI_AMI_M = 6;
    int ACTIVE_MODE_AMI_PDA_M = 7;
    int ACTIVE_MODE_AMI_DUAL_M = 8;
    int ACTIVE_MODE_AMI_DPLC = 9;

    String ACTIVE_TYPE_LORA_UNKNOWN_NAME = "Unknown";
    String ACTIVE_TYPE_LORA_AMI_NAME = "LORA";
    String ACTIVE_TYPE_LORA_AMI_S_NAME = "LORA-S";
    String ACTIVE_TYPE_LORA_AMI_M_NAME = "LORA-M";

    String ACTIVE_TYPE_NB_UNKNOWN_NAME = "Unknown";
    String ACTIVE_TYPE_NB_AMI_NAME = "NB-IoT";
    String ACTIVE_TYPE_NB_AMI_S_NAME = "NB-S";
    String ACTIVE_TYPE_NB_AMI_M_NAME = "NB-M";

    /*
     * 단말기 기본설정
     */
    String DEFAULT_TERM_MTER_UTILTY = "1"; //계량기유형1
    String DEFAULT_TERM_METER_PROTOCOL = "01"; //계량기타입1
    String DEFAULT_TERM_METER_PORT = "1"; //계량기포트1
    String DEFAULT_TERM_BASE_TIME = "0"; //AMI검침기준시각
    String DEFAULT_TERM_INTERVAL_TIME = "1"; //AMI검침시간간격
    String DEFAULT_TERM_REPORT_TIME = "1"; //AMI보고시간간격
    String DEFAULT_TERM_DATA_SKIP_FLAG = "1"; //데이터 전송 플래그
    String DEFAULT_TERM_STORE_MONTH = "2"; //PDA저장개월수
    String DEFAULT_TERM_BASE_DAY = "5"; //PDA검침기준일
    String DEFAULT_TERM_ALERT_TIME = "1"; //PDA알람시각
    String DEFAULT_TERM_PERIOD_TIME = "24"; //PDA검침시간간격
    String DEFAULT_TERM_ACTIVE_START_DAY = "1"; //매월기상시작일
    String DEFAULT_TERM_ACTIVE_END_DAY = "31"; //매월기상종료일
    String DEFAULT_TERM_ACTIVE_START_HOUR = "1"; //기상시작시간
    String DEFAULT_TERM_ACTIVE_END_HOUR = "1"; //기상종료시각

    String DEFAULT_LORA_NB_INTERVAL_TIME = "1"; //LORA,NB 검침시간간격
    int DEFAULT_NUM_LORA_NB_INTERVAL_TIME = 1; //LORA,NB 검침시간간격

    String DEFAULT_LORA_NB_REPORT_TIME = "6"; //LORA,NB 보고시간간격
    int DEFAULT_NUM_LORA_NB_REPORT_TIME = 6; //LORA,NB 보고시간간격
    String DEFAULT_LORA_NB_DATA_SKIP_FLAG = "1"; //LORA,NB 데이터 스킵 플래그

    String DEFAULT_TERM_SUB_UPDATE_INTERVAL = "12"; //하위검침주기
    int DEFAULT_NUM_TERM_SUB_UPDATE_INTERVAL = 12; //하위검침주기
}
