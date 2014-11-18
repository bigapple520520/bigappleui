/* 
 * @(#)AlbumConfig.java    Created on 2014-11-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dazzle.bigappleui.album.entity.BucketImage;

/**
 * 相册的全局配置
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午4:34:27 $
 */
public abstract class AlbumConfig {
    public static final int BACK_FROM_ALUBEN = 2;// 去某个相册获取图片

    /**
     * 相册图片列表
     */
    public static final String PARAM_IMAGELIST = "param.imagelist";
    /**
     * 相册名称
     */
    public static final String PARAM_BUCKETNAME = "param.bucketname";
    /**
     * 表示是否单选
     */
    public static final String PARAM_IF_MULTIPLE_CHOICE = "param.if.multiple.choice";
    /**
     * 设置可选限制参数，如果用户不设置的话，表示是可以选无限个
     */
    public static final String PARAM_LIMIT_COUNT = "param.limit.count";

    /**
     * 选择时临时存放处，用户不应该用到的
     */
    public static Map<String, BucketImage> tempSelMap = new HashMap<String, BucketImage>();
    /**
     * 点击确定后选中的图片，用户应该调用getSelList方法获取，而不是直接使用selList
     */
    public static List<BucketImage> selList = new ArrayList<BucketImage>();

    /**
     * 获取选中的图片
     * 
     * @return
     */
    public static List<BucketImage> getSelListAndClear() {
        List<BucketImage> temp = new ArrayList<BucketImage>(selList);
        selList.clear();
        return temp;
    }

}
