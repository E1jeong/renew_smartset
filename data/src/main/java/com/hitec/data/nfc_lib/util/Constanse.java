/**
 * <pre>
 * 상수 정의 클래스.
 * </pre>
 */
package com.hitec.data.nfc_lib.util;

public interface Constanse {
    /*
     * APP 버전 정보
     */
    //버전 정보(서버 버전이랑 동일함)
    //Manifest의 Package code에서 수정 100 => 100
    String APP_VERSION = "Hitec/KR-V"; //"Hitec/KR-V100";
    String APP_DATE = "/2024.03.25";

    /*
     * 디버그 관련 변수
     */
    //디버그 로그 출력 여부.
    boolean DEBUG = true;
    boolean DEBUG_FILE = false;

    boolean DEBUG_ALL = false;
    //public final static boolean DEBUG_ALL = true;

    boolean USE_MAIN_MENU_PASSWORD = true;
    //public final static boolean USE_MAIN_MENU_PASSWORD = false;	//yikim test

    //DB 암호화 사용 여부.
    boolean USE_DB_SECURITY = true;
    //public final static boolean USE_DB_SECURITY = false;

    boolean USE_AMI_OVERSEA_ONLY = false; //해외향 전용

    //서버연결 메세지 표시
    boolean USE_HTTP_TEST_LOG = false; //yikim test
    //public final static boolean USE_HTTP_TEST_LOG = true;	//yikim test

    //DB 파일 암호화 사용 여부.
    boolean USE_DB_FILE_PASSWORD = false; //true;
    String DB_FILE_PASSWORD = ""; //사용하지 않을 경우
    //public final static String DB_FILE_PASSWORD = "skyroot";	//사용할 경우

    //비밀번호 암호화 사용 여부.
    boolean USE_SECRET_KEY = true;

    //서버명
    String SERVER_NAME_SEOUL = "서울";

    //LG Platform 체크
    boolean USE_LG_PLATFORM_CHECK = true;
    //public final static boolean USE_LG_PLATFORM_CHECK = false;

    //LG Platform 서버주소
    String NB_PLATFORM_BZ_SERVER_IP = "106.103.250.108"; //상용기
    String NB_PLATFORM_BZ_SERVER_PORT = "5783"; //상용기

    String NB_PLATFORM_DEV_SERVER_IP = "106.103.233.155"; //개발기
    String NB_PLATFORM_DEV_SERVER_PORT = "5783"; //개발기

    String GSM_SERVER_IP = "183.98.244.122"; //gsm
    String GSM_SERVER_PORT = "36921"; //gsm

    //KT 서버주소
    String KT_DEV_SERVER_IP = "112.175.172.106"; //KT NBIoT
    String KT_DEV_SERVER_PORT = "9189"; //KT NBIoT

    String NB_PLATFORM_DEV_SERVICE_CODE = "HTEC"; //개발기 기본 서비스코드

    //VPN 서버 연결 사용여부
    boolean USE_VPN_SERVER_TEST = false; //yikim test
    //public final static boolean USE_VPN_SERVER_TEST = true;	//yikim test

    //설치화면에서 서버로 업로드 기능
    boolean USE_INSTALL_UPLOAD = false;
    //public final static boolean USE_INSTALL_UPLOAD = true;

    //인증 기본 일자
    String DEFAULT_CERTIFICATION_DATE = "20170101";
    int CERTIFICATION_VALID_MAX_DAY = 30; //30일

    /*
     * 안드로이드 설정 관련
     */
    //안드로이드 모델
    int SCREEN_SIZE_MODEL = 1; //갤럭시 노트 2
    int SCREEN_SIZE_1280x720 = 1; //갤럭시 노트 2
    int SCREEN_SIZE_2560x1600 = 2; //갤럭시 노트 10.1

    //화면 회전 상태
    int SCREEN_ORIENTATION_MODE_LANDSCAPE = 0; //가로 모드
    int SCREEN_ORIENTATION_MODE_PORTRAIT = 1; //세로 모드

    //사용자별 동작 설정
    String CONFIG_PROGRAM_MODE_USER = "0";
    String CONFIG_PROGRAM_MODE_DEVELOPER = "1";

    //사용자 레벨
    String USER_LEVEL_METERMAN = "0"; //지자체설치(1)
    String USER_LEVEL_INSTALL = "1"; //협력업체(2)
    String USER_LEVEL_ASINSTALL = "5"; //협력업체(3)
    String USER_LEVEL_MANAGER = "2"; //고객지원(4)
    String USER_LEVEL_PRODUCER = "3"; //공장생산(5)
    String USER_LEVEL_DEVELOPER = "4"; //개발자(6)
    String USER_LEVEL_DEFAULT = USER_LEVEL_ASINSTALL; //설치

