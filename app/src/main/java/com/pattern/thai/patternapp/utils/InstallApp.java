package com.pattern.thai.patternapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import java.io.File;

/**
 * Created by ggttoo44 on 2017/5/8.
 */

public class InstallApp {
    public static final String APPLICATION_ID = "com.pattern.thai.patternapp";

    public static void installApp(Context context, File file) {
        if (!FileUtils.isFileExists(file)) return;
        context.startActivity(getInstallAppIntent(file));
    }

    public static Intent getInstallAppIntent(File file) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;

        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(Utils.getContext(), APPLICATION_ID + ".provider", file);
            intent.setDataAndType(contentUri, type);
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
