package com.heiko.matisser;

import android.support.annotation.NonNull;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;

import java.util.Set;

/**
 * SelectionCreator 包装类
 *
 * @author Heiko
 * @date 2019/5/14
 */
public class SelectionCreatorWrap extends SelectionCreator {

    /**
     * Constructs a new specification builder on the context.
     *
     * @param matisse            a requester context wrapper.
     * @param mimeTypes          MIME type set to select.
     * @param mediaTypeExclusive
     */
    protected SelectionCreatorWrap(Matisse matisse, @NonNull Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        super(matisse, mimeTypes, mediaTypeExclusive);
    }

    @Override
    public void forResult(final int requestCode) {
        Matisser.requestPermission(mMatisse.getActivity(), new Matisser.PermissionCallback() {
            @Override
            public void onGetPermission() {
                SelectionCreatorWrap.super.forResult(requestCode);
            }
        });
    }
}