    String BLUETOOTH_MAC_ADDRESS_NULL = "02:00:00:00:00:00";

    /*
     * CONFIG SYSINFO 사용 변수
     */
    String APP_DEVELOPER_MODE_STR = "DEV";
    String BACKUP_FILE_MAKE_USE_STR = "USE";

    //yikim 2013.06.18  GPS 기본 위치
    String GPS_DEFAULT_LATITUDE = "37.528585";
    String GPS_DEFAULT_LONGITUDE = "127.168146";
    double DOUBLE_GPS_DEFAULT_LATITUDE = 37.528585;
    double DOUBLE_GPS_DEFAULT_LONGITUDE = 127.168146;
    int DOUBLE_GPS_DEFAULT_DISTANCE = 100; //단위[m] GPS 기본 거리이내인 경우에는 저장이 안된걸로 처리

    String GPS_DEFAULT_LATITUDE_MOBILE1 = "37.498356";
    String GPS_DEFAULT_LONGITUDE_MOBILE1 = "127.121786";

    String GPS_DEFAULT_LATITUDE_PC1 = "37.498470";
    String GPS_DEFAULT_LONGITUDE_PC1 = "127.121750";
    String GPS_DEFAULT_LATITUDE_PC2 = "37.49847";
    String GPS_DEFAULT_LONGITUDE_PC2 = "127.12175";
    String GPS_DEFAULT_LATITUDE_PC3 = "37.498479";
    String GPS_DEFAULT_LONGITUDE_PC3 = "127.121751";

    //MAP 화면 종류
    String CONFIG_MAP_TYPE_NORMAL = "1"; //기본 지도 화면
    String CONFIG_MAP_TYPE_SATELLITE = "2"; //위성 지도 화면
    String CONFIG_MAP_TYPE_DEFAULT = CONFIG_MAP_TYPE_NORMAL; //지도 화면

    //리스트화면에서 지도화면 표시 종류
    String CONFIG_MAP_LIST_ITEM_CURRENT = "1"; //현재위치
    String CONFIG_MAP_LIST_ITEM_ALL = "2"; //전체 장비 표시
    String CONFIG_MAP_LIST_ITEM_DEFAULT =
            CONFIG_MAP_LIST_ITEM_CURRENT;

    int CONFIG_MAP_LIST_ITEM_CHECL_MAX = 2000; //2000개 이상인 경우 아이콘 화면표시 시간이 소요되어 확인창 표시

    //설정 운영 모드 종류
    String CONFIG_SETTING_USER_MODE = "1"; //운영모드
    String CONFIG_SETTING_MANAGER_MODE = "2"; //관리자 모드

    //계량기 수량 모드
    String CONFIG_METER_COUNT_ONE = "1"; //1:1 모드
    String CONFIG_METER_COUNT_MULTI = "2"; //다중 모드
    String CONFIG_METER_COUNT_DEFAULT =
            CONFIG_METER_COUNT_ONE; //기본 모드

    //단말기 설정 모드
    String CONFIG_INSTALL_TERM_MODE_WRTIE = "1"; //수동설정
    String CONFIG_INSTALL_TERM_MODE_SAVE_SW = "2"; //SW 읽기저장
    String CONFIG_INSTALL_TERM_MODE_TOTAL = "3"; //수동설정/SW 읽기저장
    String CONFIG_INSTALL_TERM_MODE_DEFAULT =
            CONFIG_INSTALL_TERM_MODE_TOTAL; //기본 모드

    int NUM_INSTALL_TERM_MODE_WRTIE = 1; //수동설정
    int NUM_INSTALL_TERM_MODE_SAVE_SW = 2; //SW 읽기저장
    int NUM_INSTALL_TERM_MODE_TOTAL = 3; //수동설정/SW 읽기저장
    int NUM_INSTALL_TERM_MODE_DEFAULT =
            NUM_INSTALL_TERM_MODE_TOTAL; //기본 모드

    //Select Use, Not
    String CONFIG_SELECT_USE_NOT = "1"; //사용 안함
    String CONFIG_SELECT_USE = "2"; //사용

    //계량기 유량부 종류
    String CONFIG_METER_FLOW_TYPE_DIGTAL = "0"; //디지털
    String CONFIG_METER_FLOW_TYPE_ULTRA = "1"; //초음파

