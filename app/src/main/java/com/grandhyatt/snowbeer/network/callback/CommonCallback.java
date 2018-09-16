package com.grandhyatt.snowbeer.network.callback;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 *
 *
 * @author
 * @email
 * @mobile
 * @create 2018/7/20 22:43
 */
public abstract class CommonCallback extends Callback<Result> {

    @Override
    public Result parseNetworkResponse(Response response, int id) throws Exception {

        String strData = response.body().string();
        Result result = new Gson().fromJson(strData, Result.class);

        return result;
    }

}
