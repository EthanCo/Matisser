package com.zhihu.matisse.sample;


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.heiko.matisser.Matter;
import com.heiko.matisser.Transactor;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * LubanRRRR
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class LubanTransactor extends Transactor {

    @Override
    public void handle(final Matter matter, final Activity activity) {
        final File file = new File(matter.getRequest());
        Log.i("OnActivityResult ", "file.size:" + file.length());
        Luban.with(activity)
                .load(file)
                .ignoreBy(100)
                .setTargetDir(activity.getExternalCacheDir().toString())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        Log.i("OnActivityResult", "Luban压缩成功:" + file.toString() + " file.size:" + file.length());
                        matter.setRequest(file.getPath());
                        getNext().handle(matter, activity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用
                        Log.e("OnActivityResult", "Luban压缩失败:" + e.getMessage());
                        getNext().handle(matter, activity);
                    }
                }).launch();
    }
}
