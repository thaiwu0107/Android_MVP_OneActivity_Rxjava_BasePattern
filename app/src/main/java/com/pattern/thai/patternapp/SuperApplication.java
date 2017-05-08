package com.pattern.thai.patternapp;

import android.app.Application;
import com.blankj.utilcode.util.Utils;

/**
 * Created by ggttoo44 on 2017/5/8.
 */

public class SuperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());
    }
}
