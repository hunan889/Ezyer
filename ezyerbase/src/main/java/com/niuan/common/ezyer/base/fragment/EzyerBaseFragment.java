package com.niuan.common.ezyer.base.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Carlos Liu on 2015/9/25.
 */
public class EzyerBaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        if (isVisibleToUser()) {
            onVisibleToUser();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isVisibleToUser()) {
            onInVisibleToUser();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser()) {
            onVisibleToUser();
        } else {
            onInVisibleToUser();
        }
    }

    private boolean isVisibleToUser() {
        return getUserVisibleHint() && isResumed();
    }

    /**
     * Indicates fragment is becoming visible to user, in this case, below conditions are true:
     * 1. {@link #getUserVisibleHint()}
     * 2. {@link #isResumed()}
     * <p/>
     * Called in {@link #onResume()} or {@link #setUserVisibleHint(boolean)}
     */
    public void onVisibleToUser() {
    }

    /**
     * Indicates fragment is becoming invisible to user, in this case, below conditions are false:
     * 1. {@link #getUserVisibleHint()}
     * 2. {@link #isResumed()}
     * <p/>
     * Called in {@link #onPause()} or {@link #setUserVisibleHint(boolean)}
     */
    public void onInVisibleToUser() {

    }
}
