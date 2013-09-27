/* 
 * @(#)ResourceUtils.java    Created on 2013-9-26
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

/**
 * 根据名称来获取资源的resid
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午3:59:14 $
 */
public class ResourceResidUtils {
    public static int getResidByLayoutName(Context context, String name) {
        return getResidByName(context, "layout", name);
    }

    public static int getResidByDimenName(Context context, String name) {
        return getResidByName(context, "dimen", name);
    }

    public static int getResidByDrawableName(Context context, String name) {
        return getResidByName(context, "drawable", name);
    }

    public static int getResidByStringName(Context context, String name) {
        return getResidByName(context, "string", name);
    }

    public static int getResidByStyleName(Context context, String name) {
        return getResidByName(context, "style", name);
    }

    public static int getResidByIdName(Context context, String name) {
        return getResidByName(context, "id", name);
    }

    /**
     * 获取资源resid
     * 
     * @param context
     *            上下文
     * @param className
     *            R文件中内部类名字，例如：drawable
     * @param name
     *            资源名
     * @return
     */
    public static int getResidByName(Context context, String className, String name) {
        String residKey = getResidKey(className, name);
        Integer residInteger = cacheResId.get(residKey);
        if (null != residInteger) {
            return residInteger;
        }
        else {
            String packageName = context.getPackageName();
            Class<?> r = null;
            int resid = 0;
            try {
                r = Class.forName(packageName + ".R");

                Class<?>[] classes = r.getClasses();
                Class<?> desireClass = null;// 想要找到的className类

                for (int i = 0; i < classes.length; ++i) {
                    if (classes[i].getName().split("\\$")[1].equals(className)) {
                        desireClass = classes[i];
                        break;
                    }
                }

                if (desireClass != null) {
                    resid = desireClass.getField(name).getInt(desireClass);
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            cacheResId.put(residKey, resid);
            return resid;
        }
    }

    /**
     * 获取资源resid数组
     * 
     * @param context
     *            上下文
     * @param className
     *            R文件中内部类名字，例如：drawable
     * @param name
     *            资源名
     * @return
     */
    public static int[] getIdsByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class<?> r = null;
        int[] resids = null;
        try {
            r = Class.forName(packageName + ".R");

            Class<?>[] classes = r.getClasses();
            Class<?> desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null)
                    && (desireClass.getField(name).get(desireClass).getClass().isArray())) {
                resids = (int[]) desireClass.getField(name).get(desireClass);
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return resids;
    }

    // ///////////////////////////////////////////////缓存部分///////////////////////////////////////////////////////////
    /**
     * 用来缓存去过的resid，这样第二次取相同的resid就不能在用反射去取了
     */
    private static ConcurrentHashMap<String, Integer> cacheResId = new ConcurrentHashMap<String, Integer>();

    /**
     * 清理缓存
     */
    public void clearCacheResId() {
        cacheResId.clear();
    }

    private static String getResidKey(String className, String name) {
        return className + "-" + name;
    }

}
