/* 
 * @(#)LetterItemContent.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.entity;

/**
 * 列表项目的内容
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:33:31 $
 */
public class ItemContent extends BaseItem {
    private int tag;// 区分不同对象可用

    private String name;// 内容项显示的名字
    private Object value;// 内容的其他值对象

    public ItemContent(String name) {
        this.name = name;
    }

    public ItemContent(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
