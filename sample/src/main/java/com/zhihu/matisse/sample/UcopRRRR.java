package com.zhihu.matisse.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

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
    private String resultPath;
    private String targetPath;

    @Override
    public void handleRequest(String request, Activity activity) {
        File file_temp = new File(getCachePath(activity), "ucrop_temp.jpeg");
        Uri resultUri = Uri.fromFile(new File(request));
        Uri targetUri = Uri.fromFile(file_temp);
        resultPath = request;
        targetPath = file_temp.getPath();
        UCrop.of(resultUri, targetUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(648, 1152)
                .start(activity);
    }

    @Override
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Log.i("OnActivityResult", "裁剪成功:" + resultUri.toString());
            getNext().handleRequest(targetPath, activity);
            return true;
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("OnActivityResult", "裁剪失败:" + cropError.getMessage());
            getNext().handleRequest(resultPath, activity);
            return true;
        } else {
            return super.onActivityResult(activity, requestCode, resultCode, data);
        }
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
