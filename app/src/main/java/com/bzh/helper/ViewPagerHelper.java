package com.bzh.helper;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class ViewPagerHelper implements ViewPager.OnPageChangeListener {

    public interface IViewPagerTrendListener {

        /**
         * 当一个页面被完全选定时调用。该方法会在{@link #onPrePageSelected(int)}之后被调用。
         *
         * @param selectedPosition 当前位置
         */
        void onFullPageSelected(int selectedPosition);

        /**
         * 当滑动方向被确定时调用
         *
         * @param direct           方向 {@link #DIRECT_LEFT} {@link #DIRECT_NONE} {@link #DIRECT_RIGHT}
         * @param selectedPosition 当前位置
         * @param nextPosition     下一个位置
         */
        void onDirectSelected(int direct, int selectedPosition, int nextPosition);

        /**
         * 当一个页面被选定时调用。该方法会在{@link #onFullPageSelected(int)}之前被调用。
         *
         * @param position 位置
         */
        void onPrePageSelected(int position);

        /**
         * 在当前方向下滑动的分数
         *
         * @param direct           方向
         * @param selectedPosition 当前位置
         * @param nextPosition     下一个位置
         * @param fraction         分数
         */
        void onFractionPage(int direct, int selectedPosition, int nextPosition, float fraction);
    }

    private List<IViewPagerTrendListener> mListeners = new ArrayList<>();

    public static final int DIRECT_NONE = 0;
    public static final int DIRECT_LEFT = 1;
    public static final int DIRECT_RIGHT = 2;
    public static final int POSITION_NO = -1;

    private static final String TAG = "ViewPagerHelper";

    public void setViewPager(ViewPager viewPager, PagerAdapter adapter) {
        if (viewPager == null) {
            Log.d(TAG, "setViewPager() called with: viewPager is null");
            return;
        }
        viewPager.addOnPageChangeListener(this);
    }

    private int mDirect = DIRECT_NONE;


    private int mNextPosition = POSITION_NO;
    private int mSelectedPosition = 0;

    private int mPosition = 0;
    private int mPreviousScrollState;
    private int mScrollState;

    private int mFlagPosition;
    private int mPreFlagPosition;

    private float mPrePositionOffset;
    private float mPositionOffset;

    private boolean mIsSelectDirect;

    public void addListener(IViewPagerTrendListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeListener(IViewPagerTrendListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        // 用于判断position是否被改变
        mPreFlagPosition = mFlagPosition;
        mFlagPosition = position;

        // 用于判断左右方向
        mPrePositionOffset = mPositionOffset;
        mPositionOffset = positionOffset;

        if (mPreFlagPosition != mFlagPosition) {

            // 当快速滑动页面时，需要快速更新 mSelectedPosition 的值以及从新确定方向
            boolean isFastChange = mPreviousScrollState == SCROLL_STATE_SETTLING && mScrollState == SCROLL_STATE_DRAGGING;
            if (isFastChange && mNextPosition != POSITION_NO && mSelectedPosition != mNextPosition) {
                mSelectedPosition = mNextPosition;
            }
            mIsSelectDirect = false;
            mDirect = DIRECT_NONE;
            mNextPosition = POSITION_NO;
        } else {
            if (mPositionOffset > mPrePositionOffset) {
                if (!mIsSelectDirect) {
                    mIsSelectDirect = true;
                    mDirect = DIRECT_RIGHT;
                    mNextPosition = mPosition + 1;
                    notifyDirectSelected(mDirect, mSelectedPosition, mNextPosition);
                }
            } else if (mPositionOffset < mPrePositionOffset) {
                if (!mIsSelectDirect) {
                    mIsSelectDirect = true;
                    mDirect = DIRECT_LEFT;
                    mNextPosition = mPosition - 1;
                    notifyDirectSelected(mDirect, mSelectedPosition, mNextPosition);
                }
            }
        }

        final boolean scrollUpdate = mScrollState == SCROLL_STATE_DRAGGING ||
                (mPreviousScrollState == SCROLL_STATE_DRAGGING && mScrollState == SCROLL_STATE_SETTLING);

        if (scrollUpdate) {

            // 修复"一直向右快速滑动时，突然向左滑动导致的mSelectedTab与mNextTab更新不及时"引起的
            // 动画效果丢失的问题
            if ((mDirect == DIRECT_LEFT) && mNextPosition != POSITION_NO
                    && mPreFlagPosition == mFlagPosition
                    && mPreFlagPosition != mNextPosition
                    ) {
                mSelectedPosition = mNextPosition;
                notifyFullPage(mSelectedPosition);
                mNextPosition = POSITION_NO;
            }

            // 修复"一直向左快速滑动时，突然向右滑动导致的mSelectedTab与mNextTab更新不及时"引起的
            // 动画效果丢失的问题
            if ((mDirect == DIRECT_RIGHT)
                    && mNextPosition != POSITION_NO
                    && mSelectedPosition != POSITION_NO
                    && mPreFlagPosition == mFlagPosition
                    && mPreFlagPosition != mSelectedPosition
                    ) {
                notifyFullPage(mNextPosition);
                mNextPosition = POSITION_NO;
            }
        }

        if (mDirect != DIRECT_NONE) {
            notifyFractionPage(mDirect, mSelectedPosition, mNextPosition, mDirect == DIRECT_RIGHT ? mPositionOffset : (1 - mPositionOffset));
        }

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        notifyPrePage(mPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mPreviousScrollState = mScrollState;
        mScrollState = state;

        boolean draggingToIdle = mPreviousScrollState == SCROLL_STATE_DRAGGING && mScrollState == SCROLL_STATE_IDLE;
        boolean settingToIdle = mPreviousScrollState == SCROLL_STATE_SETTLING && mScrollState == SCROLL_STATE_IDLE;
        if ((draggingToIdle || settingToIdle)) {
            mSelectedPosition = mPosition;
            notifyFullPage(mSelectedPosition);
        }

        boolean isIdle = mScrollState == SCROLL_STATE_IDLE;
        if (isIdle) {
            mNextPosition = POSITION_NO;
            mDirect = DIRECT_NONE;
            mIsSelectDirect = false;
        }
    }

    private void notifyFullPage(int selectedPosition) {
        Log.d(TAG, "notifyFullPage() called with: selectedPosition = [" + selectedPosition + "]");
        for (IViewPagerTrendListener listener : mListeners) {
            listener.onFullPageSelected(selectedPosition);
        }
    }

    private void notifyPrePage(int position) {
        Log.d(TAG, "notifyPrePage() called");
        for (IViewPagerTrendListener listener : mListeners) {
            listener.onPrePageSelected(position);
        }
    }

    private void notifyDirectSelected(int direct, int selectedPosition, int nextPosition) {
        Log.d(TAG, "notifyDirectSelected() called with: direct = [" + direct + "], selectedPosition = [" + selectedPosition + "], nextPosition = [" + nextPosition + "]");
        for (IViewPagerTrendListener listener : mListeners) {
            listener.onDirectSelected(direct, selectedPosition, nextPosition);
        }
    }


    private void notifyFractionPage(int direct, int selectedPosition, int nextPosition, float positionOffset) {
        Log.d(TAG, String.format("notifyFractionPage() called width: direct = [%d] positionOffset = [%-15f],  mSelectedPosition = [%-2d] mNextPosition = [%-2d] mPreFlagPosition = [%-2d] mFlagPosition=[%-2d] state = %s",
                direct,
                positionOffset,
                selectedPosition,
                nextPosition,
                mPreFlagPosition,
                mFlagPosition,
                getState(mScrollState)));

        for (IViewPagerTrendListener listener : mListeners) {
            listener.onFractionPage(direct, selectedPosition, nextPosition, positionOffset);
        }
    }


    private String getState(int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                return "IDLE";
            case SCROLL_STATE_DRAGGING:
                return "DRAGGING";
            case SCROLL_STATE_SETTLING:
                return "SETTLING";
        }
        return "";
    }
}