/* 
 * @(#)PinyinUtil.java    Created on 2014-5-6
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.text.TextUtils;

/**
 * 拼音使用工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-6 下午12:56:34 $
 */
public abstract class PinyinUtil {
    /**
     * 中文转拼音，如果是单个字母或者数字不变
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyin(Context context, char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
            // 字母或者数字原字返回
            return String.valueOf(c);
        }

        if (c == 0x3007) {
            return "ling";
        }

        if (c < 4E00 || c > 0x9FA5) {
            return null;
        }

        RandomAccessFile is = null;
        try {
            is = new RandomAccessFile(PinyinSource.getFile(context), "r");
            long sp = (c - 0x4E00) * 6;
            is.seek(sp);
            byte[] buf = new byte[6];
            is.read(buf);
            return new String(buf).trim();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != is) {
                    is.close();
                }
            }
            catch (IOException e) {
                // Ignore
            }
        }

        return null;
    }

    /**
     * 首字母，原
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinF(Context context, char c) {
        String pinyin = toPinyin(context, c);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

    /**
     * 字节转拼音，小写
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinLower(Context context, char c) {
        String pinyin = toPinyin(context, c);
        if (null == pinyin) {
            return null;
        }

        return pinyin.toLowerCase();
    }

    /**
     * 取首拼，小写
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinLowerF(Context context, char c) {
        String pinyin = toPinyinLower(context, c);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

    /**
     * 字节转拼音，大写
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinUpper(Context context, char c) {
        String pinyin = toPinyin(context, c);
        if (null == pinyin) {
            return null;
        }

        return pinyin.toUpperCase();
    }

    /**
     * 取首拼，大写
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinUpperF(Context context, char c) {
        String pinyin = toPinyinUpper(context, c);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

    // /////////////////////////////////////////字符串////////////////////////////////////////////////////////////

    /**
     * 中文转拼音，如果是单个字母或者数字不变
     * 
     * @param context
     * @param hanzi
     * @return
     */
    public static String toPinyin(Context context, String hanzi) {
        if (TextUtils.isEmpty(hanzi)) {
            return null;
        }

        StringBuffer sb = new StringBuffer("");
        RandomAccessFile is = null;
        try {
            is = new RandomAccessFile(PinyinSource.getFile(context), "r");
            for (int i = 0; i < hanzi.length(); i++) {
                char ch = hanzi.charAt(i);
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                    sb.append(ch);
                    continue;
                }

                if (ch == 0x3007) {
                    sb.append("ling").append(' ');
                }
                else if (ch >= 0x4E00 || ch <= 0x9FA5) {
                    long sp = (ch - 0x4E00) * 6;
                    is.seek(sp);
                    byte[] buf = new byte[6];
                    is.read(buf);
                    sb.append(new String(buf).trim()).append(' ');
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != is) {
                    is.close();
                }
            }
            catch (IOException e) {
                // Ignore
            }
        }
        return sb.toString().trim();
    }

    /**
     * 首字母，原
     * 
     * @param context
     * @param c
     * @return
     */
    public static String toPinyinF(Context context, String hanzi) {
        String pinyin = toPinyin(context, hanzi);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

    /**
     * 字符串转拼音，小写
     * 
     * @param context
     * @param hanzi
     * @return
     */
    public static String toPinyinLower(Context context, String hanzi) {
        String pinyin = toPinyin(context, hanzi);
        if (null == pinyin) {
            return null;
        }

        return pinyin.toLowerCase();
    }

    /**
     * 取首拼，小写
     * 
     * @param context
     * @param hanzi
     * @return
     */
    public static String toPinyinLowerF(Context context, String hanzi) {
        String pinyin = toPinyinLower(context, hanzi);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

    /**
     * 字符串转拼音，大写
     * 
     * @param context
     * @param hanzi
     * @return
     */
    public static String toPinyinUpper(Context context, String hanzi) {
        String pinyin = toPinyin(context, hanzi);
        if (null == pinyin) {
            return null;
        }

        return pinyin.toUpperCase();
    }

    /**
     * 取首拼，大写
     * 
     * @param context
     * @param hanzi
     * @return
     */
    public static String toPinyinUpperF(Context context, String hanzi) {
        String pinyin = toPinyinUpper(context, hanzi);
        if (null == pinyin) {
            return null;
        }

        return pinyin.substring(0, 1);
    }

}
