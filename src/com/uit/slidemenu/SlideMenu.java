/**
 *
 *	created by Mr.Simple, Sep 13, 20148:34:03 PM.
 *	Copyright (c) 2014, hehonghui@umeng.com All Rights Reserved.
 *
 *                #####################################################
 *                #                                                   #
 *                #                       _oo0oo_                     #   
 *                #                      o8888888o                    #
 *                #                      88" . "88                    #
 *                #                      (| -_- |)                    #
 *                #                      0\  =  /0                    #   
 *                #                    ___/`---'\___                  #
 *                #                  .' \\|     |# '.                 #
 *                #                 / \\|||  :  |||# \                #
 *                #                / _||||| -:- |||||- \              #
 *                #               |   | \\\  -  #/ |   |              #
 *                #               | \_|  ''\---/''  |_/ |             #
 *                #               \  .-\__  '-'  ___/-. /             #
 *                #             ___'. .'  /--.--\  `. .'___           #
 *                #          ."" '<  `.___\_<|>_/___.' >' "".         #
 *                #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 *                #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 *                #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 *                #                       `=---='                     #
 *                #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 *                #                                                   #
 *                #               佛祖保佑         永无BUG              #
 *                #                                                   #
 *                #####################################################
 */

package com.uit.slidemenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author mrsimple
 */
public class SlideMenu extends RelativeLayout implements OnTouchListener {
    /**
     * 
     */
    private int mScreenWidth;

    private int mScreenHeight;
    /**
     * 
     */
    private View mMenuView;

    /**
     * 
     */
    private MarginLayoutParams mLeftMenuParams;
    /**
     * 菜单的右边界
     */
    protected int mLeftMarginRightEdge = 0;
    /**
     * 菜单的宽度, 将会设置为屏幕宽度的60%
     */
    private int mMenuWidth = 0;
    /**
     * 内容视图
     */
    private View mContentView;
    /**
     * 
     */
    MarginLayoutParams mContentLyParams;
    /**
     * 触摸滑动阀值
     */
    protected int mTouchSlop;
    /**
     * 
     */
    private static final int STATUS_MENU_SHOW = 1;
    /**
     * 
     */
    private static final int STATUS_MENU_HIDE = 2;
    /**
     * 
     */
    private static final int STATUS_MENU_SCROLLING = 3;
    /**
     * 
     */
    private int mMenuStatus = STATUS_MENU_HIDE;

    /**
     * 
     */
    private OnSlideMenuListener mSlideMenuListener;

    /**
     * 
     */
    private int xDown;
    /**
     * 
     */
    private int xDistance;
    /**
     * 
     */
    private float mMenuScale = 0.6f;

