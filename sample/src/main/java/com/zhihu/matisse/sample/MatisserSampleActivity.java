package com.zhihu.matisse.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.heiko.matisser.Matisser;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

/**
 * 对Matisser进行了包装，自带权限申请、图片裁剪、图片压缩
 */
public class MatisserSampleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 25;
    private UriAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisser_sample);
        findViewById(R.id.matisser).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new UriAdapter());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.matisser:
                Matisser.from(MatisserSampleActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .countable(false)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .maxSelectable(9)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Matisser.onActivityResult(this, requestCode, resultCode, data)) return;

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisser.obtainPathResult(data);
            Log.i("OnActivityResult ", String.valueOf(Matisser.obtainOriginalState(data)));

            Matisser.handleResult(this,"sample", paths, new Matisser.HandleResult() {
                @Override
                public void onResult(List<String> urls) {
                    Toast.makeText(MatisserSampleActivity.this, "handleRequest", Toast.LENGTH_SHORT).show();
                    Log.i("OnActivityResult ", "onHandle urls:" + urls);
                    mAdapter.setData(null, urls);
                }
            });

           /* List<Uri> uris = Matisse.obtainResult(data);
            final Uri resultUri = uris.get(0);
            Log.i("OnActivityResult", "选择图片:" + resultUri.toString());
            File file_temp = new File(getCachePath(this), "test_temp.jpeg");
            if (!file_temp.exists()) {
                file_temp.mkdir();
            }
            //文件不存在判断
            Uri targetFile = Uri.parse(file_temp.toString());
            UCrop.Options options = new UCrop.Options();
            //options.setCompressionFormat(Bitmap.CompressFormat.WEBP);
            //options.setCircleDimmedLayer(true);
            UCrop.of(resultUri, targetFile)
//                    .withAspectRatio(16, 9)
//                    .withMaxResultSize(900, 1600)
                    .withAspectRatio(4, 3)
                    //.withMaxResultSize(1600,1200)
                    .withMaxResultSize(2304, 1728)
                    .withOptions(options)
                    .start(this);*/
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
