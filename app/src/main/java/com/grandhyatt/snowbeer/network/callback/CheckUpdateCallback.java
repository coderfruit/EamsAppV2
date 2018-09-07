package com.grandhyatt.snowbeer.network.callback;

import com.google.gson.Gson;
import com.grandhyatt.snowbeer.network.result.CheckUpdateResult;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 *
 *
 * @author
 * @email
 * @mobile
 * @create 2018/6/1 11:19
 */
public abstract class CheckUpdateCallback extends Callback<CheckUpdateResult> {

    @Override
    public CheckUpdateResult parseNetworkResponse(Response response, int id) throws Exception {

        String strData = response.body().string();
        CheckUpdateResult result = new Gson().fromJson(strData, CheckUpdateResult.class);
        
        return result;
    }

}
