package com.zhihu.matisse.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.heiko.matisser.Matter;
import com.heiko.matisser.Transactor;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * UcopRRRR
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class UcopTransactor extends Transactor {
    public static final int BASE_REQUEST_CODE = 35960;
    private Map<Integer, Matter> matters = new HashMap<>();
    private Map<Integer, String> targetPaths = new HashMap<>();

    @Override
    public void handle(Matter matter, Activity activity) {
        Log.i("OnActivityResult", "handleRequest>>> request:" + matter.getRequest());
        File requestFile = new File(matter.getRequest());
        File targetFile = new File(getCachePath(activity), matter.getPosition() + requestFile.getName());
        Uri resultUri = Uri.fromFile(requestFile);
        Uri targetUri = Uri.fromFile(targetFile);
        matters.put(matter.getPosition(), matter);
        targetPaths.put(matter.getPosition(), targetFile.getPath());
        UCrop.of(resultUri, targetUri)
                .withAspectRatio(9, 16)
                .withMaxResultSize(648, 1152)
                .start(activity, BASE_REQUEST_CODE + matter.getPosition());
    }

    @Override
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.i("OnActivityResult", "onActivityResult>>> requestCode:" + requestCode);
        int position = requestCode - BASE_REQUEST_CODE;
        if (position >= 0 && (position) <= 9) {
            Matter matter = matters.get(position);
            if (resultCode == Activity.RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                Log.i("OnActivityResult", "裁剪成功:" + resultUri.toString());
                String targetPath = targetPaths.get(position);
                matter.setRequest(targetPath);
                getNext().handle(matter, activity);
                return true;
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                Log.e("OnActivityResult", "裁剪失败:" + cropError.getMessage());
                getNext().handle(matter, activity);
                return true;
            } else {
                return super.onActivityResult(activity, requestCode, resultCode, data);
            }
        }
        return false;
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
