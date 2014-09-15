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
import android.os.Bundle;
import android.widget.Toast;

import com.uit.slidemenu.SlideMenu.OnSlideMenuListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SlideMenu slideMenu = new SlideMenu(this);
        slideMenu.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 1"));
        slideMenu.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 2"));
        slideMenu.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 3"));
        slideMenu.addMenuItem(new MenuItem(R.drawable.ic_launcher, "menu - 4"));
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
        setContentView(slideMenu);

    }

}
