package com.sky21.liquor_app.UserAccount;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sky21.liquor_app.Fragment.CompletedFragment;
import com.sky21.liquor_app.Fragment.PendingsellerFragment;
import com.sky21.liquor_app.Fragment.PendinguserFragment;
import com.sky21.liquor_app.R;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CompletedFragment();
        } else if (position == 1) {
            return new PendinguserFragment();
        } else {
            return new PendingsellerFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.completed);
            case 1:
                return mContext.getString(R.string.pending_user);

            case 2:
                return mContext.getString(R.string.pending_seller);
            default:
                return null;
        }
    }
}