    //계량기 유량부 종류
    int METER_FLOW_TYPE_DIGITAL = 0; //0 : 디지털
    int METER_FLOW_TYPE_ULTRA = 1; //1 : 초음파
    int METER_FLOW_TYPE_MAX = METER_FLOW_TYPE_ULTRA;

    String CONFIG_PDA_MODE_PAN_ID = "9001";
    String CONFIG_BACKUP_DATA_STORAGE_DAY = "20"; //BKACKUP DATA 보관기일
    String CONFIG_LIST_LIMIT_SIZE = "2000"; ////리스트제한 사이즈

    //좌표 저장모드
    String CONFIG_GPS_SAVE_NOT = "1"; //사용 안함
    String CONFIG_GPS_SAVE_SELECT = "2"; //선택 저장
    String CONFIG_GPS_SAVE_AUTO = "3"; //자동 저장
    String CONFIG_GPS_SAVE_DEFAULT = CONFIG_GPS_SAVE_AUTO; //기본 모드

    int NUM_GPS_SAVE_NOT = 1; //저장 안함
    int NUM_GPS_SAVE_SELECT = 2; //선택 저장
    int NUM_GPS_SAVE_AUTO = 3; //자동 저장
    int NUM_GPS_SAVEE_DEFAULT = NUM_GPS_SAVE_AUTO; //기본 모드

    //GPS 사용모드
    String CONFIG_GPS_USE_NOT = "1"; //저장 안함
    String CONFIG_GPS_USE = "2"; //자동 저장
    String CONFIG_GPS_USE_DEFAULT = CONFIG_GPS_USE; //기본 모드

    //작업자 설치모드
    String CONFIG_INSTALL_MAN_MODE_BEGINNER = "1"; //기본모드
    String CONFIG_INSTALL_MAN_MODE_SENIOR = "2"; //숙련모드
    String CONFIG_INSTALL_MAN_MODE_DEFAULT =
            CONFIG_INSTALL_MAN_MODE_BEGINNER; //기본 모드

    //검침원 설치모드
    String CONFIG_METER_MAN_MODE_BEGINNER = "1"; //기본모드
    String CONFIG_METER_MAN_MODE_PHOTO = "2"; //사진모드
    String CONFIG_METER_MAN_MODE_DEFAULT =
            CONFIG_METER_MAN_MODE_BEGINNER; //기본 모드

    //사진 공통 PICK 설정
    int COM_PICK_FROM_CAMERA = 111;
    int COM_PICK_FROM_ALBUM = 112;

    //사진 업로드 사용모드
    String CONFIG_UPLOAD_IMAGE_USE_NOT = "1"; //사용 안함
    String CONFIG_UPLOAD_IMAGE_USE = "2"; //사용
    String CONFIG_UPLOAD_IMAGE_USE_DEFAULT =
            CONFIG_UPLOAD_IMAGE_USE; //기본 모드

    //사진 저장
    int CONFIG_CAMERA_IMAGE_METER_OLD = 0;
    int CONFIG_CAMERA_IMAGE_METER_NEW = 1;
    int CONFIG_CAMERA_IMAGE_DEVICE_OLD = 2;
    int CONFIG_CAMERA_IMAGE_DEVICE_NEW = 3;
    int CONFIG_CAMERA_IMAGE_PLACE = 4;
    int CONFIG_CAMERA_IMAGE_ADDRESS = 5;
    int CONFIG_CAMERA_IMAGE_AS_BEFORE = 6;
    int CONFIG_CAMERA_IMAGE_AS_AFTER = 7;

    //8장
    int CONFIG_CAMERA_IMAGE_ARRY_SIZE = 8;

    String CONFIG_CAMERA_IMAGE_TERM_DEFAULT = "11111100"; //단말기
    String CONFIG_CAMERA_IMAGE_RC_DEFAULT = "00111100"; //R,C

    //6장
  /*
	public final  static int CONFIG_CAMERA_IMAGE_ARRY_SIZE = 6;
	
	public final  static String CONFIG_CAMERA_IMAGE_TERM_DEFAULT = "111111";	//단말기
	public final  static String CONFIG_CAMERA_IMAGE_RC_DEFAULT = "001111";		//R,C
	*/

    //장비종류
    String SAVE_IMAGE_TYPE_TERM = "1"; //검침기
    String SAVE_IMAGE_TYPE_RC = "2"; //R, C

