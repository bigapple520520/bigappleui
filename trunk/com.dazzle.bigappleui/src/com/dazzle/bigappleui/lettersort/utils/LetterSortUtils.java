/* 
 * @(#)LetterSortUtils.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.dazzle.bigappleui.lettersort.entity.BaseItem;
import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;

/**
 * 给列表数据排序用
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:30:18 $
 */
public abstract class LetterSortUtils {

    /**
     * 根据字母排序，并加入了字母分割线哦
     * 
     * @param fromItem
     * @return
     */
    public static List<BaseItem> sortOrderLetter(List<ItemContent> fromItemList, Context context) {
        if (null == fromItemList || fromItemList.isEmpty()) {
            return Collections.emptyList();
        }

        List<BaseItem> ret = new ArrayList<BaseItem>();

        Map<String, List<BaseItem>> letter2ItemListMap = groupByFirstLetter(fromItemList, context);

        // 拎出无法识别的name，删除之，因为要放在最后
        List<BaseItem> unknowNameList = letter2ItemListMap.get("#");
        if (null != unknowNameList) {
            letter2ItemListMap.remove("#");
        }

        // 遍历正常的，并按字母排序，使用了选择排序的算法
        for (Entry<String, List<BaseItem>> entry : letter2ItemListMap.entrySet()) {
            String key = entry.getKey();
            List<BaseItem> itemList = entry.getValue();

            if (ret.isEmpty()) {
                ret.add(new ItemDivide(key));
                for (BaseItem item : itemList) {
                    ret.add(item);
                }
            }
            else {
                ItemDivide itemDivide = (ItemDivide) ret.get(0);
                if (key.charAt(0) > itemDivide.getLetter().charAt(0)) {
                    ret.add(new ItemDivide(key));
                    for (BaseItem item : itemList) {
                        ret.add(item);
                    }
                }
                else {
                    for (BaseItem item : itemList) {
                        ret.add(0, item);
                    }
                    ret.add(0, new ItemDivide(key));
                }
            }
        }

        // 在后面添加上无法识别的名字
        if (null != unknowNameList) {
            ret.add(new ItemDivide("#"));
            for (BaseItem item : unknowNameList) {
                ret.add(item);
            }
        }

        return ret;
    }

    /**
     * 按首字母分类,Map返回
     * 
     * @param baseMenuDtoList
     * @return
     */
    private static Map<String, List<BaseItem>> groupByFirstLetter(List<ItemContent> fromItemList, Context context) {
        Map<String, List<BaseItem>> ret = new HashMap<String, List<BaseItem>>();

        for (ItemContent item : fromItemList) {
            String firstLetter = PinyinUtil.toPinyinUpperF(context, item.getName());

            List<BaseItem> itemList = ret.get(firstLetter);
            if (null == itemList) {
                itemList = new ArrayList<BaseItem>();
                ret.put(firstLetter, itemList);
            }

            itemList.add(item);
        }

        return ret;
    }

}
