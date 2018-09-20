package com.grandhyatt.snowbeer.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.snowbeer.App;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.callback.CommonCallback;
import com.grandhyatt.snowbeer.network.callback.LoginCallback;
import com.grandhyatt.snowbeer.network.request.CheckUpdateRequest;
import com.grandhyatt.snowbeer.network.request.EditPasswordRequest;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.request.LoginRequest;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapClient;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.soapNetWork.SoapParams;
import com.grandhyatt.snowbeer.utils.JUnitUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.Arrays;
import java.util.HashMap;
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

}