    //사진 저장 플래그 설정
    int SAVE_CAMERA_IMAGE_METER_OLD = 0;
    int SAVE_CAMERA_IMAGE_METER_NEW = 1;
    int SAVE_CAMERA_IMAGE_DEVICE_OLD = 2;
    int SAVE_CAMERA_IMAGE_DEVICE_NEW = 3;
    int SAVE_CAMERA_IMAGE_PLACE = 4;
    int SAVE_CAMERA_IMAGE_ADDRESS = 5;
    int SAVE_CAMERA_IMAGE_AS_BEFORE = 6;
    int SAVE_CAMERA_IMAGE_AS_AFTER = 7;

    int SAVE_CAMERA_IMAGE_DEVICE_ARRY_SIZE = 8;

    //사진 종류
    String PHOTO_TYPE_CD_METER_OLD = "1";
    String PHOTO_TYPE_CD_METER_NEW = "2";
    String PHOTO_TYPE_CD_DEVICE_OLD = "3";
    String PHOTO_TYPE_CD_DEVICE_NEW = "4";
    String PHOTO_TYPE_CD_PLACE = "5";
    String PHOTO_TYPE_CD_ADDRESS = "6";
    String PHOTO_TYPE_CD_AS_BEFORE = "7";
    String PHOTO_TYPE_CD_AS_AFTER = "8";
    String PHOTO_TYPE_CD_WORK = "9"; //작업사진

    int PHOTO_TYPE_WORK_MAX = 8; //작업사진 수량

    //AS 사용모드
    String CONFIG_AS_USE_NOT = "1"; //저장 안함
    String CONFIG_AS_USE = "2"; //자동 저장
    String CONFIG_AS_USE_DEFAULT = CONFIG_AS_USE_NOT; //기본 모드

    //프로그램 대기모드
    String CONFIG_PROGRAM_WAIT_USE_NOT = "0"; //사용 안함
    String CONFIG_PROGRAM_WAIT_USE = "1"; //사용
    String CONFIG_PROGRAM_WAIT_DEFAULT =
            CONFIG_PROGRAM_WAIT_USE_NOT; //기본 모드

    //프로그램 종료 시간
    String CONFIG_PROGRAM_EXIT_TIME_DEFAULT = "0"; //사용안함

    //MAP Site 종류
    String CONFIG_MAP_SITE_DAUM = "1"; //다음 지도
    String CONFIG_MAP_SITE_GOOGLE = "2"; //구글 지도
    String CONFIG_MAP_SITE_DEFAULT = CONFIG_MAP_SITE_DAUM; //기본 지도 화면

    //지도데이터 사용 선택
    String CONFIG_MAP_DATA_USE_NOT = "1"; //사용 안함
    String CONFIG_MAP_DATA_USE = "2"; //사용
    String CONFIG_MAP_DATA_USE_DEFAULT = CONFIG_MAP_DATA_USE; //기본 모드

    //GPS 트래킹모드 사용
    int GPS_TRACKING_MODE_OFF = 0;
    int GPS_TRACKING_MODE_ON = 1;
    int GPS_TRACKING_MODE_HEAD = 2; //나침반 모드	//스마트폰의 자석센서 오류가 심해 사용하기 어려움

    //지도 포인트 화면 표시 상태
    int MAP_POINT_MODE_CENTER = 0; //중심 좌표 기준으로만 표시
    int MAP_POINT_MODE_TOTAL = 1; //전체 포인트 표시

    //지도 아이템 표시 선택
    int MAP_ITEM_CURRENT_ONLY = 0; //현재 위치만 표시
    int MAP_ITEM_DEVICE_MODE = 1; //현재 위치, 장비 위치 표시
    int MAP_ITEM_PAN_MODE = 2; //현재 위치, PAN 장비 위치 표시
    int MAP_ITEM_LIST_MODE = 3; //현재 위치, 리스트 장비 위치 표시
    int MAP_ITEM_AS_LIST_MODE = 4; //현재 위치, A/S 리스트 장비 위치 표시
    int MAP_ITEM_AS_DEVICE_MODE = 5; //현재 위치, A/S 장비 위치 표시

    //List work codes
    int LIST_WORK_TOTAL = 0; //작업
    int LIST_WORK_SET = 1; //설치작업
    int LIST_WORK_UPLOAD = 2; //전송
    int LIST_WORK_UPLOAD_NOTYET = 3; //미전송

