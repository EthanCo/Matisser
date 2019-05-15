package com.heiko.matisser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.ui.MatisseActivity;

import java.util.List;
import java.util.Set;

/**
 * Matisse 包装类
 *
 * @author Heiko
 * @date 2019/5/14
 */
public class Matisser {
    public static final int REQUEST_PERMISSION = 1288;
    private Matisse impl;


    private Matisser(Activity activity) {
        impl = Matisse.from(activity);
    }

    private Matisser(Fragment fragment) {
        impl = Matisse.from(fragment);
    }

    /**
     * Start Matisse from an Activity.
     * <p>
     * This Activity's {@link Activity#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param activity Activity instance.
     * @return Matisse instance.
     */
    public static Matisser from(Activity activity) {
        return new Matisser(activity);
    }

    /**
     * Start Matisse from a Fragment.
     * <p>
     * This Fragment's {@link Fragment#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param fragment Fragment instance.
     * @return Matisse instance.
     */
    public static Matisser from(Fragment fragment) {
        return new Matisser(fragment);
    }

    /**
     * Obtain user selected media' {@link Uri} list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return User selected media' {@link Uri} list.
     */
    public static List<Uri> obtainResult(Intent data) {
        return data.getParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION);
    }

    /**
     * Obtain user selected media path list in the starting Activity or Fragment.
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return User selected media path list.
     */
    public static List<String> obtainPathResult(Intent data) {
        return data.getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH);
    }

    /**
     * Obtain state whether user decide to use selected media in original
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return Whether use original photo
     */
    public static boolean obtainOriginalState(Intent data) {
        return data.getBooleanExtra(MatisseActivity.EXTRA_RESULT_ORIGINAL_ENABLE, false);
    }

    /**
     * MIME types the selection constrains on.
     * <p>
     * Types not included in the set will still be shown in the grid but can't be chosen.
     *
     * @param mimeTypes MIME types set user can choose from.
     * @return {@link SelectionCreatorWrap} to build select specifications.
     * @see MimeType
     * @see SelectionCreatorWrap
     */
    public SelectionCreatorWrap choose(Set<MimeType> mimeTypes) {
        return this.choose(mimeTypes, true);
    }

    /**
     * MIME types the selection constrains on.
     * <p>
     * Types not included in the set will still be shown in the grid but can't be chosen.
     *
     * @param mimeTypes          MIME types set user can choose from.
     * @param mediaTypeExclusive Whether can choose images and videos at the same time during one single choosing
     *                           process. true corresponds to not being able to choose images and videos at the same
     *                           time, and false corresponds to being able to do this.
     * @return {@link SelectionCreatorWrap} to build select specifications.
     * @see MimeType
     * @see SelectionCreatorWrap
     */
    public SelectionCreatorWrap choose(Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        return new SelectionCreatorWrap(impl, mimeTypes, mediaTypeExclusive);
    }

    @Nullable
    Activity getActivity() {
        return impl.getActivity();
    }

    @Nullable
    Fragment getFragment() {
        return impl.getFragment();
    }

    private static Responsibility responsibility;

    public static void addHandler(Responsibility _responsibility) {
        if (responsibility == null) {
            responsibility = _responsibility;
        } else {
            responsibility.setNext(_responsibility);
            responsibility = _responsibility;
        }
    }

    public static void handleResult(Activity activity, String url, final Responsibility onhandle) {
        if (responsibility == null) {
            onhandle.handleRequest(url, activity);
        } else {
            responsibility.setNext(new Responsibility() {
                @Override
                public void handleRequest(String request, Activity activity) {
                    onhandle.handleRequest(request, activity);
                    responsibility.setNext(null);
                }
            });
            responsibility.handleRequest(url, activity);
        }
    }
}
