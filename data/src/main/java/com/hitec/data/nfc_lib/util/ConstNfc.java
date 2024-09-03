/**
 * <pre>
 * 상수 정의 클래스.
 * </pre>
 */
package com.hitec.data.nfc_lib.util;

public interface ConstNfc {
    //NFC 종류
    int NFC_TYPE_NO = 0; //NFC Not Use
    int NFC_TYPE_A = 1; //NFC Type
    int NFC_TYPE_B = 2; //NFC Type
    int NFC_TYPE_DEFAULT = NFC_TYPE_NO; //NFC Default
    //A-Type, B-Type : waitI2CTime() 함수에서 사용

    int DEVICE_CODE_SMART = 0xFA;

    String STR_NULL_LORA_ID = "000000-0000-000000";
    String STR_NULL_NB_ID = "00-000000-000000-0";
    String STR_FIRST_NULL_LORA_ID = "000000";
    String STR_NULL_NB_CDMA_NO = "000-00-0000000000";

    //FW 종류
    int FW_LORA_L100 = 1; //
    int FW_LORA_DEFAULT = FW_LORA_L100; //

    //FW 종류
    int FW_NB_N100 = 1; //
    int FW_NB_DEFAULT = FW_NB_N100; //

    //RF Time out
    int NFC_TIME_OUT_DEFAULT = 2000;

    String DEFAULT_LORA_PAN = "0000";
    String DEFAULT_NB_PAN = "0000";
    String AMI_RF_LORA_NB_PAN = "0000";

    //NFC Write wait time msec
    int NFC_RESP_WAIT_TIME_DEFAULT = 500;
    int NFC_RESP_WAIT_TIME_NODE_CONF_SET = 100;
    int NFC_RESP_WAIT_TIME_READ_METER = 100;
    int NFC_RESP_WAIT_TIME_PERIOD_ACK = 20;
    int NFC_RESP_WAIT_TIME_SERVER_CONNECT = 100;
    int NFC_RESP_WAIT_TIME_SMART_METER = 1000; //350;

    //NFC Start wait time msec
    int NFC_START_WAIT_TIME_DEFAULT = 0;
    int NFC_START_WAIT_TIME_READ_METER = 1400; //800;

    //RF State
    int NFC_RF_STATE_None = 0;
    int NFC_RF_STATE_ReadConfig = 1;
    int NFC_RF_STATE_ReadLoraConf = 2;
    int NFC_RF_STATE_ReadNbConf = 3;
    int NFC_RF_STATE_WriteConfig = 4;
    int NFC_RF_STATE_WriteLoraConf = 5;
    int NFC_RF_STATE_WriteNbConf = 6;
    int NFC_RF_STATE_ReadMeter = 7;
    int NFC_RF_STATE_ReadPeriodMeter = 8;
    int NFC_RF_STATE_ReqServerConnect = 10;
    int NFC_RF_STATE_ReadServerConnect = 11;
    int NFC_RF_STATE_WritePulseMeter = 12;
    int NFC_RF_STATE_ReqMainReset = 13;
    int NFC_RF_STATE_SetTimeInfo = 14;
    int NFC_RF_STATE_ReadSaveConfig = 15;
    int NFC_RF_STATE_ControlSleepMode = 16;
    int NFC_RF_STATE_ControlWakeupMode = 17;
    int NFC_RF_STATE_ControlReportMode = 18;
    int NFC_RF_STATE_WriteServerServiceCode = 19;
    int NFC_RF_STATE_WriteManualServiceCode = 20;
    int NFC_RF_STATE_ReadServiceCode = 21;
    int NFC_RF_STATE_ReadAppEuiKey = 22;
    int NFC_RF_STATE_WriteAppEuiKey = 23;
    int NFC_RF_STATE_ControlPeriodMode = 24;
    int NFC_RF_STATE_ControlDebugMode = 25;
    int NFC_RF_STATE_SetFwUpdate = 26;
    int NFC_RF_STATE_ControlDataSkipMode = 27;
    int NFC_RF_STATE_SerialChange = 28;
    int NFC_RF_STATE_ChangeMinuteInterval = 29;
    int NFC_RF_STATE_UploadCheckDeviceSn = 30;
    int NFC_RF_STATE_SelectGsmOrLte = 31;
    int NFC_RF_STATE_ChangeDomain = 32;

    int NFC_RF_STATE_SmartMeterRead = 41;
    int NFC_RF_STATE_SmartConfRead = 42;
    int NFC_RF_STATE_SmartConfMeterRead = 43;
    int NFC_RF_STATE_SmartConfQnWrite = 44;
    int NFC_RF_STATE_SmartConfSnWrite = 45;
    int NFC_RF_STATE_SmartMeterValueWrite = 46;
    int NFC_RF_STATE_SmartCertiCompWrite = 47;
    int NFC_RF_STATE_SmartCertiCompRead = 48;
    int NFC_RF_STATE_SmartMeterCountStart = 49;
    int NFC_RF_STATE_SmartMeterCountRead = 50;
    int NFC_RF_STATE_SmartMeterCountReset = 51;
    int NFC_RF_STATE_SmartConfTemperatureWrite = 52;
    int NFC_RF_STATE_SmartConfAutoCompStart = 53;

