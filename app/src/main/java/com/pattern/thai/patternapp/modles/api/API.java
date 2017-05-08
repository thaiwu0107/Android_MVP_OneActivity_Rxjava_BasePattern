package com.pattern.thai.patternapp.modles.api;

import android.content.Context;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pattern.thai.patternapp.definecode.DefineCode.SPCodes.SP_CLIENT_IMIE;
import static com.pattern.thai.patternapp.definecode.DefineCode.SPCodes.SP_TAG_DEFINE;

/**
 * Created by ggttoo44 on 2017/5/8.
 */

public class API {
    private Retrofit m_retrofit;
    private String IMEI;
    private String Operator;
    private static final API ourInstance = new API();
    static API getInstance() {
        return ourInstance;
    }
    private API() {}

    public Retrofit getRetrofit(){
        return m_retrofit;
    }

    public void init(){
        SPUtils m_spUtils = new SPUtils(SP_TAG_DEFINE);
        IMEI = PhoneUtils.getIMEI();
        m_spUtils.put(SP_CLIENT_IMIE,IMEI);
        m_retrofit = new Retrofit.Builder()
                .baseUrl("/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 自定义Converter实现RequestBody到String的转换
     */
    private static class StringConverter implements Converter<ResponseBody, String> {
        private static final StringConverter INSTANCE = new StringConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    /**
     * 用于向Retrofit提供StringConverter
     */
    private static class StringConverterFactory extends Converter.Factory {
        private static final StringConverterFactory INSTANCE = new StringConverterFactory();
        private static StringConverterFactory create() {
            return INSTANCE;
        }

        // 我們只關心實現從ResponseBody到String的轉換，所以其它方法可不覆蓋
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return StringConverter.INSTANCE;
            }
            //其他類型我們不處理，返回null就行
            return null;
        }
    }
}
