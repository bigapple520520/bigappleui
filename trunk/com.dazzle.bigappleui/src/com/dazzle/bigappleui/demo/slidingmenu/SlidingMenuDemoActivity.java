package com.dazzle.bigappleui.demo.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnCloseListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnClosedListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenedListener;

/**
 * 侧滑控件demo
 * 
 * @author xuan
 */
public class SlidingMenuDemoActivity extends Activity {
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_slidingmenu_main);

		// 侧滑主界面
		View above = LayoutInflater.from(this).inflate(
				R.layout.demo_slidingmenu_above, null);
		TextView aboveText = (TextView) above.findViewById(R.id.aboveText);

		// 侧滑菜单左边的界面
		View menuLeft = LayoutInflater.from(this).inflate(
				R.layout.demo_slidingmenu_menu_left, null);
		View menuRight = LayoutInflater.from(this).inflate(
				R.layout.demo_slidingmenu_menu_right, null);

		// 设置侧滑界面参数
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 左右都可以侧滑模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏都可以触发侧滑（指定view除外）

		slidingMenu.setContent(above);// 把三个界面放入侧滑容器里
		slidingMenu.setMenu(menuLeft);
		slidingMenu.setSecondaryMenu(menuRight);

		// 设置侧滑的偏移量直接数字的话单位是px，可以用dimens文件的id设置
		slidingMenu.setBehindOffset(50);
		slidingMenu.setBehindScrollScale(0);// 背景菜单出现比例，0表示不动

		// 忽略不产生侧滑效果的子界面
		slidingMenu.addIgnoredView(aboveText);

		// 左边菜单界面，侧边的渐变效果
		slidingMenu
				.setShadowDrawable(R.drawable.demo_slidingmenu_menuleft_shadow);
		slidingMenu.setShadowWidth(30);

		// 选中view的效果，暂时还没有知道是什么效果
		// slidingMenu.setSelectorEnabled(true);
		// slidingMenu.setSelectedView(menuLeft);
		// slidingMenu.setSelectorDrawable(R.drawable.demo_slidingmenu_menuleft_shadow);

		// 各种事件
		slidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				Toast.makeText(SlidingMenuDemoActivity.this, "我侧滑到了左边菜单界面",
						Toast.LENGTH_SHORT).show();
			}
		});

		slidingMenu.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				Toast.makeText(SlidingMenuDemoActivity.this, "我侧滑到了主界面",
						Toast.LENGTH_SHORT).show();
			}
		});

		slidingMenu.setSecondaryOnOpenListner(new OnOpenListener() {
			@Override
			public void onOpen() {
				Toast.makeText(SlidingMenuDemoActivity.this, "我侧滑到了右边边菜单界面",
						Toast.LENGTH_SHORT).show();
			}
		});

		slidingMenu.setOnOpenedListener(new OnOpenedListener() {
			@Override
			public void onOpened() {
				// 每次滑动过后，如果状态在左边或者右边菜单处，就会调用这个，即使原本就在左边或者右边菜单处，
				// 然后进过了用户的侧滑操作后还是在这个左边或者右边菜单处
				// 此也会被调用的
			}
		});

		slidingMenu.setOnClosedListener(new OnClosedListener() {
			@Override
			public void onClosed() {
				// 每次滑动过后，如果状态在主界面处，就会调用这个，即使原本就在主界面，然后进过了用户的侧滑操作后还是在这个主界面
				// 此也会被调用的
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}

}