    //LORA DATA FORMAT
    int LORA_DATA_FORMAT_NONE = 0;
    int LORA_DATA_FORMAT_SKT = 1;
    int LORA_DATA_FORMAT_HITEC = 2;

    //NB DATA FORMAT
    int NB_DATA_FORMAT_NONE = 0;
    int NB_DATA_FORMAT_HITEC = 1;
    int NB_DATA_FORMAT_TLV = 2;

    //NB SERVER 통신모드
    int NB_SERVER_UDP = 0;
    int NB_SERVER_PLATFROM = 1;

    //DISPLAY DATA FORMAT
    int DISPLAY_DATA_FORMAT_NONE = 0;

    //모뎀 상태
    int MODEM_STATE_IDLE = 0;
    int MODEM_STATE_SERVER_CONNNECT = 1; //서버접속 중
    int MODEM_STATE_SERVER_WAIT = 2; //서버접속 대기

    //설정 결과 종류
    int CONF_RESULT_TYPE_SUCCESS = 0;
    int CONF_RESULT_TYPE_ERROR = 1;

    //NB 통신오류 오류 종류
    int NB_RESULT_OK = 0; //정상
    int NB_RESULT_ERROR_MODEM_NETWORK = 1; //모뎀연결오류(또는 기지국 접속 실패)
    int NB_RESULT_ERROR_SIM_GATEWAY = 2; //SIM 오류(또는 기지국 접속오류)
    int NB_RESULT_ERROR_RESERVED = 3; //예비
    int NB_RESULT_ERROR_SERVER = 4; //서버 접속 실패
    int NB_RESULT_ERROR_TIME = 5; //시간 수신오류
    int NB_RESULT_ERROR_ATTACH = 6; //망 접속 실패
    int NB_RESULT_ERROR_PLATFORM = 7; //플랫폼 접속 실패

    //설정 오류 종류
    int CONF_SET_ERROR_NONE = 0;
    int CONF_SET_ERROR_SN = 1;
    int CONF_SET_ERROR_TIME = 2;
    int CONF_SET_ERROR_METER_TYPE = 3;
    int CONF_SET_ERROR_METER_PORT = 4;

    //
    String LORA_APP_RESULT_NON = "";
    String LORA_APP_RESULT_OK = "OK";

    //LoRa Key 설정 결과 종류
    int LORA_KEY_SET_RESULT_WAIT = 0; //설정중
    int LORA_KEY_SET_RESULT_SUCCESS = 1; //설정완료
    int LORA_KEY_SET_RESULT_MODEM = 2; //모뎀 통신중
    int LORA_KEY_SET_RESULT_ERROR = 3; //설정오류

    //LoRa Key 설정 오류 종류
    int LORA_KEY_CONF_SET_ERROR_NONE = 0;
    int LORA_KEY_CONF_SET_ERROR_SN = 1; //일련번호 오류
    int LORA_KEY_CONF_SET_ERROR_MODEM = 2; //모뎀 오류

    //METER COUNT STATUS
    int METER_COUNT_STATUS_RESET = 0;
    int METER_COUNT_STATUS_START = 1;
    int METER_COUNT_STATUS_READ = 2;

    //METER 자동 보정 결과
    int METER_AUTO_COMP_RESULT_START = 0; //자동보정 시작
    int METER_AUTO_COMP_RESULT_RUN = 1; //자동보정 진행중
    int METER_AUTO_COMP_RESULT_STATUS = 2; //상태정보 확인

    int METER_AUTO_COMP_ERROR_NONE = 0; //정상
    int METER_AUTO_COMP_ERROR_FLASH = 1; //FLASH 보호시간 초과
    int METER_AUTO_COMP_ERROR_LOW_FLOW = 2; //보정실패 - 기준유량보다 적게 측정

    //FW Update Report result
    int FW_UPDATE_RESULT_OK = 0;
    int FW_UPDATE_RESULT_ERROR_SN = 1; //일련번호 오류
    int FW_UPDATE_RESULT_ERROR_WAIT = 2; //통신중인경우 대기요청
    int FW_UPDATE_RESULT_ERROR_NOT_BSL = 3; //지원안함

    int FW_UPDATE_STATE_INFO = 0; //NB 펌웨어 업데이트 상태정보
    int FW_UPDATE_STATE_READY = 1; //NB 펌웨어 업데이트 준비완료
    int FW_UPDATE_STATE_GSM = 2; //GSM단말기 펌웨어 업데이트
}
