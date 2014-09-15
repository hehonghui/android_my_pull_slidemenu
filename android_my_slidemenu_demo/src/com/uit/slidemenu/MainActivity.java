/**
 *
 *	created by Mr.Simple, Sep 12, 201411:18:06 AM.
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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.slidemenu.SlideMenu.OnSlideMenuListener;

/**
 * @author mrsimple
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SlideMenu slideMenu = new SlideMenu(this);

        MenuView menuView = new MenuView(this);
        menuView.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 1"));
        menuView.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 2"));
        menuView.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 3"));
        menuView.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 4"));
        // });
        slideMenu.setOnSlideMenuListener(new OnSlideMenuListener() {

            @Override
            public void onMenuStateChange(int state) {
                if (state == SlideMenu.MENU_HIDE) {
                    Toast.makeText(getApplicationContext(), "menu close",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "menu open ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
        // slideMenu.setMenuView(menuView);

        TextView menu = new TextView(this);
        menu.setTextSize(30);
        menu.setGravity(Gravity.CENTER);
        menu.setText("Menu View");
        menu.setBackgroundColor(Color.RED);
        //
//        slideMenu.setMenuView(menu);

        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setText("Content view");
        textView.setGravity(Gravity.CENTER);
        //
//        slideMenu.setContentView(textView);

        //
        slideMenu.attachToActivity(this);

    }
}
