package com.youyudj.leveling.chat.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.youyudj.leveling.chat.db.base.BaseManager;
import com.youyudj.leveling.common.SystemLogHelper;
import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.new_chat.ChatBaseActivity;
import com.youyudj.leveling.new_chat.Const;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/**
 * Created by Mao Jiqing on 2016/9/28.
 */
public class ChatApplication extends Application {

    static ChatApplication _instance = null;

    public static ChatApplication getInstance() {
        return _instance;
    }

    public ChatApplication(){
        _instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseManager.initOpenHelper(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
        InitializationConfig config = InitializationConfig.newBuilder(this)
                .connectionTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .build();
        NoHttp.initialize(config);

        //初始化Fresco
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());
        ImagePipelineConfig configs = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(listeners)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this, configs);
        Thread.setDefaultUncaughtExceptionHandler(
                new UncaughtExceptionHandler(this)
        );
    }

    public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
        private final Context myContext;

        public UncaughtExceptionHandler(Context context) {
            myContext = context;
        }

        public void uncaughtException(Thread thread, Throwable exception) {
            SystemLogHelper.Error(exception);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
