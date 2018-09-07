package com.grandhyatt.snowbeer.network.callback;

import com.google.gson.Gson;
import com.grandhyatt.snowbeer.network.result.LoginResult;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 *
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:19
 */
public abstract class LoginCallback extends Callback<LoginResult> {

    @Override
    public LoginResult parseNetworkResponse(Response response, int id) throws Exception {

        String strData = response.body().string();
        LoginResult result = new Gson().fromJson(strData, LoginResult.class);
        
        return result;
    }


}