    /**
     * @param context
     */
    public SlideMenu(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        this.getViewTreeObserver().addOnGlobalLayoutListener(new
                OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        if (mMenuView == null) {
                            // default menu view
                            SlideMenu.this.setMenuView(null);
                        }

                        if (mContentView == null) {
                            // default menu view
                            SlideMenu.this.setContentView(null);
                        }
                        mLeftMenuParams = (MarginLayoutParams) mMenuView.getLayoutParams();
                        mLeftMenuParams.width = (int) (mScreenWidth * mMenuScale);
                        mLeftMenuParams.height = mScreenHeight;
                        // hide the menu view
                        mLeftMenuParams.leftMargin = -mLeftMenuParams.width;
                        mMenuView.setLayoutParams(mLeftMenuParams);

                        mMenuWidth = mLeftMenuParams.width;
                        Log.d(VIEW_LOG_TAG, "### screen width = " + mScreenWidth);

                        //
                        mContentLyParams = (MarginLayoutParams) mContentView.getLayoutParams();
                        mContentLyParams.width = mScreenWidth;
                        //
                        SlideMenu.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
    }

    /**
     * @param menuLayout
     */
    public void setMenuView(int menuLayout) {
        mMenuView = LayoutInflater.from(getContext()).inflate(menuLayout, this, false);
        //
        setMenuView(mMenuView);
    }

    /**
     * @param menuLayout
     */
    public void setMenuView(View menuView) {
        mMenuView = menuView;

        if (mMenuView == null) {
            mMenuView = new MenuView(getContext());
        }

        if (mMenuView.getId() == View.NO_ID) {
            mMenuView.setId(123);
        }
        this.addView(mMenuView, 0);
    }

    /**
     * 
     */
    public void setContentView(int contentLayout) {

        mContentView = LayoutInflater.from(getContext()).inflate(contentLayout, this, false);
        //
        setContentView(mContentView);
    }

    /**
     * @param menuView
     * @param contentView
     */
    public void setContentView(View contentView) {

        mContentView = contentView;
        if (mContentView == null) {
            mContentView = new TextView(getContext());
        }
        
        if ( mMenuView == null ) {
            setMenuView(null);
        }
        //
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentView
                .getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        layoutParams.width = mScreenWidth;
        layoutParams.addRule(RelativeLayout.RIGHT_OF, mMenuView.getId());
        mContentView.setBackgroundColor(Color.CYAN);
        mContentView.setOnTouchListener(this);

        //
        this.addView(mContentView, layoutParams);
    }

    /**
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        if (activity != null) {
            activity.getWindow().getDecorView().setOnTouchListener(this);
            activity.setContentView(this);
        }
    }

    /**
     * @param scale
     */
    public void setMenuViewScale(float scale) {
        if (scale > 0.0f && scale <= 1.0f) {
            mMenuScale = scale;
        }
    }

    /**
     * @param listener
     */
    public void setOnSlideMenuListener(OnSlideMenuListener listener) {
        mSlideMenuListener = listener;
    }

    // /**
    // * @param item
    // */
    // public void addMenuItem(MenuItem item) {
    // mMenuView.addMenuItem(item);
    // }

    /**
     * 
     */
    private void changeMenuViewMarginAsync(int leftMargin) {
        new MenuViewAsyncTask().execute(leftMargin);
    }

    /**
     * @param distance 从右向左滑动, 此时 distance 为负数
     */
    private void scrollToLeft(int distance) {

        if (mLeftMenuParams.leftMargin == -mMenuWidth || mContentLyParams.rightMargin == 0) {
            return;
        }
        int menuLeftMargin = distance;
        int contentLeftMargin = -mMenuWidth - distance;

        //
        updateMargin(menuLeftMargin, contentLeftMargin);
    }

    /**
     * @param distance 从左向右滑动, 此时 distance 为正数
     */
    private void scrollToRight(int distance) {

        if (mContentLyParams.leftMargin == -mMenuWidth || mLeftMenuParams.leftMargin == 0
                || isInvalidData(distance)) {
            return;
        }
        int menuLeftMargin = -mMenuWidth + distance;
        int contentLeftMargin = -distance;

        //
        updateMargin(menuLeftMargin, contentLeftMargin);
    }

    /**
     * 无效的滑动, 例如当菜单为完全显示的状态下，此时 menu left margin = 0。我们先从 x = 600的坐标点按下，然后向左滑动到
     * x = 400, 此时再向右滑动到 x = 600，在 x刚小于600的时刻，例如 x = 599, 此时 distance = -1, 因此
     * menu left margin = -1，即将近完全显示的状态。现在继续向右滑动，超过 x = 600 ( 实际上需要超过 600 +
     * mTouchSlop 的坐标才行 )，此时 distance = x - 600, 例如distance = 16, 此时 menu left
     * margin 会迅速的从 -1 到 -mMenuWidth + distance,
     * 即菜单将近因此的状态。因此在这个节点会出现跳跃。该方法就是判断是否是这种情况的。
     * 
     * @param distance
     * @return
     */
    private boolean isInvalidData(int distance) {
        /**
         * if (mLeftLvParams.leftMargin > -100 && xOffset < 200 &&
         * mContentLayoutParams.rightMargin < -400) { return; }
         */
        int menuLeftMargin = mLeftMenuParams.leftMargin;
        // 误差值
        int errRange = 20;
        return -mMenuWidth + distance + errRange < menuLeftMargin;
    }

    /**
     * @param menuLeftMargin
     * @param contentLeftMargin
     */
    private void updateMargin(int menuLeftMargin, int contentLeftMargin) {

        if (menuLeftMargin < -mMenuWidth) {
            menuLeftMargin = -mMenuWidth;
        } else if (menuLeftMargin > 0) {
            menuLeftMargin = 0;
        }

        if (contentLeftMargin > 0) {
            contentLeftMargin = 0;
        } else if (contentLeftMargin < -mMenuWidth) {
            contentLeftMargin = -mMenuWidth;
        }

        mLeftMenuParams.leftMargin = menuLeftMargin;
        mMenuView.setLayoutParams(mLeftMenuParams);

        //
        mContentLyParams.rightMargin = contentLeftMargin;
        mContentView.setLayoutParams(mContentLyParams);

        Log.d(VIEW_LOG_TAG, "###  mLeftLvParams.leftMargin = " + mLeftMenuParams.leftMargin
                + ",  mContentLayoutParams.rightMargin = " + mContentLyParams.rightMargin);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                xDistance = (int) (event.getRawX() - xDown);
                if (mMenuStatus == STATUS_MENU_SCROLLING) {
                    return true;
                }
                // 滑动阀值
                if (Math.abs(xDistance) >= mTouchSlop) {
                    // adjustMenuMargin(xDistance);
                    if (xDistance > 0) {
                        scrollToRight(xDistance);
                    } else {
                        scrollToLeft(xDistance);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                int curLeftMargin = mLeftMenuParams.leftMargin;
                int targetMargin = -1;
                int changeSlop = mMenuWidth / 3;
                int absDistance = Math.abs(xDistance);
                //
                if (curLeftMargin < 0 && curLeftMargin > -mMenuWidth) {
                    if (xDistance > mTouchSlop && absDistance >= changeSlop) {
                        targetMargin = 0;
                    } else if (absDistance >= changeSlop && xDistance < 0) {
                        targetMargin = -mMenuWidth;
                    } else if (mMenuStatus == STATUS_MENU_SHOW) {
                        targetMargin = 0;
                    } else if (mMenuStatus == STATUS_MENU_HIDE) {
                        targetMargin = -mMenuWidth;
                    }
                } else if (curLeftMargin == 0 && xDistance < 0) {
                    targetMargin = -mMenuWidth;
                } else if (targetMargin == -1 && curLeftMargin == 0) {
                    targetMargin = 0;
                } else if (targetMargin == -1 && curLeftMargin == -mMenuWidth) {
                    targetMargin = -mMenuWidth;
                }

                if (targetMargin == -1 && xDistance < 0 && mMenuStatus != STATUS_MENU_SHOW) {
                    return false;
                }
                // 对菜单和 content 的 margin 进行处理的异步任务
                changeMenuViewMarginAsync(targetMargin);
                break;
            default:
                break;
        }
        return false;
    } //

    /**
     * 
     */
    public void showMenu() {
        if (mMenuStatus != STATUS_MENU_SHOW) {
            changeMenuViewMarginAsync(mLeftMarginRightEdge);
        }
    }

    /**
     * 
     */
    public void hideMenu() {
        if (mMenuStatus != STATUS_MENU_HIDE) {
            changeMenuViewMarginAsync(-mMenuWidth);
        }
    }

    /**
     * @author mrsimple
     */
    class MenuViewAsyncTask extends AsyncTask<Integer, MarginPair, Void> {

        int mTargetMargin = 0;
        int mOldLeftMargin = mLeftMenuParams.leftMargin;
        int mOldRightMargin = mContentLyParams.rightMargin;
        //
        MarginPair marginPair = new MarginPair();

        @Override
        protected Void doInBackground(Integer... params) {
            mTargetMargin = params[0];
            Log.d(VIEW_LOG_TAG, "### background, mTargetMargin : " + mTargetMargin + ", old = "
                    + mOldLeftMargin);
            if (mOldLeftMargin == mTargetMargin) {
                return null;
            }
            mMenuStatus = STATUS_MENU_SCROLLING;
            try {
                int loopTime = Math.abs(mTargetMargin - mOldLeftMargin);
                loopTime = Math.min(loopTime, mMenuWidth);
                int left = 0;
                int right = 0;
                for (int i = 1; i <= loopTime; i++) {
                    if (mTargetMargin == 0) {
                        left = mOldLeftMargin + i;
                        marginPair.menuLeftMargin = Math.min(left, 0);
                        right = mOldRightMargin - i;
                        marginPair.contenteRightMargin = Math.max(right, -mMenuWidth);
                    } else {
                        left = mOldLeftMargin - i;
                        marginPair.menuLeftMargin = Math.max(left, -mMenuWidth);
                        right = mOldRightMargin + i;
                        marginPair.contenteRightMargin = Math.min(right, 0);
                    }
                    Log.d(VIEW_LOG_TAG, "### margin pair = " + marginPair + ", loop time = "
                            + loopTime);
                    publishProgress(marginPair);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        } //

        @Override
        protected void onProgressUpdate(MarginPair... values) {

            mLeftMenuParams.leftMargin = marginPair.menuLeftMargin;
            mMenuView.setLayoutParams(mLeftMenuParams);

            mContentLyParams.rightMargin = marginPair.contenteRightMargin;
            mContentView.setLayoutParams(mContentLyParams);

            Log.d(VIEW_LOG_TAG, "@@@ value[0]" + values[0] + ", mTarget = " + mTargetMargin
                    + ", mOldLeftMargin = " + mOldLeftMargin
                    + ", mContentLayoutParams.rightMargin = " + mContentLyParams.rightMargin);
        }

        @Override
        protected void onPostExecute(Void result) {

            if (mSlideMenuListener != null) {
                if (mTargetMargin < 0 && mMenuStatus != STATUS_MENU_HIDE) {
                    mSlideMenuListener.onMenuStateChange(MENU_HIDE);
                } else if (mTargetMargin == 0 && mMenuStatus != STATUS_MENU_SHOW) {
                    mSlideMenuListener.onMenuStateChange(MENU_SHOW);
                }
            }

            if (mLeftMenuParams.leftMargin == 0) {
                mMenuStatus = STATUS_MENU_SHOW;
            } else if (mLeftMenuParams.leftMargin == -mMenuWidth) {
                mMenuStatus = STATUS_MENU_HIDE;
            }
        }

    } //

    /**
     * 
     */
    public static final int MENU_HIDE = 0;
    /**
     * 
     */
    public static final int MENU_SHOW = 1;

    /**
     * @author mrsimple
     */
    public static interface OnSlideMenuListener {
        /**
         * 0为隐藏状态, 1为显示状态
         * 
         * @param state
         */
        public void onMenuStateChange(int state);
    }

    /**
     * @author mrsimple
     */
    public static class MarginPair {
        int menuLeftMargin;
        int contenteRightMargin;

        @Override
        public String toString() {
            return "MarginPair [menuLeftMargin=" + menuLeftMargin + ", contenteRightMargin="
                    + contenteRightMargin + "]";
        }
    }
}
