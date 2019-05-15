package com.zhihu.matisse.sample;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.heiko.matisser.Responsibility;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * UcopRRRR
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class UcopRRRR extends Responsibility {
    @Override
    public void handleRequest(String request, Activity activity) {
        File file_temp = new File(getCachePath(activity), "ucrop_temp.jpeg");
        Uri targetUri = Uri.fromFile(file_temp);
        Uri resultUri = Uri.fromFile(new File(request));
        UCrop.of(resultUri, targetUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(648, 1152)
                .start(activity);
    }

    /**
     * 获取app缓存路径
     *
     * @param context
     * @return
     */
    public String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
