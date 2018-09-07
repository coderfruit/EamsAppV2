package com.grandhyatt.snowbeer.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.snowbeer.App;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.network.callback.CheckUpdateCallback;
import com.grandhyatt.snowbeer.network.callback.CommonCallback;
import com.grandhyatt.snowbeer.network.callback.GetCustomerCallBack;
import com.grandhyatt.snowbeer.network.callback.GetCustomerListCallBack;
import com.grandhyatt.snowbeer.network.callback.GetOrganizationListCallBack;
import com.grandhyatt.snowbeer.network.callback.GetSecondChkListCallBack;
import com.grandhyatt.snowbeer.network.callback.GetStoreHouseListCallBack;
import com.grandhyatt.snowbeer.network.callback.LoginCallback;
import com.grandhyatt.snowbeer.network.request.CheckUpdateRequest;
import com.grandhyatt.snowbeer.network.request.EditPasswordRequest;
import com.grandhyatt.snowbeer.network.request.GetCustomerRequest;
import com.grandhyatt.snowbeer.network.request.GetSMSCodeRequest;
import com.grandhyatt.snowbeer.network.request.LoginRequest;
import com.grandhyatt.snowbeer.network.request.ResetPasswordRequest;
import com.grandhyatt.snowbeer.network.result.LoginResult;
import com.grandhyatt.snowbeer.utils.JUnitUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.Response;


/**
 * 网络请求工具类(以Async结尾的为异步方法,以Sync结尾的为同步方法)
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:04
 */

public class NetWorkRequestUtils {

    private static final String TAG = NetWorkRequestUtils.class.getSimpleName();

