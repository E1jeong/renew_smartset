/**
 * <pre>
 * 상수 정의 클래스.
 * </pre>
 */
package com.hitec.presentation.nfc_lib.util;

public interface ConstDB {
    /*
     * DB 설정 관련
     */
    //DB 파일 암호 설정
    String DB_TYPE_AMI = "1";

    int DB_VERSION_AMI_1 = 100; //DB 시작
    int DB_VERSION_AMI_2 = 102;
    int DB_VERSION_AMI_3 = 103;
    int DB_VERSION_AMI_4 = 104; //S_METER_RESULT 테이블 pan,nwk..추가
    int DB_VERSION_AMI_5 = 105; //INSTALL 테이블 통신방식, 통신사코드..추가
    int DB_VERSION_AMI_6 = 106; //AS_TABLE 테이블 통신방식, 통신사코드추가	//2018
    int DB_VERSION_AMI_7 = 107; //INSTALL 테이블 accountCheckNote 추가		//2019.03.25
    int DB_VERSION_AMI_8 = 108; //INSTALL 테이블 deviceMemo 추가		//2019.03.27
    int DB_VERSION_AMI_9 = 109; //SYSINFO 테이블 dbSecurity 추가, LG Platform 사용 유무 추가		//2019.06.12
    int DB_VERSION_AMI_10 = 110; //AS 테이블 변경	//2020.01.06
    int DB_VERSION_AMI_11 = 111; //INSTALL 테이블 imageUpload,companyCd  추가		//2020.09.10
    int DB_VERSION_AMI_12 = 112; //SYSINFO 테이블 nbServiceCode 추가, INSTALL 테이블 nbCseId,nbIccId, nbServiceCode  추가 	//2021.03.03
    int DB_VERSION_AMI_13 = 113; //INSTALL 테이블 meterDigits1,meterDigits2, meterDigits3  추가 	//2021.07.02
    int DB_VERSION_AMI_14 = 114; //INSTALL 테이블 reportRangTime, dataSkipFlag  추가 	//2021.12.13
    int DB_VERSION_AMI_15 = 115; //mobile_set  AS_TABLE : cdmaNo 추가
    int DB_VERSION_AMI_PREV = DB_VERSION_AMI_14;
    int DB_VERSION_AMI_CURRENT = DB_VERSION_AMI_15;

    int DB_VERSION_CONFIG_1 = 101;
    int DB_VERSION_CONFIG_2 = 102;
    int DB_VERSION_CONFIG_3 = 103; //초음파 계량기 설정 정보 추가 2019.12.04
    int DB_VERSION_CONFIG_PREV = DB_VERSION_CONFIG_2;
    int DB_VERSION_CONFIG_LAST = DB_VERSION_CONFIG_3;

    String DB_TYPE_SMART_METER = "1";

    int DB_VERSION_METER_1 = 101;
    int DB_VERSION_METER_LAST = DB_VERSION_METER_1;

    /*
     * CONFIG 설정 관련
     */
    //Database 종류
    int DATABASE_AMI_MODE = 0; //검침정보
    int DATABASE_AMI_UPGRADE = 1; //검침정보 UPGRADE
    int DATABASE_AS_MODE = 2; //A/S정보

    int DATABASE_AMI_NORMAL = 0; //Normal Mode
    int DATABASE_AMI_TEST = 1; //Test Mode

    int DATABASE_CONFIG_MODE = 10; //Config 정보
    int DATABASE_CONFIG_UPGRADE = 11; //Config 정보 UPGRADE

    int DATABASE_METER_CONTROL_MODE = 20; //Meter control 정보
    int DATABASE_METER_CONTROL_UPGRADE = 21; //Meter control 정보 UPGRADE

    /*
     * DB 정렬
     */
    int DB_SORT_NO = 0;
    int DB_SORT_PAN_NWK = 1;
    int DB_SORT_CONSUME_HOUSE_NO = 2;
    int DB_SORT_CONSUME_HOUSE_NAME = 3;
    int DB_SORT_ADDRESS = 4;
    int DB_SORT_NWK_NUM = 5;
    int DB_SORT_DEVICE_SN = 6;
    int DB_SORT_NOTYET = 7; //미설치
    int DB_SORT_WMU_ID = 8; //WMU ID
    int DB_SORT_DEVICE_TYPE = 9; //장비종류
    int DB_SORT_PROGRESS_STATUS = 10; //진행상태
    int DB_SORT_DEVICE_ID = 11; //장비 ID