    //지도 activity 선택
    int LIST_ACTIVITY_TERM = 0; //	D_ListTermActivity
    int LIST_ACTIVITY_CONCEN = 1; //	D_ListConcenActivity
    int LIST_ACTIVITY_WMU = 2; //	W_ListTermActivity
    int LIST_ACTIVITY_WCU = 3; //	W_ListConcenActivity
    int LIST_ACTIVITY_NFC = 4; //	N_ListTermActivity

    //카메라 크기 종류
    String CONFIG_CAMEAR_SIZE_TYPE_DEFAULT = "800x600"; //카메라 이미지 저장 크기
    int PHOTO_IMAGE_MAX_WIDTH_4_3 = 800; //	사진 넓이
    int PHOTO_IMAGE_MAX_HEIGHT_4_3 = 600; //	사진 높이
    int PHOTO_IMAGE_MAX_WIDTH_16_9 = 960; //	사진 넓이
    int PHOTO_IMAGE_MAX_HEIGHT_16_9 = 540; //	사진 높이

    int IMAGE_RATE_4_3 = 0; //	이미지 비율 4:3
    int IMAGE_RATE_16_9 = 1; //	이미지 비율 16:9
    int IMAGE_RATE_19_9 = 2; //	이미지 비율 19:9
    int IMAGE_RATE_UNKNOWN = 3; //	이미지 비율 ?:?

    String CONFIG_COMPANY_SELECT_DEFAULT = ""; //업체전체
    String CONFIG_INSTALL_GROUP_DEFAULT = ""; //전체그룹

    //DB download mode
    String DOWNLOAD_DB_TEST_MODE = "0"; //test mode (계량기 제어모드)
    String DOWNLOAD_DB_NORMAL_MODE = "1"; //download mode

    //DB upload Type
    String UPLOAD_TYPE_TOTAL = "0"; //전체 전송
    String UPLOAD_TYPE_ONE = "1"; //1개 전송
    String UPLOAD_TYPE_ONE_PHOTO = "2"; //사진 1개 전송

    //서버 설정
    String SERVER_LOGIN_PASSWORD = "01"; //서버 암호
    String SERVER_DEFAULT_IP = "1.233.95.240"; //기본 서버 주소

    String SERVER_RECORD_ON = "1"; //서버 ID, PW 저장
    String SERVER_RECORD_OFF = "0"; //서버 ID, PW NOT Save

    String CONFIG_NFC_NOT_USE = "0";
    String CONFIG_NFC_USE = "1";
    String CONFIG_NFC_USE_DEFAULT = CONFIG_NFC_USE;

    String CONFIG_NFC_TYPE_NO = "0";
    String CONFIG_NFC_TYPE_A = "1";
    String CONFIG_NFC_TYPE_B = "2";
    String CONFIG_NFC_TYPE_DEFAULT = CONFIG_NFC_TYPE_A;

    //사진전송 리스트
    String CONFIG_PHOTO_LIST_UP_NOT_USE = "0";
    String CONFIG_PHOTO_LIST_UP_USE = "1";
    String CONFIG_PHOTO_LIST_UP_USE_DEFAULT =
            CONFIG_PHOTO_LIST_UP_NOT_USE;

    String SERVER_SITE_DOWN = "1"; //download Site

    String USE_UPDATE_CHROME_OFF = "0";
    String USE_UPDATE_CHROME_ON = "1";

    String CONFIG_NB_SERVICE_CODE_AUTO = "0"; //자동설정
    String CONFIG_NB_SERVICE_CODE_MANUAL = "1"; //수동설정
    String CONFIG_NB_SERVICE_CODE_DEFAULT =
            CONFIG_NB_SERVICE_CODE_AUTO;

    int CONFIG_NB_SERVICE_CODE_AUTO_NUM = 0; //자동설정
    int CONFIG_NB_SERVICE_CODE_MANUAL_NUM = 1; //수동설정

    String CONFIG_NB_SERVER_CONTOL_AUTO = "0"; //자동설정
    String CONFIG_NB_SERVER_CONTOL_MANUAL = "1"; //수동설정
    String CONFIG_NB_SERVER_CONTOL_DEFAULT =
            CONFIG_NB_SERVER_CONTOL_AUTO;

    int CONFIG_NB_SERVER_CONTOL_AUTO_NUM = 0; //자동설정
    int CONFIG_NB_SERVER_CONTOL_MANUAL_NUM = 1; //수동설정

    /*
     * 파일설정
     *
     */

