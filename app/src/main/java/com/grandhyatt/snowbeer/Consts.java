package com.grandhyatt.snowbeer;

/**
 * 系统常量定义类 此类内仅放常量定义，请不要放其它逻辑代码
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:55
 */
public class Consts {

    /**
     * 应用程序APPID
     * */
    public static final String APPID = "91639425";

    /**
     * 应用程序SECRET_KEY
     * */
    public static final String SECRET_KEY = "x0hd4AwHMCB04auMfLU7FOV06leY27hV";

    /**
     * 应用程序接口服务器IP地址
     * */
    public static final String API_HOST_IP_ADDRESS = "pda.crbln.com";

    /**
     * web服务命名空间
     */
    public static final String API_NAME_SPACE = "http://tempuri.org/";
    /**
     * 应用程序接口服务器端口
     * */
    public static final String API_HOST_PORT = "9900";

    /**
     * 应用程序接口地址
     * */
    public final static String API_SERVICE_HOST = "http://" + API_HOST_IP_ADDRESS + ":" + API_HOST_PORT + "/EAMS/CRBEAMSService.asmx";

    /**
     * 应用程序附件服务器IP地址
     * */
    public static final String API_ATTACTMENT_IP_ADDRESS = "pda.crbln.com";

    /**
     * 应用程序附件服务器端口
     * */
    public static final String API_ATTACTMENT_PORT = "9900";

    /**
     * 应用程序附件服务器地址
     * */
    public final static String API_ATTACTMENT_HOST = "http://" + API_ATTACTMENT_IP_ADDRESS + ":" + API_ATTACTMENT_PORT + "/attactment/";

    /**
     * 相机扫码回调code
     */
    public static final int CAMERA_BARCODE_SCAN = 49374;
    /**
     * 数据字典
     */
    public enum EnumTextDictonay {
        EquipmentClass,	//设备标识
        EquipmentStatus,	//设备使用状况
        GetMode,	//设备购置方式
        RepairmentLevel,	//设备维修级别
        MaintenanceLevel,	//设备保养级别
        InspectionItem,	//设备检验项目
        InspectionMode,	//设备检验方式
        InspectionResult,	//设备检验结论
        FaultClass,	//设备故障类别
        Unit,	//计量单位
        MaterialClass,	//机物料分类
        FailureReportingStatus,	//报修状态
        FailureReportingLevel,	//报修级别
        FailureReportingResult,	//报修处理状态
        FailureReportingDesc,	//报修描述

    }


}
