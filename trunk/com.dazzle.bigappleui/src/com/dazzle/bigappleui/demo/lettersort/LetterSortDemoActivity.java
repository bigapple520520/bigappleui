package com.dazzle.bigappleui.demo.lettersort;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.view.LetterSortView;

/**
 * 字母排序列表控件demo
 * 
 * @author xuan
 */
public class LetterSortDemoActivity extends Activity {
	private LetterSortView letterSortView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.demo_lettersort_main);

		// 加载数据
		ItemContent ic = new ItemContent("徐安", null);
		List<ItemContent> dataList = new ArrayList<ItemContent>();
		dataList.add(ic);

		ic = new ItemContent("大王", null);
		dataList.add(ic);
		ic = new ItemContent("小王", null);
		dataList.add(ic);
		ic = new ItemContent("红红", null);
		dataList.add(ic);
		ic = new ItemContent("黄黄", null);
		dataList.add(ic);
		ic = new ItemContent("黄小", null);
		dataList.add(ic);
		ic = new ItemContent("黄3", null);
		dataList.add(ic);
		ic = new ItemContent("妮妮", null);
		dataList.add(ic);
		ic = new ItemContent("艹但", null);
		dataList.add(ic);
		ic = new ItemContent("艹小", null);
		dataList.add(ic);
		ic = new ItemContent("而是", null);
		dataList.add(ic);
		ic = new ItemContent("ABC", null);
		dataList.add(ic);
		ic = new ItemContent("ccc", null);
		dataList.add(ic);

		letterSortView = (LetterSortView) findViewById(R.id.letterSortView);

		// 自定义设置中间的字母显示框
		letterSortView.setLetterShow((TextView) LayoutInflater.from(this)
				.inflate(R.layout.demo_lettersort_textview, null));

		letterSortView.getListView().setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// LetterSortBar letterSortBar = new LetterSortBar(this);
		//
		// letterSortBar.setOnLetterChange(new OnLetterChange() {
		// @Override
		// public void letterChange(String letter) {
		// letterSortView.getLetterShow().setText(letter);
		// if (letterSortView.getLetterShow().getVisibility() != View.VISIBLE) {
		// letterSortView.getLetterShow().setVisibility(View.VISIBLE);
		// }
		//
		// // 定位ListView的显示区域
		// LetterSortAdapter lsa = (LetterSortAdapter)
		// letterSortView.getListView().getAdapter();
		// Integer indexInteger = lsa.getIndexMap().get(letter);
		// final int index = (null == indexInteger) ? -1 : indexInteger;
		//
		// // TODO:怎么这句话不灵啊。。。。。
		// letterSortView.getListView().setSelection(index);
		// letterSortView.getListView().requestFocusFromTouch();
		// }
		// });
		//
		// letterSortBar.setOutLetterSeacherBar(new OutLetterSeacherBar() {
		// @Override
		// public void outBar(String lastLetter) {
		// letterSortView.getLetterShow().setVisibility(View.GONE);
		// }
		// });
		// letterSortView.setLetterSortBar(letterSortBar);

		letterSortView.getListView().setDividerHeight(0);
		letterSortView.getListView().setAdapter(
				new LetterSortDemoAdapter(dataList, this));
	}

}
