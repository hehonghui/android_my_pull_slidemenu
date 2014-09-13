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
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uit.slidemenu.MenuView.OnSlideMenuListener;

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
    /**
     * // *
     */
    private TextView mContentView;
    /**
     * 
     */
    MarginLayoutParams mContentLayoutParams;

    protected int mTouchSlop;

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
        // mMenuView.getViewTreeObserver().addOnGlobalLayoutListener(new
        // OnGlobalLayoutListener() {
        //
        // @Override
        // public void onGlobalLayout() {
        // mLeftLvParams = (MarginLayoutParams) mMenuView.getLayoutParams();
        // // hide the menu view
        // mLeftLvParams.leftMargin = -mScreenWidth + 100;
        // mMenuView.setLayoutParams(mLeftLvParams);
        //
        // Log.d(VIEW_LOG_TAG, "### screen width = " + mScreenWidth);
        // //
        // mMenuView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        // }
        // });
        mMenuView.setId(123);
        this.addView(mMenuView, 0);
        mMenuView.setOnTouchListener(this);

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
                if (mLeftLvParams.leftMargin == 0) {
                    hideMenuView(-mLeftLvParams.width);
                }
            }
        });
        mContentView.setTextSize(30f);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, mMenuView.getId());
        mContentView.setBackgroundColor(Color.CYAN);
        this.addView(mContentView, layoutParams);

        mContentView.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            mLeftLvParams = (MarginLayoutParams) mMenuView.getLayoutParams();
            // hide the menu view
            mLeftLvParams.width = (int) (mScreenWidth * 0.6f);
            mLeftLvParams.leftMargin = -mLeftLvParams.width;
            mMenuView.setLayoutParams(mLeftLvParams);

            Log.d(VIEW_LOG_TAG, "### screen width = " + mScreenWidth);
        }
    }

    /**
     * @param listener
     */
    public void setOnSlideMenuListener(OnSlideMenuListener listener) {
        mMenuView.mSlideMenuListener = listener;
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
    private void hideMenuView(int leftMargin) {
        mLeftLvParams.leftMargin = leftMargin;
        mMenuView.setLayoutParams(mLeftLvParams);
    }

    int xDown;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                int distance = (int) (event.getRawX() - xDown);
                if (Math.abs(distance) > mTouchSlop) {
                    if (mLeftLvParams.leftMargin < mLeftMarginEdge) {
                        hideMenuView(0);
                    } else if (mLeftLvParams.leftMargin == 0 && distance < 0) {
                        hideMenuView(-mLeftLvParams.width);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return false;
    }
}
