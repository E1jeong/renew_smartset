/**
 * <pre>
 * RF Common 상수 정의
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol;

public interface NfcConstant {
    // 송수신되는 RF 메시지의 최대/최소 길이
    public static final int MAX_LEN_RF_MESSAGE = 64;
    public static final int MIN_LEN_RF_MESSAGE = 2; // MsgType, Length

    public static final int DIR_SMART_TO_NODE = 0;
    public static final int DIR_NODE_TO_SMART = 1;

    public static final int DEVICE_CODE_AMI = 0x03;
    public static final int DEVICE_CODE_LORA = 0x05;
    public static final int DEVICE_CODE_NB = 0x06;
    public static final int DEVICE_CODE_DISPLAY = 0x07;
    public static final int DEVICE_CODE_GSM = 0x08;
    public static final int DEVICE_CODE_SMART = 0xFA;

    public static final String DEVICE_ID_SMART = "FFFFFFFFAAAAAAAA";

    public static final int LORA_COM_MODE_LORA = 0;
    public static final int LORA_COM_MODE_RF = 1;

    public static final int ACTIVE_MODE_AMI_NONE = 0;
    public static final int ACTIVE_MODE_AMI_AMI = 1;
    public static final int ACTIVE_MODE_AMI_PDA = 2;
    public static final int ACTIVE_MODE_AMI_DUAL = 3;
    public static final int ACTIVE_MODE_AMI_AMI_S = 4;
    public static final int ACTIVE_MODE_AMI_PDA_S = 5;
    public static final int ACTIVE_MODE_AMI_AMI_M = 6;
    public static final int ACTIVE_MODE_AMI_PDA_M = 7;
    public static final int ACTIVE_MODE_AMI_DUAL_M = 8;

    /*
     * LORA FW Version
     */
    public static final int LORA_VERSION_L100 = 0;
    public static final int LORA_VERSION_DEFAULT = LORA_VERSION_L100;

    /*
     * NB-IOT FW Version
     */
    public static final int NB_VERSION_N100 = 0;
    public static final int NB_VERSION_DEFAULT = NB_VERSION_N100;

    /*
     * DISPLAY FW Version
     */
    public static final int DISPLAY_VERSION_D100 = 0;
    public static final int DISPLAY_VERSION_DEFAULT = DISPLAY_VERSION_D100;

    /*
     * Node Send Cmd type
     */
    public static final int NODE_SEND_NODE_CONF_REQ = 0x01;
    public static final int NODE_SEND_LORA_CONF_SET = 0x02;
    public static final int NODE_SEND_NB_CONF_SET = 0x03;
    public static final int NODE_SEND_METER_REQ = 0x04;
    public static final int NODE_SEND_SERVER_CONNECT_REQ = 0x05;
    public static final int NODE_SEND_BD_CONTROL_REQ = 0x06;
    public static final int NODE_SEND_PERIOD_METER_REQ = 0x07;
    public static final int NODE_SEND_PERIOD_METER_ACK = 0x08;
    public static final int NODE_SEND_PULSE_METER_VALUE_SET = 0x09;
    public static final int NODE_SEND_DEVICE_INFO_REQ = 0x0A;
    public static final int NODE_SEND_SLAVE_CHECK_REQ = 0x0B;
    public static final int NODE_SEND_NB_ID_REQ = 0x0C;
    public static final int NODE_SEND_NB_ID_SET = 0x0D;
    public static final int NODE_SEND_DISPLAY_CONF_SET = 0x0E;
    public static final int NODE_SEND_TIME_INFO_SET = 0x0F;
    public static final int NODE_SEND_ACCOUNT_NO_SET = 0x10;
    public static final int NODE_SEND_LORA_EUI_KEY_REQ = 0x11;
    public static final int NODE_SEND_LORA_EUI_KEY_SET = 0x12;
    public static final int NODE_SEND_FLASH_DATE_LIST_REQ = 0x13;
    public static final int NODE_SEND_FLASH_DATA_REQ = 0x14;
    public static final int NODE_SEND_FW_UPDATE_REQ = 0x15;
    public static final int NODE_SEND_SN_CHANGE_REQ = 0x16;
    public static final int NODE_SEND_CHANGE_MINUTE_INTEVAL_REQ = 0x17;
    public static final int NODE_SEND_SELECT_GSM_OR_LTE_REQ = 0x18;
    public static final int NODE_SEND_GSM_CHANGE_DOMAIN = 0x19;

    /*
     * Node Recv Cmd type
     */
    public static final int NODE_RECV_LORA_CONF_REPORT = 0x51;
    public static final int NODE_RECV_NB_CONF_REPORT = 0x52;
    public static final int NODE_RECV_METER_REPORT = 0x53;
    public static final int NODE_RECV_SERVER_CONNECT_REPORT = 0x54;
    public static final int NODE_RECV_BD_CONTROL_ACK = 0x55;
    public static final int NODE_RECV_FW_VER_REPORT = 0x56;
    public static final int NODE_RECV_PERIOD_METER_REPORT = 0x57;
    public static final int NODE_RECV_PULSE_METER_VALUE_ACK = 0x58;
    public static final int NODE_RECV_DEVICE_INFO_REPORT = 0x59;
    public static final int NODE_RECV_SLAVE_CHECK_REPORT = 0x5A;
    public static final int NODE_RECV_NB_ID_REPORT = 0x5B;
    public static final int NODE_RECV_DISPLAY_CONF_REPORT = 0x5E;
    public static final int NODE_RECV_TIME_INFO_REPORT = 0x5F;
    public static final int NODE_RECV_ACCOUNT_NO_REPORT = 0x60;
    public static final int NODE_RECV_LORA_EUI_KEY_REPORT = 0x61;
    public static final int NODE_RECV_FLASH_DATE_LIST_REPORT = 0x63;
    public static final int NODE_RECV_FLASH_DATA_REPORT = 0x64;
    public static final int NODE_RECV_FW_UPDATE_REPORT = 0x65;
    public static final int NODE_RECV_SN_CHANGE_REPORT = 0x66;
    public static final int NODE_RECV_CHANGE_MINUTE_INTERVAL_REPORT = 0x67;
    public static final int NODE_RECV_SELECT_GSM_OR_LTE_REPORT = 0x68;
    public static final int NODE_RECV_GSM_CHANGE_DOMAIN_REPORT = 0x69;

    /*
     * Smartmeter Send Cmd type
     */
    public static final int NODE_SEND_SMART_METER_SEND = 0x41;

    /*
     * Smartmeter Recv Cmd type
     */
    public static final int NODE_RECV_SMART_METER_RECV = 0x91;
    public static final int NODE_RECV_SMART_METER_ERROR_RESULT = 0x92;

    /*
     * meter port
     */
    public static final int UART_1 = 0x01;
    public static final int UART_2 = 0x02;
    public static final int PULSE_1 = 0x11;
    public static final int PULSE_2 = 0x12;
    public static final int RS232_1 = 0x21;
    public static final int RS485_1 = 0x31;
    public static final int DPLC = 0x41;

    //config 보고 종류
    public static final int CONF_REPORT_TYPE_REQ = 0;
    public static final int CONF_REPORT_TYPE_SET = 1;
    public static final int CONF_REPORT_TYPE_CODE_IP_CHECK = 2;

    //config Sleep Mode 종류
    public static final int CONF_SLEEP_STATE_NONE = 0;
    public static final int CONF_SLEEP_STATE_SLEEP = 1;
    public static final int CONF_SLEEP_STATE_NORMAL = 2;

    //config Report Mode 종류
    public static final int CONF_REPORT_MODE_NONE = 0;
    public static final int CONF_REPORT_MODE_SERVER = 1; //서버변경모드
    public static final int CONF_REPORT_MODE_TERM = 2; //단말변경모드

    //config Period Mode 종류
    public static final int CONF_PERIOD_MODE_NONE = 0;
    public static final int CONF_PERIOD_MODE_USE = 1; //사용
    public static final int CONF_PERIOD_MODE_NOT_USE = 2; //사용안함

    //config Data skip Mode 종류
    public static final int CONF_DATA_SKIP_MODE_NONE = 0;
    public static final int CONF_DATA_SKIP_MODE_USE = 1; //사용
    public static final int CONF_DATA_SKIP_MODE_NOT_USE = 2; //사용안함

    //config Debug Mode 종류
    public static final int CONF_DEBUG_MODE_NONE = 0;
    public static final int CONF_DEBUG_MODE_JTAG_UART = 1; //JTAG UART 사용
    public static final int CONF_DEBUG_MODE_METER_PORT = 2; //계량기 포트 사용
    public static final int CONF_DEBUG_MODE_NOT_USE = 3; //사용안함

    //config Reset Mode 종류
    public static final int CONF_BD_RESET_NONE = 0;
    public static final int CONF_BD_RESET_NOW = 1;

    //Server connect req type 종류
    public static final int READ_SERVER_CONNECT_STATE = 0;
    public static final int REQ_SERVER_CONNECT = 1;

    //계량기 유량부 종류
    public static final int METER_FLOW_TYPE_DIGITAL = 0;
    public static final int METER_FLOW_TYPE_ULTRA = 1;

    //Lora Join Mode 종류
    public static final int LORA_CONTROL_JOIN_MODE = 0;
    public static final int LORA_CONTROL_REAL_JOIN_MODE = 1;

    /*
     * Meter data valid
     */
    public static final int METER_VALID_OK = 0; //정상
    public static final int METER_VALID_ERROR = 1; //연결오류
    public static final int METER_VALID_SN_ERROR = 2; //일련번호 오류
    public static final int METER_VALID_VALUE_ERROR = 3; //검침데이터 오류
    public static final int METER_VALID_SUB_ERROR = 4; //하위 검침기 연결오류

    //Meter State 종류
    public static final int METER_STATE_RUNING = 0;
    public static final int METER_STATE_COMPLETE = 1;
    public static final int METER_STATE_MODEM = 2; //모뎀 통신중
    public static final int METER_STATE_SUB_ERROR = 3; //하위장비 오류

    /*
     * Limit Values
     */
    public static final int MAX_METER_PER_TERMINAL = 3; // 하나의 단말기에 연결되는 계량기의 최대 수
    public static final int MAX_METER_CERTI_COMP = 7; // 계량기 인증용 보정 최대 수

    /*
     * Slave Check state
     */
    public static final int SLAVE_STATE_CONNECT = 0; //확인중
    public static final int SLAVE_STATE_SUCCESS = 1; //확인정상
    public static final int SLAVE_STATE_MODEM = 2; //모뎀통신중
    public static final int SLAVE_STATE_ERROR = 3; //통신불량

    /*
     * Smart Meter Change mode
     */
    public static final int SMART_METER_CHANGE_MODE_READ = 0x00;
    public static final int SMART_METER_CHANGE_MODE_WRITE_SN = 0x01;
    public static final int SMART_METER_CHANGE_MODE_WRITE_METER = 0x02;

    /*
     * Smart Meter error result
     */
    public static final int SMART_METER_RESULT_NONE = 0x00;
    public static final int SMART_METER_RESULT_ERROR_SN = 0x01;

    /*
     * Meter Send Cmd type
     */
    public static final int METER_SEND_METER_REQ = 0x5B;
    public static final int METER_SEND_CONF_SET = 0xA0;
    public static final int METER_SEND_METER_VALUE_SET = 0xA1;
    public static final int METER_SEND_CONF_REQ = 0xA2;
    public static final int METER_SEND_CONF_METER_REQ = 0xA3;
    public static final int METER_SEND_VALVE_CONTROL_SET = 0xA4;
    public static final int METER_SEND_CONF_AUTO_START = 0xA5;
    public static final int METER_SEND_LCD_DISPLAY = 0xA6;
    public static final int METER_SEND_CERTI_COMP_SET = 0xA8;
    public static final int METER_SEND_CERTI_COMP_REQ = 0xA9;
    public static final int METER_SEND_ULTRA_CONF_SET = 0xAA;
    public static final int METER_SEND_RESET_REQ = 0xAF;

    /*
     * Meter Recv Cmd type
     */
    public static final int METER_RECV_METER_REPORT = 0x08;
    public static final int METER_RECV_CONF_REPORT = 0xB2;
    public static final int METER_RECV_CONF_METER_REPORT = 0xB3;
    public static final int METER_RECV_VALVE_CONTROL_REPORT = 0xB4;
    public static final int METER_RECV_CONF_AUTO_REPORT = 0xB5;
    public static final int METER_RECV_CERTI_COMP_REPORT = 0xB8;

    //계량기 제조유형
    public static final int METER_MAKER_HT = 1; //하이텍 복갑
    public static final int METER_MAKER_DH = 2; //대한
    public static final int METER_MAKER_KS = 3; //경성
    public static final int METER_MAKER_HT_S = 4; //하이텍 싱글
    public static final int METER_MAKER_DS = 5; //디에스워터
    public static final int METER_MAKER_DR = 6; //디알텍
    public static final int METER_MAKER_HT_U = 7; //하이텍 초음파

    public static final String METER_MAKER_NAME_HT = "HT"; //하이텍 복갑
    public static final String METER_MAKER_NAME_DH = "DH"; //대한
    public static final String METER_MAKER_NAME_KS = "KS"; //경성
    public static final String METER_MAKER_NAME_HT_S = "HT_S"; //하이텍 싱글
    public static final String METER_MAKER_NAME_DS = "DS"; //디에스워터
    public static final String METER_MAKER_NAME_DR = "DR"; //디알텍
    public static final String METER_MAKER_NAME_HT_U = "HT_U"; //하이텍 초음파
}