    public final static String DEVICE_TYPE = "android";


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
            strIPAddress = "http://" + strIPAddress + ":" +  strPort + "/EAMS/CRBEAMSService.asmx?op=";
        }else{
            strIPAddress = "http://" + Consts.API_HOST_IP_ADDRESS + ":" + Consts.API_HOST_PORT + "/EAMS/CRBEAMSService.asmx?op=";
        }
        return strIPAddress;
    }

    /**
     *
     * */
    private static HashMap getAuthHttpRequestHeader(Context context) {
        String ts = getTimeStamp();
        String token = getAuthToken(com.grandhyatt.snowbeer.utils.SPUtils.getToken(context),ts);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid",SPUtils.getLastLoginUserID(context));
        hashMap.put("token", token);
        hashMap.put("ts", ts);
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
        return hashMap;
    }

    /**
     *
     * */
    private static HashMap getHttpRequestHeader() {
        String ts = getTimeStamp();
        String token = getToken(ts);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("ts", ts);
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
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

    /**********************************************************************************************/

    /**
     * 创建同步的网络请求,execute方法不传入callback即为同步的请求，返回Response
     */
    public static Response execResponseSync(Context context, String url) throws IOException {
        Response response = OkHttpUtils.get()
                .url(url)
                .tag(context)
                .build()
                .execute();
        return response;
    }

    /**
     * 清空Session
     *
     * @return
     */
    public static void clearSession() {
        CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        if (cookieJar instanceof CookieJarImpl) {
            ((CookieJarImpl) cookieJar).getCookieStore().removeAll();
        }
    }

    /**
     * 取消网络请求
     *
     * @param context 上下文对象
     * @return
     */
    public static void cancelNetWorkRequest(Context context) {
        OkHttpUtils.getInstance().cancelTag(context);
    }

    /**
     * 将文件作为请求体，发送到服务器的异步方法
     *
     * @param context  上下文对象
     * @param file     要上传的文件
     * @param callback
     * @return
     */
    public static void postFileAsync(Context context, File file, StringCallback callback) {
        String url = Consts.API_ATTACTMENT_HOST + "/";
        OkHttpUtils.postFile()
                .url(url)
                .tag(context)
                .file(file)
                .build()
                .execute(callback);
    }


    /**
     * 表单形式上传文件的异步方法
     *
     * @param context  上下文对象
     * @param file     要上传的文件
     * @param callback
     * @return
     */
    public static void uploadFileAsync(Context context, File file, StringCallback callback) {
        String url = Consts.API_ATTACTMENT_HOST + "/";
        uploadFileAsync(context, url, file, callback);
    }

    /**
     * 表单形式上传文件的异步方法
     *
     * @param context  上下文对象
     * @param url      文件上传路径
     * @param file     要上传的文件
     * @param callback
     * @return
     */
    public static void uploadFileAsync(Context context, String url, File file, StringCallback callback) {

        if (null == file || !file.exists()) {
            return;
        }

        url = Consts.API_ATTACTMENT_HOST + "/" + url;

        OkHttpUtils.post()
                .addFile(file.getName(), file.getName(), file)
                .url(url)
                .tag(context)
                .headers(getAuthHttpRequestHeader(context))
                .build()
                .execute(callback);
    }

    /**
     * 表单形式多文件上传的异步方法
     *
     * @param context  上下文对象
     * @param files    要上传的文件
     * @param callback
     * @return
     */
    public static void multiUploadFileAsync(
            Context context, List<File> files, StringCallback callback) {

        String url = Consts.API_ATTACTMENT_HOST + "/";
        multiUploadFileAsync(context, url, files, callback);
    }

    /**
     * 表单形式多文件上传的异步方法
     *
     * @param context  上下文对象
     * @param url      文件上传路径
     * @param files    要上传的文件
     * @param callback
     * @return
     */
    public static void multiUploadFileAsync(
            Context context, String url, List<File> files, StringCallback callback) {

        if (null == files) {
            return;
        }

        url = Consts.API_ATTACTMENT_HOST + "/" + url;

        Map<String, File> fileMap = new HashMap<>();

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).exists()) {
                fileMap.put(files.get(i).getName(), files.get(i));
            }
        }

        OkHttpUtils.post()
                .files("multiFile", fileMap)
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .build()
                .execute(callback);

    }

    /**
     * 下载文件的异步方法
     *
     * @param context  上下文对象
     * @param url      要下载的文件url
     * @param callBack 文件下载回调
     * @return
     */
    public static void downloadFileAsync(
            Context context, String url, FileCallBack callBack) {

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .build()
                .execute(callBack);
    }

    /**
     * 提交Json数据的异步方法
     *
     * @param context  上下文对象
     * @param url      url
     * @param object   上提交的bean
     * @param callback 回调
     */
    public static void postJsonAsync(Context context, String url, Object object, StringCallback callback) {

        OkHttpUtils
                .postString()
                .url(url)
                .tag(context)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(object))
                .build()
                .execute(callback);
    }

    /**
     * 获取图片的异步犯方法
     *
     * @param context  上下文对象
     * @param url      url
     * @param callback 图片下载的回调
     * @return
     */
    public static void getImageAsync(Context context, String url, BitmapCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(callback);
    }


    /**********************************************************************************************/

    /**
     * 找回密码-验证绑定手机
     *
     */
    public static void getResetPasswordSMSCodeAsync(final Context context, GetSMSCodeRequest request, final CommonCallback callback) {
        final String url = getHostUrl() + "Member/backcode";
        final Map<String, String> params = new HashMap<>();
        params.put("mobile",request.getMobile());

        OkHttpUtils.post()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(Result response, int id) {
                        callback.onResponse(response, id);
                    }
                });
    }


    /**
     * 找回密码-设置新密码
     */
    public static void resetPasswordAsync(final Context context, ResetPasswordRequest request, final CommonCallback callback) {
        final String url = getHostUrl() + "Member/backpass";

        final Map<String, String> params = new HashMap<>();
        params.put("mobile",request.getMobile());
        params.put("code",request.getCode());
        params.put("pwd",request.getPwd());

        OkHttpUtils.post()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(Result response, int id) {
                        callback.onResponse(response, id);
                    }
                });
    }


    /***
     * 登录的异步方法
     *
     */
    public static void loginAsync(final Context context, LoginRequest request, final LoginCallback callback) {

        final String url = getHostUrl() + "login";

        //获取http请求身份验证参数
        HashMap<String,String> params = getHttpRequestHeader();
        params.put("strUserCode ",request.getAccount());
        //将用户密码使用md5加密(用户密码+用户密码)
        params.put("strUserPwd",request.getPassword());//StringUtils.getMD5String(request.getPassword() + request.getPassword()));

        OkHttpUtils.post()
                .url(url)
                .tag(context)
                .params(params)
                .build()
                .execute(new LoginCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(LoginResult response, int id) {
                        //登录成功后保存服务器回传的token
                        if(response != null && response.code == Result.RESULT_CODE_SUCCSED){
                            SPUtils.setToken(context,response.getToken());
                        }
                        callback.onResponse(response, id);
                    }
                });
    }

    public static void getUserInfoAsync(final Context context, StringCallback callback) {
        final String url = getHostUrl() + "userId";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.get()
                .url(url)
                .headers(getAuthHttpRequestHeader(context))
                .params(params)
                .tag(context)
                .build()
                .execute(callback);
    }

    /**
     * 注销登录的异步方法
     *
     * @param callback 注销回调
     */
    public static void logoutAsync(final Context context, StringCallback callback) {
        final String url = getHostUrl() + "logout";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.post()
                .url(url)
                .tag(context)
                .headers(getAuthHttpRequestHeader(context))
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 版本检测
     *
     */
    public static void checkUpdateAsync(final Context context, CheckUpdateRequest request, CheckUpdateCallback callback) {
        final String url = getHostUrl() + "CheckUpdate";

        final Map<String, String> params  = getAuthHttpRequestHeader(context);
        params.put("platform",String.valueOf(request.getPlatform()));
        params.put("version",String.valueOf(request.getVersionCode()));

        OkHttpUtils.post()
                .url(url)
                .headers(getAuthHttpRequestHeader(context))
                .params(params)
                .tag(context)
                .build()
                .execute(callback);
    }

    /**
     * 修改密码
     */
    public static void updatePasswordAsync(final Context context, EditPasswordRequest request, final CommonCallback callback) {
        final String url = getHostUrl() + "updatePassword";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        params.put("password",StringUtils.getMD5String(request.getPassword() + request.getPassword()));
        params.put("passwordNew",StringUtils.getMD5String(request.getPasswordNew() + request.getPasswordNew()));


        OkHttpUtils.post()
                .url(url)
                .tag(context)
                .headers(getAuthHttpRequestHeader(context))
                .params(params)
                .build()
                .execute(callback);
    }


    /**
     * 获取组织机构的异步方法
     *
     * @param context
     * @param callback
     */
    public static void getOrganizationListAsync(final Context context, final GetOrganizationListCallBack callback) {

        final String url = getHostUrl() + "getOrganizationList";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 获取组织机构的异步方法
     *
     * @param context
     * @param callback
     */
    public static void getStoreHouseListAsync(final Context context, final GetStoreHouseListCallBack callback) {

        final String url = getHostUrl() + "getStoreHouseList";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 获取组织机构的异步方法
     *
     * @param context
     * @param callback
     */
    public static void getCustomerListAsync(final Context context, final GetCustomerListCallBack callback) {

        final String url = getHostUrl() + "getCustomerList";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 获取组织机构的异步方法
     *
     * @param context
     * @param callback
     */
    public static void getCustomerAsync(final Context context, GetCustomerRequest request, final GetCustomerCallBack callback) {

        final String url = getHostUrl() + "getCustomer";

        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        params.put("customerCode",request.getCustomerCode());

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 获取复检单数据列表的异步方法
     *
     * @param context
     * @param callback
     */
    public static void getSecondChkListAsync(final Context context, final GetSecondChkListCallBack callback) {

        final String url = getHostUrl() + "getSecondChkList";
        //获取http请求身份验证参数
        HashMap<String,String> params = getAuthHttpRequestHeader(context);

        OkHttpUtils.get()
                .url(url)
                .tag(context)
                .headers(getHttpRequestHeader())
                .params(params)
                .build()
                .execute(callback);
    }

}
