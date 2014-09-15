/**
 *
 *	created by Mr.Simple, Sep 13, 20148:53:24 PM.
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrsimple
 */
public class MenuView extends RelativeLayout {

    private ListView mListView;
    /**
     * 
     */
    private List<MenuItem> mMenuItems = new ArrayList<MenuItem>();

    /**
     * @param context
     */
    public MenuView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    /**
     * 
     */
    private void initLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.menu_view, this, true);

        mListView = (ListView) findViewById(R.id.menu_listview);
        mListView.setAdapter(new MenuItemAdapter());
    }

    /**
     * @param menu
     */
    public void addMenuItem(MenuItem menu) {
        mMenuItems.add(menu);
    }

    /**
     * @param menu
     */
    public void addMenuItem(List<MenuItem> menus) {
        mMenuItems.clear();
        mMenuItems = menus;
    }

    public void name() {

    }

    /**
     * @author mrsimple
     */
    class MenuItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuItems.size();
        }

        @Override
        public MenuItem getItem(int position) {
            return mMenuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.menu_listview_item, parent,
                        false);
                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.menu_item_iv);
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.menu_item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MenuItem item = getItem(position);
            viewHolder.mImageView.setImageResource(item.mItemDrawableResId);
            viewHolder.mTextView.setText(item.mItemName);
            return convertView;
        }

    } //

    /**
     * @author mrsimple
     */
    static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }

}
