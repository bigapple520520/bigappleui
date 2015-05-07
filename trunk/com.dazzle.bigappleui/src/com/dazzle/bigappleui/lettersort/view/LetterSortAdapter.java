/* 
 * @(#)LetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.view;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dazzle.bigappleui.lettersort.entity.BaseItem;
import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;
import com.dazzle.bigappleui.lettersort.utils.LetterSortUtils;

/**
 * 数据适配器，使用了模板方法设计模式
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:10:58 $
 */
public abstract class LetterSortAdapter extends BaseAdapter {
    private Context context;
    private List<BaseItem> itemList;// 数据列表
    private final HashMap<String, Integer> indexMap = new HashMap<String, Integer>();// 存放字母的索引位置

    // 初始化一些自定义常量
    public LetterSortAdapter(List<ItemContent> fromList, Context context) {
        this.context = context;
        itemList = LetterSortUtils.sortOrderLetter(fromList, context);
        initIndexMap(itemList);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseItem bi = itemList.get(position);
        if (bi instanceof ItemContent) {
            convertView = drawItemContent(position, convertView, parent, (ItemContent) bi);
        }
        else if (bi instanceof ItemDivide) {
            convertView = drawItemDivide(position, convertView, parent, (ItemDivide) bi);
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 刷新设配器
     * 
     * @param fromItem
     */
    public void notifyDataSetChanged(List<ItemContent> fromList) {
        itemList = LetterSortUtils.sortOrderLetter(fromList, context);
        initIndexMap(itemList);
        super.notifyDataSetChanged();
    }

    private void initIndexMap(List<BaseItem> fromList) {
        for (int i = 0, n = fromList.size(); i < n; i++) {
            BaseItem bi = fromList.get(i);
            if (bi instanceof ItemDivide) {
                ItemDivide id = (ItemDivide) bi;
                indexMap.put(id.getLetter(), i);
            }
        }
    }

    public HashMap<String, Integer> getIndexMap() {
        return indexMap;
    }

    /**
     * 自定义实现绘制普通View
     * 
     * @param position
     * @param convertView
     * @param parent
     * @param itemContent
     * @return
     */
    public abstract View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent);

    /**
     * 自定义实现绘制分割线View
     * 
     * @param position
     * @param convertView
     * @param parent
     * @param itemDivide
     * @return
     */
    public abstract View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide);

}
