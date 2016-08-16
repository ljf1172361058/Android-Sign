package com.example.application;


import java.util.concurrent.TimeUnit;

import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Application;
import okhttp3.OkHttpClient;

public class MyApplication extends Application{
	
	/**
	 * android应用程序真正入口。
     * 此方法在所有activity，servie，receiver组件之前调用
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//              .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
               .build();

      OkHttpUtils.initClient(okHttpClient);
	}
}
