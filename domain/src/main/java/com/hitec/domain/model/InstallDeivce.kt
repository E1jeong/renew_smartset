package com.hitec.domain.model

data class InstallDevice(
    val modTypeCd: String? = null,
    val meterDeviceId: String,
    val deviceTypeCd: String? = null, // 장비 타입 NB, GSM
    val consumeHouseNo: String? = null, // 수용가 번호
    val meterMethodCd: String? = null,
    val deviceModelCd: String? = null, // 장비 모델에 따른 코드
    val companyCd: String? = null,
    val meterDeviceNm: String? = null, // 장비 모델명
    val meterDeviceSn: String? = null,
    val consumeStateCd: String? = null, // TODO 수용가 운영인지 준비인지 ?
    val deviceStateCd: String? = null, // 상동인듯
    val meterStateCd1: String? = null,
    val meterStateCd2: String? = null,
    val meterStateCd3: String? = null,
    val installGroupCd: String? = null,
    val uploadResultCd: String? = null,
    val uploadErrorCd: String? = null,
    val barcode: String? = null,
    val cameraSave: String? = null,
    val imageUpload: String? = null,
    val setDt: String? = null, // 설치일자
    val AreaBig: String? = null,
    val AreaBigCd: String? = null,
    val AreaMid: String? = null,
    val AreaMidCd: String? = null,
    val AreaSmall: String? = null,
    val AreaSmallCd: String? = null,
    val setAreaAddr: String? = null, //상세주소
    val setPlaceDesc: String? = null,
    val cdmaNo: String? = null, //IMSI, CTN
    val serverAddr1: String? = null,
    val serverPort1: String? = null,
    val pan: String? = null,
    val panNm: String? = null,
    val nwk: String? = null, //IMEI
    val pnwk: String? = null,
    val mac: String? = null,
    val gpsLatitude: String? = null, //위도
    val gpsLongitude: String? = null, //경도
    val meterBaseTime: String? = null,
    val meterIntervalTime: String? = null, //계량기 검침주기
    val reportIntervalTime: String? = null, //단말기 보고주기
    val meterStoreMonth: String? = null,
    val meterBaseDay: String? = null,
    val meterAlertTime: String? = null,
    val meterPeriodTime: String? = null,
    val activeStartDay: String? = null,
    val activeEndDay: String? = null,
    val activeStartHour: String? = null,
    val activeEndHour: String? = null,
    val terminalType: String? = null, // 단말기 타입
    val firmware: String? = null, // 펌웨어 버전
    val subUpdateInterval: String? = null,
    val subNwkID: String? = null,
    val terminalTypeCd: String? = null, // 단말기 종류
    val consumeHouseNm: String? = null, // 수용가 이름
    val utilityCode: String? = null,
    val waterCompCode: String? = null,
    val wmuManufacturerCode: String? = null,
    val wmuInstallCode: String? = null,
    val cdmaTypeCd: String? = null,
    val firmwareGateway: String? = null,
    val serverConnectionCode: String? = null,
    val concenIP: String? = null,
    val concenGwIP: String? = null,
    val serverURL: String? = null,
    val rfChn: String? = null,
    val hcuId: String? = null,
    val setInitDate: String? = null, // 최초 설치일자
    val masterSn: String? = null,
    val subSn1: String? = null,
    val subSn2: String? = null,
    val subSn3: String? = null,
    val subSn4: String? = null,
    val accountMeterUse1: String? = null,
    val accountMeterUse2: String? = null,
    val accountMeterUse3: String? = null,
    val meterCount: String? = null,
    val meterCd1: String? = null,
    val meterTypeCd1: String? = null,
    val meterPort1: String? = null,
    val meterCompany1: String? = null, // 계량기 제조사
    val meterSn1: String? = null, // 계량기 일련번호
    val meterCurrVal1: String? = null,
    val meterCaliber1: String? = null,
    val metercaliberCd1: String? = null, // 계량기 구경
    val meterDigits1: String? = null,
    val meterCd2: String? = null,
    val meterTypeCd2: String? = null,
    val meterPort2: String? = null,
    val meterCompany2: String? = null,
    val meterSn2: String? = null,
    val meterCurrVal2: String? = null,
    val meterCaliber2: String? = null,
    val metercaliberCd2: String? = null,
    val meterDigits2: String? = null,
    val meterCd3: String? = null,
    val meterTypeCd3: String? = null,
    val meterPort3: String? = null,
    val meterCompany3: String? = null,
    val meterSn3: String? = null,
    val meterCurrVal3: String? = null,
    val meterCaliber3: String? = null,
    val metercaliberCd3: String? = null,
    val meterDigits3: String? = null,
    val communicationTypeCd: String? = null, // 424, lora, nb, gsm 구분
    val telecomTypeCd: String? = null, // 통신사
    val nbServiceCode: String? = null, // 서비스 코드
    val nbCseId: String? = null, // CSE ID
    val nbIccId: String? = null, // USIM ICC ID
    val accountCheckNote: String? = null, // 장비 메모
    val reportRangeTime: String? = null,
    val dataSkipFlag: String? = null,
    val deviceMemo: String? = null,
)