    //folder name
    String SYSTEM_DOWNLOAD_FOLDER = "Download";
    String APP_COM_FOLDER = "hitec/com/file";
    String BACKUP_FOLDER = "hitec/com/file/setbackup";
    String DATABASE_FOLDER = "hitec/com/file/databases";
    String DEBUG_LOG_FOLDER = "hitec/com/file/log";
    String EXCEL_DATA_FOLDER = "hitec/com/file/excel";
    String IMAGE_DATA_FOLDER = "hitec/com/file/image";
    String CONFIG_FOLDER = "hitec/com/file/config";
    String DOWNLOAD_FOLDER = "hitec/com/file/down";
    String PHOTO_DATA_FOLDER = "hitec/com/file/photo";
    String METERING_DATA_FOLDER = "hitec/com/file/metering";
    String LORA_LIST_FOLDER = "hitec/com/file/lora";
    String AS_LIST_FOLDER = "hitec/com/file/as";
    String LORA_CTRL_FOLDER = "hitec/com/lora_control";
    String LORA_CTRL_SET_FOLDER =
            "hitec/com/lora_control/set_list";

    String METER_CONTROL_FOLDER = "hitec/com/smartmeter";
    String METER_CTRL_SET_LIST_FOLDER =
            "hitec/com/smartmeter/set_list";

    //FILE name
    String SMART_SET_NAME = "SmartSet"; //SmartSet
    String SMART_SET_APK_NAME = "SmartSet.apk"; //SmartSet.apk
    String NORMAL_DB_FILE_NAME = "mobile_set"; //"mobile_set.db"
    String AS_DB_FILE_NAME = "mobile_as"; //"mobile_as.db"
    String TEST_DB_FILE_NAME = "mobile_test"; //"mobile_test.db"
    String METER_DB_FILE_NAME = "meter_"; //"meter_20180301.db"
    String LOGIN_LOGO_FILE_NAME = "login_logo.jpg";
    String CONFIG_FILE_NAME = "mobile_config.db";
    String LOCAL_SITE_FILE_NAME = "localsite.cfg";
    String LOLA_LIST_FILE_NAME = "lora_list_"; //lora_list_지자체명
    String LOLA_LIST_FILE_EXT = ".cfg"; //확장자.cfg
    String PHOTO_IMAGE_FILE_EXT = ".jpg"; //확장자.jpg

    //사진 파일명
    String PHOTO_SUB_NAME_METER_OLD = "_meter_old"; //(구)계량기
    String PHOTO_SUB_NAME_METER_NEW = "_meter_new"; //(신)계량기
    String PHOTO_SUB_NAME_DEVICE_OLD = "_device_old"; //(구)장비
    String PHOTO_SUB_NAME_DEVICE_NEW = "_device_new"; //(신)장비
    String PHOTO_SUB_NAME_PLACE = "_place"; //설치배경
    String PHOTO_SUB_NAME_ADDRESS = "_address"; //주소
    String PHOTO_SUB_NAME_AS_BEFORE = "_as_before"; //AS 전 사진
    String PHOTO_SUB_NAME_AS_AFTER = "_as_after"; //As 후 사진
    String PHOTO_SUB_NAME_WORK = "_work"; //작업사진

    /*
     * HTTP 상태 정보
     */
    String HTTP_SERVER_SUCCESS = "0";
    String HTTP_SERVER_ERROR = "-1";

    /*
     * 서버 통신 상태 정보
     */
    String DOWNLOAD_LOGIN = "downloadLogin";
    String DOWNLOAD_PROGRAM = "downloadProgram";
    String DOWNLOAD_LOCAL_SITE = "downloadLocalSite";
    String DOWNLOAD_SUB_AREA = "downloadSubArea";
    String DOWNLOAD_INSTALL_DB = "downloadInstall";
    String DOWNLOAD_AS_DB = "downloadAsDB";
    String DOWNLOAD_DELETE_DB = "downloadDelete";
    String DOWNLOAD_VPN_SERVER = "downloadVpnServer";
    String DOWNLOAD_SECUWAYSSL = "downloadSecuwaySsl";
    String DOWNLOAD_LORA_LIST = "downloadLoraList";

    String SERVER_LOCAL_SITE_NAME = "localsite";
    String UPLOAD_ESSENTIAL_INFO = "uploadEssentialInfo";
    String UPLOAD_INSTALL_INFO = "uploadInstallInfo";
    String UPLOAD_CHANGE_STATE = "uploadChangeState";

    String UPLOAD_AS_STATE = "UploadAsState";
    String UPLOAD_AS_ESSENTIAL = "UploadAsEssential";

    String DEVICE_LAST_DATA = "DeviceLastData";
    String DEVICE_JOIN_DATA = "DeviceJoinData";

