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

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
    /**
     * 
     */
    private MenuView mMenuView;

    /**
     * 
     */
    private MarginLayoutParams mLeftLvParams;
    /**
     * 
     */
    protected int mLeftMarginEdge = 0;

    private int mMenuWidth = 0;
    /**
     * // *
     */
    private TextView mContentView;
    /**
     * 
     */
    MarginLayoutParams mContentLayoutParams;

    protected int mTouchSlop;

    private static final int STATUS_MENU_SHOW = 1;

    private static final int STATUS_MENU_HIDE = 2;

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

        initLayout();
    }

    /**
     * 
     */
    private void initLayout() {
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        //
        mMenuView = new MenuView(getContext());
        this.getViewTreeObserver().addOnGlobalLayoutListener(new
                OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mLeftLvParams = (MarginLayoutParams) mMenuView.getLayoutParams();
                        mLeftLvParams.width = (int) (mScreenWidth * 0.6f);
                        // hide the menu view
                        mLeftLvParams.leftMargin = -mLeftLvParams.width;
                        mMenuView.setLayoutParams(mLeftLvParams);

                        mMenuWidth = mLeftLvParams.width;
                        Log.d(VIEW_LOG_TAG, "### screen width = " + mScreenWidth);

                        //
                        mContentLayoutParams = (MarginLayoutParams) mContentView.getLayoutParams();
                        mContentLayoutParams.width = mScreenWidth;
                        //
                        SlideMenu.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
        mMenuView.setId(123);
        this.addView(mMenuView, 0);

        //
        mContentView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = mScreenWidth;
        mContentView.setText("这是Content View ");
        mContentView.setGravity(Gravity.CENTER);
        mContentView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLeftLvParams.leftMargin == 0 && mMenuStatus == STATUS_MENU_SHOW) {
                    // changeMenuViewMarginAsync(-mMenuWidth);
                }
            }
        });
        mContentView.setTextSize(30f);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, mMenuView.getId());
        mContentView.setBackgroundColor(Color.CYAN);
        this.addView(mContentView, layoutParams);
        mContentView.setOnTouchListener(this);
    }

    /**
     * @param listener
     */
    public void setOnSlideMenuListener(OnSlideMenuListener listener) {
        mSlideMenuListener = listener;
    }

    /**
     * @param item
     */
    public void addMenuItem(MenuItem item) {
        mMenuView.addMenuItem(item);
    }

    /**
     * 
     */
    private void changeMenuViewMarginAsync(int leftMargin) {
        new MenuViewAsyncTask().execute(leftMargin);
    }

    /**
     * @param xOffset
     */
    protected void adjustMenuMargin(int xOffset) {

        //
        int curLeftMargin = mLeftLvParams.leftMargin;
        //
        int contentRightMargin = mContentLayoutParams.rightMargin;
        Log.d(VIEW_LOG_TAG, "### curLeftMargin = " + curLeftMargin + ", contentRightMargin = "
                + contentRightMargin + ", xOffet = "
                + xOffset);
        //
        if ((curLeftMargin == 0 && xOffset > 0)
                || (curLeftMargin == -mMenuWidth && xOffset < 0)) {
            Log.d(VIEW_LOG_TAG, "### adjustMenuMargin return ");
            return;
        }

        int newLeftMargin = -mMenuWidth;
        // 计算新的left margin值
        if (xOffset > 0 && mLeftLvParams.leftMargin < 0) {

            if (mLeftLvParams.leftMargin > -100 && xOffset < 200
                    && mContentLayoutParams.rightMargin < -400) {
                return;
            }
            // left menu move to content region
            newLeftMargin += xOffset;
            // Content View move to right
            contentRightMargin = -xOffset;
        } else if (xOffset < 0 && curLeftMargin > -mMenuWidth) {
            newLeftMargin = xOffset;
            Log.d(VIEW_LOG_TAG, "### 1  : contentRightMargin = " + contentRightMargin
                    + ", xOffset " + (xOffset));
            //
            // contentRightMargin += -xOffset;
            contentRightMargin = -mMenuWidth - newLeftMargin;
            Log.d(VIEW_LOG_TAG, "### 2  : contentRightMargin = " + contentRightMargin
                    + ", -xOffset " + (-xOffset));
        }

        // 确保阀值
        if (newLeftMargin > 0) {
            newLeftMargin = 0;
        } else if (newLeftMargin < -mMenuWidth) {
            newLeftMargin = -mMenuWidth;
        }

        if (contentRightMargin < -mMenuWidth) {
            contentRightMargin = -mMenuWidth;
        } else if (contentRightMargin > 0) {
            contentRightMargin = 0;
        }

        //
        mLeftLvParams.leftMargin = newLeftMargin;
        mMenuView.setLayoutParams(mLeftLvParams);

        //
        mContentLayoutParams.rightMargin = contentRightMargin;
        mContentView.setLayoutParams(mContentLayoutParams);
        // Log.d(VIEW_LOG_TAG, "### mLeftLvParams left margin = " +
        // mLeftLvParams.leftMargin
        // + ", right margin = " + mContentLayoutParams.rightMargin
        // );
    }

    /**
     * @param distance 从右向左滑动, 此时 distance 为负数
     */
    private void scrollToLeft(int distance) {

        if (mLeftLvParams.leftMargin == -mMenuWidth || mContentLayoutParams.rightMargin == 0) {
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

        if (mContentLayoutParams.leftMargin == -mMenuWidth || mLeftLvParams.leftMargin == 0) {
            return;
        }
        int menuLeftMargin = -mMenuWidth + distance;
        int contentLeftMargin = -distance;

        //
        updateMargin(menuLeftMargin, contentLeftMargin);
    }

    /**
     * 无效的滑动
     * 
     * @param distance
     * @return
     */
    private boolean isInvalidData(int distance) {
         return false ;
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

        mLeftLvParams.leftMargin = menuLeftMargin;
        mMenuView.setLayoutParams(mLeftLvParams);

        //
        mContentLayoutParams.rightMargin = contentLeftMargin;
        mContentView.setLayoutParams(mContentLayoutParams);

        Log.d(VIEW_LOG_TAG, "###  mLeftLvParams.leftMargin = " + mLeftLvParams.leftMargin
                + ",  mContentLayoutParams.rightMargin = " + mContentLayoutParams.rightMargin);
    }

    int xDown;
    int xDistance;

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
                if (Math.abs(xDistance) > mTouchSlop) {
                    // adjustMenuMargin(xDistance);
                    if (xDistance > 0) {
                        scrollToRight(xDistance);
                    } else {
                        scrollToLeft(xDistance);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                int curLeftMargin = mLeftLvParams.leftMargin;
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
                // changeStatus(targetMargin);
                // if (targetMargin == -1 && yDistance < 0 && mMenuStatus !=
                // STATUS_MENU_SHOW
                // || curLeftMargin == 0) {
                // return false;
                // }
                if (targetMargin == -1 && xDistance < 0 && mMenuStatus != STATUS_MENU_SHOW) {
                    return false;
                }
                changeMenuViewMarginAsync(targetMargin);
                // 在这里进行处理
                break;
            default:
                break;
        }
        return false;
    } //

    /**
     * 
     */
    // private void changeStatus(int targetMargin) {
    // if (targetMargin == 0) {
    // mMenuStatus = STATUS_MENU_SHOW;
    // } else if (targetMargin == -mMenuWidth) {
    // mMenuStatus = STATUS_MENU_HIDE;
    // }
    // }

    public void showMenu() {
        if (mMenuStatus != STATUS_MENU_SHOW) {
            changeMenuViewMarginAsync(mLeftMarginEdge);
        }
    }

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
        int mOldLeftMargin = mLeftLvParams.leftMargin;
        int mOldRightMargin = mContentLayoutParams.rightMargin;
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

            mLeftLvParams.leftMargin = marginPair.menuLeftMargin;
            mMenuView.setLayoutParams(mLeftLvParams);

            mContentLayoutParams.rightMargin = marginPair.contenteRightMargin;
            mContentView.setLayoutParams(mContentLayoutParams);

            Log.d(VIEW_LOG_TAG, "@@@ value[0]" + values[0] + ", mTarget = " + mTargetMargin
                    + ", mOldLeftMargin = " + mOldLeftMargin
                    + ", mContentLayoutParams.rightMargin = " + mContentLayoutParams.rightMargin);
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

            if (mLeftLvParams.leftMargin == 0) {
                mMenuStatus = STATUS_MENU_SHOW;
            } else if (mLeftLvParams.leftMargin == -mMenuWidth) {
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
