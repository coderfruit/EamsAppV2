package com.grandhyatt.snowbeer.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.snowbeer.App;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceBillEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.callback.CommonCallback;
import com.grandhyatt.snowbeer.network.callback.LoginCallback;
import com.grandhyatt.snowbeer.network.request.CheckUpdateRequest;
import com.grandhyatt.snowbeer.network.request.EditPasswordRequest;
import com.grandhyatt.snowbeer.network.request.EquipmentRequest;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.request.LoginRequest;
import com.grandhyatt.snowbeer.network.request.MaintenReportingRequest;
import com.grandhyatt.snowbeer.network.request.RepairmentReportingRequest;
import com.grandhyatt.snowbeer.network.result.InspectBillResult;
import com.grandhyatt.snowbeer.network.result.MaintenanceBillResult;
import com.grandhyatt.snowbeer.network.result.RepairmentEquipmentResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapClient;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.soapNetWork.SoapParams;
import com.grandhyatt.snowbeer.utils.JUnitUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.activity.EquipMgrRepairExSpareCheckActivity;
import com.grandhyatt.snowbeer.view.activity.RepairmentExPlanCheckActivity;
import com.grandhyatt.snowbeer.view.activity.RepairmentExReportActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoapUtils {

	private static final String TAG = NetWorkRequestUtils.class.getSimpleName();

	public final static String DEVICE_TYPE = "android";

	/***********************************************************************************************/
	//url、token相关
	public static String getHostUrl(){

		//判断是否为JUnit运行环境
		if(JUnitUtils.isRunningTest()){
			return Consts.API_HOST_IP_ADDRESS;
		}

		APIHostInfoEntity apiHostInfo = SPUtils.getAPIHostInfo(App.getAppContext());

		String strIPAddress = "";
		String strPort = "";

		if(apiHostInfo != null){
			strIPAddress = apiHostInfo.getHost_url();
			strPort = apiHostInfo.getPort();
		}

		if (!TextUtils.isEmpty(strIPAddress) && (!TextUtils.isEmpty(strPort))) {
			strIPAddress = "http://" + strIPAddress + ":" +  strPort + "/EAMS/CRBEAMSService.asmx";
		}else{
			strIPAddress = "http://" + Consts.API_HOST_IP_ADDRESS + ":" + Consts.API_HOST_PORT + "/EAMS/CRBEAMSService.asmx";
		}
		return strIPAddress.replace(" ","");
	}

	/**
	 *
	 * */
	private static SoapParams getAuthHttpRequestHeader(Context context) {
		String ts = getTimeStamp();
		String token = getAuthToken(SPUtils.getToken(context),ts);

		SoapParams hashMap = new SoapParams();
		hashMap.put("uid",SPUtils.getLastLoginUserID(context));
		hashMap.put("token", token);
		hashMap.put("ts", ts);
		return hashMap;
	}

	/**
	 *
	 * */
	private static SoapParams getHttpRequestHeader() {
		String ts = getTimeStamp();
		String token = getToken(ts);

		SoapParams hashMap = new SoapParams();
		hashMap.put("token", token);
		hashMap.put("ts", ts);
		return hashMap;
	}

	private static String getAuthToken(String serverToken, String timeStamp){
		String token = StringUtils.getMD5String("&token=" + serverToken + "&ts=" + timeStamp + "&APPID=" + Consts.APPID + "&SECRET_KEY=" + Consts.SECRET_KEY);
		return TextUtils.isEmpty(token)?"":token;
	}

	/**
	 * 获取登录前Token
	 * */
	private static String getToken(String timeStamp){
		String token = StringUtils.getMD5String("&ts=" + timeStamp + "&APPID=" + Consts.APPID + "&SECRET_KEY=" + Consts.SECRET_KEY);
		return TextUtils.isEmpty(token)?"":token;
	}

	/**
	 * 获取时间戳
	 * */
	private static String getTimeStamp(){
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * 获取APP平台
	 */
	public static String getAppPlatform() {
		String strData = "android_v1";
		return strData;
	}

	/***********************************************************************************************/
    //辅助方法
	/** 实例话对象. */
	private SoapClient mClient = null;

	/** AbSoapUtil实例. */
	
	private static SoapUtils mAbSoapUtil;

	/**
	 * 单例 获取AbSoapUtil实例.
	 * 
	 * @param context
	 *            the context
	 * @return single instance of AbSoapUtil
	 */
	public static SoapUtils getInstance(Context context) {
		if (null == mAbSoapUtil) {

			mAbSoapUtil = new SoapUtils(context);
		}
		return mAbSoapUtil;
	}

	/**
	 * AbSoapUtil构�?�方�?.
	 * 
	 * @param context
	 *            the context
	 */
	private SoapUtils(Context context) {
		super();
		this.mClient = new SoapClient(context);
	}

	/**
	 * Call.
	 * 
	 * @param url
	 *            the url
	 * @param nameSpace
	 *            the name space
	 * @param methodName
	 *            the method name
	 * @param params
	 *            the params
	 * @param listener
	 *            the listener
	 */
	public void call(String url, String nameSpace, String methodName, SoapParams params, SoapListener listener) {
		mClient.call(url, nameSpace, methodName, params, listener);
	}

	public void call(String methodName, SoapParams params, SoapListener listener) {
		String url = getHostUrl();
		String nameSpace = Consts.API_NAME_SPACE;
		mClient.call(url, nameSpace, methodName, params, listener);
	}

	/**
	 * 描述：设置连接超时时间(第一次请求前设置).
	 * 
	 * @param timeout
	 *            毫秒
	 */
	public void setTimeout(int timeout) {
		mClient.setTimeout(timeout);
	}

	/**
	 * Sets the dot net.
	 *
	 * @param dotNet
	 *            the new dot net
	 */
	public void setDotNet(boolean dotNet) {
		mClient.setDotNet(dotNet);
	}

	/*****************************************************************************/
	/**
	 * List对象转字符串
	 * @param list
	 * @return
	 */
	public static String listToString(List<String> list){
		if(list==null){
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean first = true;
		//第一个前面不拼接","
		for(String string :list) {
			if(first) {
				first=false;
			}else{
				result.append("******");
			}
			result.append(string);
		}
		return result.toString();
	}

	/**
	 * 字符串转List对象
	 * @param strs
	 * @return
	 */
	public static List<String> stringToList(String strs){
		final  String str = "******";
		String strArr[] = strs.split(str);
		return Arrays.asList(str);
	}

	/*******************************************************************************************/
	//业务方法
	/***
	 * 登录的异步方法
	 *
	 */
	public static void loginAsync(final Context context, LoginRequest request, final SoapListener callback) {

		final String url = getHostUrl();
		String methodName = "Login";
		//获取http请求身份验证参数
		SoapParams params = getHttpRequestHeader();
		params.put("strUserCode ",request.getAccount());
		//将用户密码使用md5加密(用户密码+用户密码)
		params.put("strUserPwd", StringUtils.getMD5String(request.getPassword() + request.getPassword()));

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 检查新版本
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void checkUpdateAsync(final Context context, CheckUpdateRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "CheckUpdate";
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("platform",String.valueOf(request.getPlatform()));
		params.put("version",String.valueOf(request.getVersionCode()));

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 修改密码
	 */
	public static void updatePasswordAsync(final Context context, EditPasswordRequest request, final SoapListener callback) {
		final String url = getHostUrl();
		String methodName = "ModifyUserPwd";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("prePwd",request.getPassword());   //StringUtils.getMD5String(request.getPassword() + request.getPassword()));
		params.put("newPwd",request.getPasswordNew());//StringUtils.getMD5String(request.getPasswordNew() + request.getPasswordNew()));

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取数据字典
	 * @param context
	 * @param dicName
	 * @param callback
	 */
	public static void getTextDictoryAsync(final Context context, Consts.EnumTextDictonay dicName, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetTextDictory";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("dicName",dicName.toString());

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 通过条码获取设备
	 * @param context
	 * @param barcode
	 * @param callback
	 */
	public static void getEquipmentByBarcodeAsync(final Context context, String barcode, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentByBarcode";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("barcode",barcode.toString());

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 通过设备ID获取设备
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getEquipmentByIDAsync(final Context context, String equipID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentByID";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID.toString());

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 提交报修信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void submitFaultReportAsync(final Context context, FailureReportingRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "NewFailureReporting";

		String userID = SPUtils.getLastLoginUserID(context);
		String userName = SPUtils.getLastLoginUserName(context);

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",request.getEquipmentID());
		params.put("userName",userName);
		params.put("reportDate",request.getReportDate());
		params.put("failureLevel",request.getFailureLevel());
		params.put("failureDesc",request.getFailureDesc());
		params.put("linkUser",request.getLinkUser());
		params.put("linkMobile",request.getLinkMobile());

		String imgs = listToString(request.getBase64Imgs());
		params.put("failureImgs",imgs);
		params.put("failureVoice",request.getBase64Voice());

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取用户报修信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void getFailureReportsAsync(final Context context, FailureReportingRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetFailureReports";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("rptDateFrom",request.getReportDateFrom());
		params.put("rptDateTo",request.getReportDateTo());
		params.put("status",request.getStatus());
		params.put("equipmentID",request.getEquipmentID());
		params.put("failureLevel",request.getFailureLevel());
		params.put("OperateResult",request.getOperateResult());
		params.put("rptUser",request.getReportUser());
		params.put("linkUser",request.getLinkUser());
		params.put("currentLastIdx",request.getCurrentLastIdx());
		params.put("corpID",request.getCorpID());

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取单个报修信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void getFailureReportAsync(final Context context, FailureReportingRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetFailureReport";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("failureReportID",request.getID());

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/*获取检验记录 */
	public static void getInspectReportAsync(final Context context, String requestID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetInspectReport";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("ReportID",requestID);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/*获取保养记录 */
	public static void getMaintenReportAsync(final Context context, MaintenReportingRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetMaintenReport";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("failureReportID",request.getID());

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/*获取维修记录 */
	public static void getRepairmentReportAsync(final Context context, RepairmentReportingRequest request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetRepairmentReport";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("ReportID",request.getID());

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}
	/**
	 * 获取附件文件数据
	 * @param context
	 * @param fileGuid
	 * @param callback
	 */
	public static void getFileDataAsync(final Context context, String fileGuid, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetFileData";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("guid",fileGuid);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 删除报修
	 * @param context
	 * @param rptID
	 * @param callback
	 */
	public static void removeFailureReportingAsync(final Context context, String rptID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "RemoveFailureReporting";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("failureReportID",rptID);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备保养计划
	 * @param context
	 * @param equipID 设备ID
	 * @param maintenLevel 保养类型级别
	 * @param callback
	 */
	public static void getEquipmentMaintenPlanAsync(final Context context, String equipID,String maintenLevel, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentMaintenancePlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("maintenLevel",maintenLevel);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}
	/**
	 * 获取设备检验计划
	 * @param context
	 * @param equipID 设备ID
	 * @param callback
	 */
	public static void getEquipmentInspectPlanAsync(final Context context, String equipID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentInspectPlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		SoapUtils.getInstance(context).call(methodName,params,callback);
	}
	/**
	 * 获取设备维护计划
	 * @param context
	 * @param equipID 设备ID
	 * @param repairmentLevel 维修级别
	 * @param callback
	 */
	public static void getEquipmentPlanAsync(final Context context, String equipID,String repairmentLevel, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentPlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("repairmentLevel",repairmentLevel);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备备件信息
	 * @param context
	 * @param equipID 设备ID
	 * @param callback
	 */
	public static void getEquipmentSparesAsync(final Context context, String equipID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentSpares";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);


		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维修可用备件库存信息
	 * @param context
	 * @param equipID
	 * @param deptID
	 * @param spareContent
	 * @param callback
	 */
	public static void getEquipmentSparesStoreInfo(final Context context, String equipID, String deptID,String spareContent, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentSparesStoreInfo";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("deptID",deptID);
		params.put("spareContent",spareContent);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维修可用备件库存信息
	 * @param context
	 * @param equipID
	 * @param materialContent
	 * @param callback
	 */
	public static void getEquipmentMaterialStoreInfo(final Context context, String equipID,String materialContent, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentMaterialStoreInfo";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);

		params.put("materialContent",materialContent);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}
	/**
	 * 获取组织机构下的部门信息
	 * @param context
	 * @param corporationID
	 * @param callback
	 */
	public static void getDepartment(final Context context, String corporationID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetDepartment";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corporationID",corporationID);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}


	/**
	 * 提交维修信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void submitNewEquipReparimentRepairAsync(final Context context, RepairmentEquipmentResult request, ArrayList<String> _CheckPlanIDList, final SoapListener callback)
	{
        final String url = getHostUrl();
        String methodName = "NewEquipReparimentRepair";

        //String userID = SPUtils.getLastLoginUserID(context);
        String userName = SPUtils.getLastLoginUserName(context);

        //获取http请求身份验证参数
        SoapParams params = getAuthHttpRequestHeader(context);

        params.put("userName", userName);
        RepairmentBillEntity rparbill = request.getRepairmentBillData();
        Gson gson1 = new Gson();

        String jsonrepa = gson1.toJson(rparbill, RepairmentBillEntity.class);
        params.put("repariment", jsonrepa);
        if (_CheckPlanIDList != null && _CheckPlanIDList.size()>0 ) {
            String jsonPlan = gson1.toJson(_CheckPlanIDList);
            params.put("listPlanID", jsonPlan);
        }
        List<EquipmentUseSpareEntity> listeuse = request.getSpareInEquipmentData();
        String jsonEquiSpare = gson1.toJson(listeuse);
        params.put("ListSpareEqer", jsonEquiSpare);
//		String[] arrString = (String[])list.toArray(new String[list.size()]) ;
        //params.put("listPlanID",(String [] )_CheckPlanIDList.toArray(new String[_CheckPlanIDList.size()]));

//		params.put("reportDate",request.getReportDate());
//		params.put("failureLevel",request.getFailureLevel());
//		params.put("failureDesc",request.getFailureDesc());
//		params.put("linkUser",request.getLinkUser());
//		params.put("linkMobile",request.getLinkMobile());
//
//		String imgs = listToString(request.getBase64Imgs());
//		params.put("failureImgs",imgs);
//		params.put("failureVoice",request.getBase64Voice());

        SoapUtils.getInstance(context).call(methodName, params, callback);
    }

	/**
	 * 提交保养信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void submitNewEquipMaintenanceRepairAsync(final Context context, MaintenanceBillResult request, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "NewEquipMaintenanceRepair";

		//String userID = SPUtils.getLastLoginUserID(context);
		String userName = SPUtils.getLastLoginUserName(context);

		//获取http请求身份验证参数
		SoapParams params = getAuthHttpRequestHeader(context);

		params.put("userName", userName);
		MaintenanceBillEntity rparbill = request.getData();
		Gson gson1 = new Gson();

		String jsonrepa = gson1.toJson(rparbill, MaintenanceBillEntity.class);
		params.put("mainten", jsonrepa);

		List<MaintenanceItemEntity> rparbillitem = request.getdateItemList();

		String jsonrepaitem = gson1.toJson(rparbillitem);
		params.put("maintenItem", jsonrepaitem);
		SoapUtils.getInstance(context).call(methodName, params, callback);
	}

	/**
	 * 提交检验信息
	 * @param context

	 * @param callback
	 */
	public static void submitNewEquipInspectRepairAsync(final Context context, InspectBillResult res, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "NewEquipInspectRepair";

		//String userID = SPUtils.getLastLoginUserID(context);
		String userName = SPUtils.getLastLoginUserName(context);

		//获取http请求身份验证参数
		SoapParams params = getAuthHttpRequestHeader(context);

		params.put("userName", userName);
		InspectBillEntity rparbill = res.getData();
		Gson gson1 = new Gson();

		String jsonrepa = gson1.toJson(rparbill, InspectBillEntity.class);
		params.put("inspect", jsonrepa);


		SoapUtils.getInstance(context).call(methodName, params, callback);
	}


	/**
	 * 获取预警消息条数
	 * @param context
	 * @param corpID
	 * @param isGetEpRep 是否获取设备维修计划提醒
	 * @param isGetEpMan 是否获取设备保养计划提醒
	 * @param isGetEpIns 是否获取设备检验计划提醒
	 * @param isGetSpRep 是否获取备件更换提醒
	 * @param isGetRepEx 是否获取设备外委维修提醒
	 * @param isGetBdRep 是否获取建筑物维修提醒
	 * @param isGetAsyByTm 是否获取化学仪器按使用次数提醒
	 * @param isGetAsyByCy 是否获取化学仪器按使用周期提醒
	 * @param isGetAsyRep 是否获取化学仪器按维修计划提醒
	 * @param isGetRptMsgCnt 是否获取待处理报修条数
	 * @param callback
	 */
	public static void getWarningInfoCount(final Context context, String corpID,
          boolean isGetEpRep,boolean isGetEpMan,boolean isGetEpIns,boolean isGetSpRep,boolean isGetRepEx,
		  boolean isGetBdRep,boolean isGetAsyByTm,boolean isGetAsyByCy,boolean isGetAsyRep,
										   boolean isGetRptMsgCnt,
		  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetWarningInfoCount";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("isGetEpRep",String.valueOf(isGetEpRep));
		params.put("isGetEpMan",String.valueOf(isGetEpMan));
		params.put("isGetEpIns",String.valueOf(isGetEpIns));
		params.put("isGetSpRep",String.valueOf(isGetSpRep));
		params.put("isGetRepEx",String.valueOf(isGetRepEx));
		params.put("isGetBdRep",String.valueOf(isGetBdRep));
		params.put("isGetAsyByTm",String.valueOf(isGetAsyByTm));
		params.put("isGetAsyByCy",String.valueOf(isGetAsyByCy));
		params.put("isGetAsyRep",String.valueOf(isGetAsyRep));
		params.put("isGetRptMsgCnt",String.valueOf(isGetRptMsgCnt));

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	//---------------------------------
	//获取维修保养检验提醒
	//---------------------------------
	/**
	 * 获取设备维护计划
	 * @param context
	 * @param corpID 组织机构ID
	 * @param currentLastIdx 当前行序号
	 * @param callback
	 */
	public static void getRepairmentPlan(final Context context, String corpID, String currentLastIdx, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetRepairmentPlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维护计划
	 * @param context
	 * @param corpID 组织机构ID
	 * @param currentLastIdx 当前行序号
	 * @param callback
	 */
	public static void getMaintenancePlan(final Context context, String corpID, String currentLastIdx, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetMaintenancePlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维护计划
	 * @param context
	 * @param corpID 组织机构ID
	 * @param currentLastIdx 当前行序号
	 * @param callback
	 */
	public static void getInspectionPlan(final Context context, String corpID, String currentLastIdx, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetInspectionPlan";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维护计划
	 * @param context
	 * @param corpID 组织机构ID
	 * @param currentLastIdx 当前行序号
	 * @param callback
	 */
	public static void getSpareReplaceInfo(final Context context, String corpID, String currentLastIdx, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetSpareReplaceInfo";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维护计划
	 * @param context
	 * @param corpID 组织机构ID
	 * @param currentLastIdx 当前行序号
	 * @param callback
	 */
	public static void getRepairmentPlanEx(final Context context, String corpID, String currentLastIdx, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetRepairmentPlanEx";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",corpID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getWarningInfo_Equip(final Context context, String equipID, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetWarningInfo_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取设备信息
	 * @param context
	 * @param request
	 * @param callback
	 */
	public static void getEquipments(final Context context, EquipmentRequest request ,final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipments";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("corpID",request.getCorpID());
		params.put("deptID",request.getDeptID());
		params.put("useState",request.getUseState());
		params.put("equipTypeID",request.getEquipTypeID());
		params.put("assetTypeID",request.getAssetTypeID());
		params.put("currentLastIdx",request.getCurrentLastIdx());

		params.put("equipinfo",request.getEquipInfo());
		params.put("location",request.getLocation());
		params.put("keeper",request.getKeeper());
		params.put("manu",request.getManu());

		SoapUtils.getInstance(context).call(methodName, params, callback);
	}

	/**
	 * 获取设备类型
	 * @param context
	 * @param callback
	 */
	public static void getEquipmentType(final Context context,final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetEquipmentType";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);

		SoapUtils.getInstance(context).call(methodName, params, callback);
	}

	/**
	 * 获取化学仪器使用事由列表
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getAssayEquipUseReason(final Context context,String equipID ,final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetAssayEquipUseReason";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);

		SoapUtils.getInstance(context).call(methodName, params, callback);
	}

	/**
	 * 添加化学仪器使用记录
	 * @param context
	 * @param equipID
	 * @param useReason
	 * @param callback
	 */
	public static void addAssayEquipUseRecordAsync(final Context context, String equipID, String useReason, final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "AddAssayEquipUseRecord";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("useReason",useReason);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 修改报修状态
	 * @param context
	 * @param failureReportID
	 * @param status 处理状态(待审核，待处理，已处理)
	 * @param OperateResult 处理结果(已关闭、已维修、已保养、已检验)
	 * @param OperateID 操作ID可为空(维修主表ID、保养主表ID、检验主表ID)
	 * @param OperateDesc 操作描述，或记录处理的单号（维修主表BillNO、保养主表BillNO、检验主表BillNO）
	 * @param callback
	 */
	public static void modifyFailureReportingAsync(final Context context, String failureReportID, String status, String OperateResult, String OperateID, String OperateDesc,  String remark, final SoapListener callback){

		final String url = getHostUrl();
		String methodName = "ModifyFailureReporting";

		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("failureReportID",failureReportID);
		params.put("status",status);
		params.put("OperateResult",OperateResult);
		params.put("OperateID",OperateID);
		params.put("OperateDesc",OperateDesc);
		params.put("remark",remark);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取设备维修预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getRepairmentPlan_Equip(final Context context, String equipID, String currentLastIdx,  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetRepairmentPlan_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备保养预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getMaintenancePlan_Equip(final Context context, String equipID, String currentLastIdx,  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetMaintenancePlan_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备检验预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getInspectionPlan_Equip(final Context context, String equipID, String currentLastIdx,  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetInspectionPlan_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备备件更换预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getSpareReplaceInfo_Equip(final Context context, String equipID, String currentLastIdx,  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetSpareReplaceInfo_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备外委维修预警消息条数
	 * @param context
	 * @param equipID
	 * @param callback
	 */
	public static void getRepairmentPlanEx_Equip(final Context context, String equipID, String currentLastIdx,  final SoapListener callback)
	{
		final String url = getHostUrl();
		String methodName = "GetRepairmentPlanEx_Equip";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 添加设备巡检记录
	 * @param context
	 * @param equipID
	 * @param routingUserID
	 * @param routingStatus
	 * @param operateType
	 * @param operateID
	 * @param operateBillNO
	 * @param callback
	 */
	public static void newEquipmentRoutingInspect(final Context context, String equipID, String routingUserID, String routingStatus, String operateType,
												  String operateID,String operateBillNO,  final SoapListener callback){
		final String url = getHostUrl();
		String methodName = "NewEquipmentRoutingInspect";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("routingUserID",routingUserID);
		params.put("routingStatus",routingStatus);
		params.put("operateType",operateType);
		params.put("operateID",operateID);
		params.put("operateBillNO",operateBillNO);

		SoapUtils.getInstance(context).call(methodName,params,callback);
	}

	/**
	 * 获取设备维修记录
	 * @param context
	 * @param equipID
	 * @param currentLastIdx
	 * @param callback
	 */
	public static void getEquipRepairBills(final Context context, String equipID, String currentLastIdx,  final SoapListener callback){

		final String url = getHostUrl();
		String methodName = "GetEquipRepairBills";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取设备保养记录
	 * @param context
	 * @param equipID
	 * @param currentLastIdx
	 * @param callback
	 */
	public static void getEquipMaintenBills(final Context context, String equipID, String currentLastIdx,  final SoapListener callback){

		final String url = getHostUrl();
		String methodName = "GetEquipMaintenBills";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取设备检验记录
	 * @param context
	 * @param equipID
	 * @param currentLastIdx
	 * @param callback
	 */
	public static void getEquipInspectionBills(final Context context, String equipID, String currentLastIdx,  final SoapListener callback){

		final String url = getHostUrl();
		String methodName = "GetEquipInspectionBills";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}

	/**
	 * 获取外委设备维修记录
	 * @param context
	 * @param equipID
	 * @param currentLastIdx
	 * @param callback
	 */
	public static void getEquipRepairExBills(final Context context, String equipID, String currentLastIdx,  final SoapListener callback){

		final String url = getHostUrl();
		String methodName = "GetEquipRepairExBills";
		//获取http请求身份验证参数
		final SoapParams params  = getAuthHttpRequestHeader(context);
		params.put("equipID",equipID);
		params.put("currentLastIdx",currentLastIdx);

		SoapUtils.getInstance(context).call(methodName,params,callback);

	}


	public static void getEquipmentSparesDeptAsync(EquipMgrRepairExSpareCheckActivity equipMgrRepairExSpareCheckActivity, String equipID, String deptID, String spareContent, SoapListener callback) {
	}

	public static void getEquipmentExPlanAsync(RepairmentExPlanCheckActivity repairmentExPlanCheckActivity, String equipmentID, SoapListener callback) {
	}

	public static void submitNewEquipReparimentExRepairAsync(RepairmentExReportActivity repairmentExReportActivity, String faultDesc, RepairmentEquipmentResult request, ArrayList<String> checkPlanIDList, SoapListener callback) {
	}
}
