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
import java.util.HashMap;
import java.util.Map;

/**
 * UcopRRRR
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class UcopRRRR extends Responsibility {
    public static final int BASE_REQUEST_CODE = 35960;
    private Map<Integer, String> resultPaths = new HashMap<>();
    private Map<Integer, String> targetPaths = new HashMap<>();
    private Map<Integer, String> types = new HashMap<>();

    @Override
    public void handleRequest(String type, int position, String request, Activity activity) {
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.dismiss();
            }
        }, 1000);*/
        Log.i("OnActivityResult", "handleRequest>>> request:" + request);
        File requestFile = new File(request);
        File targetFile = new File(getCachePath(activity), position + requestFile.getName());
        Uri resultUri = Uri.fromFile(requestFile);
        Uri targetUri = Uri.fromFile(targetFile);
        resultPaths.put(position, request);
        targetPaths.put(position, targetFile.getPath());
        types.put(position, type);
        UCrop.of(resultUri, targetUri)
                .withAspectRatio(9, 16)
                .withMaxResultSize(648, 1152)
                .start(activity, BASE_REQUEST_CODE + position);
    }

    @Override
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.i("OnActivityResult", "onActivityResult>>> requestCode:" + requestCode);
        int position = requestCode - BASE_REQUEST_CODE;
        if (position >= 0 && (position) <= 9) {
            String type = types.get(position);
            if (resultCode == Activity.RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                Log.i("OnActivityResult", "裁剪成功:" + resultUri.toString());
                String targetPath = targetPaths.get(position);
                getNext().handleRequest(type, position, targetPath, activity);
                return true;
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                Log.e("OnActivityResult", "裁剪失败:" + cropError.getMessage());
                String resultPath = resultPaths.get(position);
                getNext().handleRequest(type, position, resultPath, activity);
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
