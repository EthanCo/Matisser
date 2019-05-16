/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Matisse原本的Sample
 */
public class MatisseSampleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private UriAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_sample);
        findViewById(R.id.zhihu).setOnClickListener(this);
        findViewById(R.id.dracula).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new UriAdapter());
    }

    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            switch (v.getId()) {
                                case R.id.zhihu:
                                    /*Matisse.from(MatisseSampleActivity.this)
                                            .choose(MimeType.ofAll(), false)
                                            .countable(true)
                                            .capture(true)
                                            .captureStrategy(
                                                    new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                                            .maxSelectable(9)
                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                            .gridExpectedSize(
                                                    getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                            .thumbnailScale(0.85f)
//                                            .imageEngine(new GlideEngine())  // for glide-V3
                                            .imageEngine(new Glide4Engine())    // for glide-V4
                                            .setOnSelectedListener(new OnSelectedListener() {
                                                @Override
                                                public void onSelected(
                                                        @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                                    // DO SOMETHING IMMEDIATELY HERE
                                                    Log.e("onSelected", "onSelected: pathList=" + pathList);

                                                }
                                            })
                                            .originalEnable(true)
                                            .maxOriginalSize(10)
                                            .autoHideToolbarOnSingleTap(true)
                                            .setOnCheckedListener(new OnCheckedListener() {
                                                @Override
                                                public void onCheck(boolean isChecked) {
                                                    // DO SOMETHING IMMEDIATELY HERE
                                                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                                                }
                                            })
                                            .forResult(REQUEST_CODE_CHOOSE);*/
                                    break;
                                case R.id.dracula:
                                    /*Matisse.from(MatisseSampleActivity.this)
                                            .choose(MimeType.ofImage())
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(false)
                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                            .maxSelectable(9)
                                            .originalEnable(true)
                                            .maxOriginalSize(10)
                                            .imageEngine(new PicassoEngine())
                                            .forResult(REQUEST_CODE_CHOOSE);*/
                                    break;
                                default:
                                    break;
                            }
                            mAdapter.setData(null, null);
                        } else {
                            Toast.makeText(MatisseSampleActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            /*mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(data)));*/
        }
    }
}
