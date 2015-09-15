package com.niuan.common.ezyer.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;


/**
 * Created by Carlos on 2015/9/14.
 */
public abstract class EzyerSingleFragmentActivity extends EzyerActivity {

    private static final String TAG = EzyerSingleFragmentActivity.class
            .getSimpleName();
    public static final String TAG_NAME_FRAGMENT = "fragment";
    private Fragment mFragment;
    
    private FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Log.v(TAG, "onCreate");

        mContainer = new FrameLayout(this);
        mContainer.setId(R.id.fragment_container);
        
        setContentView(mContainer);

        mFragment = getSupportFragmentManager().findFragmentByTag(
                TAG_NAME_FRAGMENT);
    }

    @Override
    public void onResume() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (mFragment == null) {
            mFragment = initFragment();// getFragmentByType(mType);
            Intent intent = getIntent();
            if (intent != null) {
                Bundle params = intent.getExtras();
                if (mFragment != null) {
                    mFragment.setArguments(params);
                }
            }
            transaction.add(R.id.fragment_container, mFragment, TAG_NAME_FRAGMENT);
        }
        transaction.show(mFragment);
        transaction.commitAllowingStateLoss();
        super.onResume();
    }

    protected abstract Fragment initFragment();
}