    String UPLOAD_IMAGE_DATA = "UploadImageData";
    String DOWNLOAD_IMAGE_DATA = "DownloadImageData";
    String DOWNLOAD_IMAGE_LIST = "DownloadImageList";

    /*
     * 서버전송 결과
     */
    String SERVER_MSG_CODE_OK = "0";
    String SERVER_MSG_CODE_ERROR = "-1";

    String SERVER_RETURN_CODE_OK = "0";
    String SERVER_RETURN_CODE_PASSWORD_ERR = "-1";
    String SERVER_RETURN_CODE_USER_ID_ERR = "-2";
    String SERVER_RETURN_CODE_UNKNOWN_MOBILE_ERR = "-3";
    String SERVER_RETURN_CODE_UNKNOWN_SERVER_ERR = "-4";
    String SERVER_RETURN_CODE_NOT_SUPPORTE_AREA_ERR = "-5"; //지원하지 않는 지역입니다.

    String UPLOAD_RESULT_OK = "0";
    String UPLOAD_RESULT_ERROR = "-1";

    String UPLOAD_ERROR_NO = "0";
    String UPLOAD_ERROR_SN = "-1";
    String UPLOAD_ERROR_PAN_NWK = "-2";
    String UPLOAD_ERROR_SET_DATE = "-3";
    String UPLOAD_ERROR_REGISTER = "-4"; //등록오류

    String AS_UPLOAD_ERROR_NO = "0";
    String AS_UPLOAD_ERROR_REGISTRATION = "-1";

    String IMAGE_UPLOAD_RESULT_OK = "0";
    String IMAGE_UPLOAD_RESULT_ERROR = "-1";

    String IMAGE_DOWNLOAD_RESULT_OK = "0";
    String IMAGE_DOWNLOAD_RESULT_ERROR = "-1";
    String IMAGE_DOWNLOAD_RESULT_FILE_ERROR = "-11";
    String IMAGE_DOWNLOAD_RESULT_SAVE_ERROR = "-12";

    //public final static String AS_SERVER_URL = "http://1.233.95.240:8080/hitecj";

    //Lora list
    //public final static String LORA_LIST_SERVER_URL = "http://1.233.95.240:8080/smart/installDownload/LoraList/";

    /*
     * Download 설치정보에서 A/S 정보 요청 flag
     */
    String DOWN_AS_REQ_INSTALL_ONLY = "0";
    String DOWN_AS_REQ_INSTALL_AS = "1";

    /*
     * Download A/S 정보 요청 flag
     */
    String DOWN_AS_REQ_CODE_ONLY = "0"; //코드
    String DOWN_AS_REQ_CODE_AS = "1"; //코드 + 정보

    /*
     * 업체코드
     */
    String SELECT_COMPANY_TOTAL = "0";
    String SELECT_COMPANY_HITEC = "1";

    String COMPANY_CD_HITEC = "1";
    String COMPANY_CD_YOUBICOM = "2";
    String COMPANY_CD_WIZIT = "3";
    String COMPANY_CD_GAAM = "4";

    /*
     * 서버 접속방법
     */
    String SERVER_CONECTION_TYPE_NORMAL = "0";
    String SERVER_CONECTION_TYPE_VPN = "1";
    String SERVER_CONECTION_TYPE_LOCAL = "2"; //지역모바일

    /*
     * VPN 연결 상태
     */
    int VPN_CONNECT_STATUS_REDAY = 0; //연결 준비
    int VPN_CONNECT_STATUS_SUCCESS = 1; //연결 성공
    int VPN_CONNECT_STATUS_CONNECTING = 2; //연결중

    int VPN_CONNECTION_TIME_OUT = 600; //타임아웃 시간(sec)

    //public final static String SECUWAYSSL_APK_URL = "http://1.233.95.240:8080/smart/installDownloadApp/SecuwaySSLService.apk";

    //검침중계기 프로토콜 종류
    String BT_DEVICE_PROTOCOL_WMU = "00"; //WMU MODE
    String BT_DEVICE_PROTOCOL_AMI = "01"; //AMI MODE
    String BT_DEVICE_PROTOCOL_LORA = "02"; //LORA MODE
    String BT_DEVICE_PROTOCOL_NB = "03"; //NB-IOT MODE
    String BT_DEVICE_PROTOCOL_DISPLAY = "04"; //옥외표시기 MODE
    String BT_DEVICE_PROTOCOL_GSM = "05"; //GSM
    String BT_DEVICE_PROTOCOL_TOTAL = "AA"; //total
    String BT_DEVICE_PROTOCOL_DEFAULT =
            BT_DEVICE_PROTOCOL_AMI; //DEFAULT MODE

