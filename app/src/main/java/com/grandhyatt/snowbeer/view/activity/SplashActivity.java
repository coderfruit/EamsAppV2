package com.grandhyatt.snowbeer.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.snowbeer.MainActivity;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.LoginCallback;
import com.grandhyatt.snowbeer.network.request.LoginRequest;
import com.grandhyatt.snowbeer.network.result.LoginResult;
import com.grandhyatt.snowbeer.utils.PackageUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SplashActivity extends Activity {

    @BindView(R.id.iv_entry)
    ImageView mIVEntry;

    @BindView(R.id.img_dxkjlogo)
    ImageView img_dxkjlogo;

    private static final int[] Imgs = {
            R.drawable.snow4, R.drawable.bg_splash_02,
            R.drawable.bg_splash_03, R.drawable.bg_splash_04
    };

//    private static final int[] Imgs = {
//            R.drawable.bg_splash
//    };

    private static final int ANIM_TIME = 3000;
    private static final float SCALE_END = 1.25F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        startMainActivity();
    }

    private void startMainActivity() {
//        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
//        Glide.with(SplashActivity.this).load(Imgs[random.nextInt(Imgs.length)]).into(mIVEntry);
        Glide.with(SplashActivity.this).load(Imgs[0]).into(mIVEntry);
        img_dxkjlogo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dxkjlogo));

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {

                    @Override
                    public void call(Long aLong) {
                        startAnim();
                    }
                });
    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                boolean isFirst = SPUtils.getGuided(SplashActivity.this, PackageUtils.getPackageVersionCode(SplashActivity.this));
                if (isFirst) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    finish();
                } else {
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    autoLogin();
                }

            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void autoLogin() {

        final String userName = SPUtils.getLastLoginUserName(SplashActivity.this);
        final String password = SPUtils.getLastLoginUserPassword(SplashActivity.this);

        LoginRequest request = new LoginRequest();
        request.setAccount(userName);
        request.setPassword(password);

        NetWorkRequestUtils.loginAsync(SplashActivity.this, request, new LoginCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                SplashActivity.this.overridePendingTransition(com.grandhyatt.commonlib.R.anim.base_slide_right_in, com.grandhyatt.commonlib.R.anim.base_slide_remain);
                SplashActivity.this.finish();
            }

            @Override
            public void onResponse(LoginResult response, int id) {
                if(null == response || response.code != Result.RESULT_CODE_SUCCSED){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                    return;
                }

                SPUtils.setLastLoginUserName(SplashActivity.this,userName);
                SPUtils.setLastLoginUserPassword(SplashActivity.this,password);
                SPUtils.setLastLoginUserID(SplashActivity.this,String.valueOf(response.getData().getID()));


                IntentUtil.newIntent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.finish();

                finish();
            }
        });
    }
}