    int DB_SORT_AS_DEFAULT = 0;
    int DB_SORT_AS_NAME = 1;
    int DB_SORT_AS_CODE = 2;

    /*
     * DB 설치 상태
     */
    int DB_STATE_TOTAL = 1; //Total
    int DB_STATE_COMPLEITE = 2; //Complete
    int DB_STATE_NOTYET = 3; //Not yet

    /*
     * DB Upload 상태
     */
    int DB_UPLOAD_STATE_TOTAL = 1; //Total
    int DB_UPLOAD_STATE_OK = 2; //
    int DB_UPLOAD_STATE_ERROR = 3; //
    int DB_UPLOAD_STATE_NOT_YET = 4; //미설치

    String DB_UPLOAD_RESULT_NOTYET = "";
    String DB_UPLOAD_RESULT_OK = "0";
    String DB_UPLOAD_RESULT_ERROR = "-1";

    /*
     * DB 장비 운영 상태
     */
    int DB_OPERATION_MODE_TOTAL = 1; //Total
    int DB_OPERATION_MODE_READY = 2; //준비
    int DB_OPERATION_MODE_OPERATION = 3; //운영
    int DB_OPERATION_MODE_MANUAL = 4; //수기
    int DB_OPERATION_MODE_UNREGISTER = 5; //미등록

    String DB_OPERATION_STATE_READY = "1"; //준비
    String DB_OPERATION_STATE_OPERATION = "2"; //운영
    String DB_OPERATION_STATE_MANUAL = "3"; //수기

    /*
     * 장비설정 변경 종류 코드
     */
    String DB_INSTALL_TYPE_INSTALL = "I";
    String DB_INSTALL_TYPE_UPDATE = "U";

    /*
     * 수용가 운영상태 종류 코드
     */
    String DB_CONSUME_STATE_READY = "1"; //준비
    String DB_CONSUME_STATE_OPERATION = "2"; //운영
    String DB_CONSUME_STATE_MANUAL = "3"; //수기

    /*
     * 장비운영상태 종류 코드
     */
    String DB_DEVICE_STATE_READY = "1"; //준비
    String DB_DEVICE_STATE_OPERATION = "2"; //운영
    String DB_DEVICE_STATE_MANUAL = "3"; //수기

    /*
     * 계량기운영상태 종류 코드
     */
    String DB_METER_STATE_READY = "1"; //대기
    String DB_METER_STATE_OPERATION = "2"; //과금
    String DB_METER_STATE_STOP = "3"; //정수
    String DB_METER_STATE_CLOSE = "4"; //폐전

    /*
     * AS설정 변경 종류 코드
     */
    String DB_AS_TYPE_INSTALL = "I";
    String DB_AS_TYPE_UPDATE = "U";

    /*
     * PAN TOTAL
     */
    String DB_TOTAL_PAN = "Total"; //전체 PAN

    /*
     * Active mode
     */

    String DB_ACTIVE_TYPE_AMI_NONE = "0";
    String DB_ACTIVE_TYPE_AMI_AMI = "1";
    String DB_ACTIVE_TYPE_AMI_PDA = "2";
    String DB_ACTIVE_TYPE_AMI_DUAL = "3";
    String DB_ACTIVE_TYPE_AMI_AMI_S = "4";
    String DB_ACTIVE_TYPE_AMI_PDA_S = "5";
    String DB_ACTIVE_TYPE_AMI_AMI_M = "6";
    String DB_ACTIVE_TYPE_AMI_PDA_M = "7";
    String DB_ACTIVE_TYPE_AMI_DUAL_M = "8";
    String DB_ACTIVE_TYPE_AMI_DPLC = "9";

    String DB_ACTIVE_TYPE_AMI_AMI_AUX = "6";

    //계량기 유량부 종류
    int DB_METER_FLOW_TYPE_DIGITAL = 0; //0 : 디지털
    int DB_METER_FLOW_TYPE_ULTRA = 1; //1 : 초음파
}
