package com.heiko.matisser;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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

    public static final String TAG = "SelectionCreatorWrap";
    private Lazy<PermissionsFragment> mRxPermissionsFragment;

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
        if (isMarshmallow()) {
            /*Matisser.requestPermission(mMatisse.getActivity(), new Matisser.PermissionCallback() {
                @Override
                public void onGetPermission() {
                    SelectionCreatorWrap.super.forResult(requestCode);
                }
            });*/
            mRxPermissionsFragment = getLazySingleton(((FragmentActivity) mMatisse.getActivity()).getSupportFragmentManager());
            if (mRxPermissionsFragment.get().isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                SelectionCreatorWrap.super.forResult(requestCode);
            } else if (mRxPermissionsFragment.get().isRevoked(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mRxPermissionsFragment.get().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            } else {
                mRxPermissionsFragment.get().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        } else {
            SelectionCreatorWrap.super.forResult(requestCode);
        }
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @NonNull
    private Lazy<PermissionsFragment> getLazySingleton(@NonNull final FragmentManager fragmentManager) {
        return new Lazy<PermissionsFragment>() {

            private PermissionsFragment PermissionsFragment;

            @Override
            public synchronized PermissionsFragment get() {
                if (PermissionsFragment == null) {
                    PermissionsFragment = getPermissionsFragment(fragmentManager);
                }
                return PermissionsFragment;
            }

        };
    }

    private PermissionsFragment getPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        PermissionsFragment PermissionsFragment = findPermissionsFragment(fragmentManager);
        boolean isNewInstance = PermissionsFragment == null;
        if (isNewInstance) {
            PermissionsFragment = new PermissionsFragment();
            fragmentManager
                    .beginTransaction()
                    .add(PermissionsFragment, TAG)
                    .commitNow();
        }
        return PermissionsFragment;
    }

    private PermissionsFragment findPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        return (PermissionsFragment) fragmentManager.findFragmentByTag(TAG);
    }

    @FunctionalInterface
    public interface Lazy<V> {
        V get();
    }
}
