package com.zhihu.matisse.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.heiko.matisser.Matisser;
import com.heiko.matisser.Responsibility;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisser.obtainPathResult(data);
            mAdapter.setData(Matisser.obtainResult(data), paths);
            Log.i("OnActivityResult ", String.valueOf(Matisser.obtainOriginalState(data)));
            Log.i("OnActivityResult ", "paths[0]:" + paths.get(0));

            File file = new File(paths.get(0));
            if (!file.exists()) {
                Toast.makeText(MatisserSampleActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.i("OnActivityResult ", "file.size:" + file.length());
            }
            Matisser.handleResult(this, paths.get(0), new Responsibility() {
                @Override
                public void handleRequest(String request, Context context) {
                    File file = new File(request);
                    Log.i("OnActivityResult", "handleRequest:" + request + " file.size:" + file.length());
                    Toast.makeText(MatisserSampleActivity.this, "handleRequest", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