    int BT_PROTOCOL_WMU = 0; //WMU MODE
    int BT_PROTOCOL_AMI = 1; //AMI MODE

    //단말기 프로토콜 종류
    String TERM_DEVICE_TYPE_AMI = "03"; //AMI TYPE
    String TERM_DEVICE_TYPE_WMU = "04"; //WMU TYPE

    //검침중계기 주파수대역 종류
    String BT_DEVICE_FREQ_BAND_424 = "0";
    String BT_DEVICE_FREQ_BAND_433 = "1";
    String BT_DEVICE_FREQ_BAND_DEFAULT =
            BT_DEVICE_FREQ_BAND_424; //DEFAULT

    int BT_DEVICE_FREQ_BAND_424_NUM = 0;
    int BT_DEVICE_FREQ_BAND_433_NUM = 1;
    int BT_DEVICE_FREQ_BAND_DEFAULT_NUM =
            BT_DEVICE_FREQ_BAND_424_NUM; //DEFAULT

    /*
     * 카메라 저장 유무
     */
    String CAMERA_SAVE_NO = ""; //카메라 저장 안됨
    String CAMERA_SAVE_OK = "1"; //카메라 1개 저장 정상

    /*
     * 지역 설정
     */

    String PARENT_AREA_BIG_CODE = "42"; //메인 상위코드

    /*
     * Show box button type
     */
    int SHOW_BOX_BTN_STOP = 1; //중지
    int SHOW_BOX_BTN_CLOSE = 2; //CLOSE
    int SHOW_BOX_BTN_OK = 3; //확인
    int SHOW_BOX_BTN_SELECT = 4; //선택
    int SHOW_BOX_BTN_CONTINUE_METER_VALUE = 5; //계량기 검침
    int SHOW_BOX_BTN_CONTINUE_METER_COMPANY = 6; //계량기 제조사
    int SHOW_BOX_BTN_CONTINUE_READ_CONFIG = 7; //정보읽기 요청
    int SHOW_BOX_BTN_CONTINUE_GPS_DATA = 8; //GPS 데이터 수정요청
    int SHOW_BOX_BTN_CONTINUE_PERIOD_DATA = 9; //기간검침 진행
    int SHOW_BOX_BTN_CONTINUE_FW_UPDATE = 10; //펌웨어 업데이트 진행
    int SHOW_BOX_BTN_CONTINUE_QR_CODE = 11; //QR Code 요청

    /*
     * 프로그램 변수 설정
     *
     */
    //프로그램 변수 설정
    long BACK_PRESS_DELAY_TIME = 300; //msec	뒤로가기 버튼 두번 누르기 시간 설정
    int FINISH_APPLICATION_ALERT = 0; //어플 종료 팝업
    int LODDING_PROGRESS_ALERT = 101; //로딩 팝업
    int GPS_MESSAGE_ALERT = 102; //GPS 활성화 경고 팝업

    int LIST_BUTTON_MAX_LEN = 10; //버튼 최대 길이에 따른 문자 표시에 사용

    /*
     * 설정 변수 기본값
     *
     */

    String DEFAULT_MAP_SIZE = "2"; //기본 지도 사이즈
    String DEFAULT_MAP_ROTATE_DISTANCE = "10"; //지도 중심 이동 거리(m)
    String DEFAULT_MEASURE_DISTANCE = "100"; //자동 검침 거리
    String DEFAULT_CONSECUTION_MEASURING_TIME = "500"; //연속검침 주기(msec) 500msec
    String DEFAULT_RE_MEASURING_TIME = "3000"; //재검침 주기(msec) 3000msec
    String DEFAULT_RX_CHANNEL = "25"; //차량 검침 수신 채널
    String DEFAULT_AUTO_MEASURE_COUNT = "10"; //자동 검침 갯수
    String DEFAULT_SELECT_DEVICE_TYPE = "0"; // 장치선택  0: 차량검침중계기, 1: 휴대검침중계기

    /*
     * 다음맵 호출시 사용
     */
    int ACTIVE_DAUMAMP_GPS_SERACH = 0; //좌표검색
    int ACTIVE_DAUMAMP_ADDR_SERACH = 1; //주소검색
    int ACTIVE_DAUMAMP_GPS_NAVI = 2; //좌표로 길찾기
}
