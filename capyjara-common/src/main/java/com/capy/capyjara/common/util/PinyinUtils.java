package com.capy.capyjara.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.lang3.StringUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinUtils {

    public static String getChineseInitialsUpperCase(String chinese, boolean isFull) {
        return convertChinese2Pinyin(chinese, isFull).toUpperCase();
    }


    public static String getChineseInitialsLowerCase(String chinese, boolean isFull) {
        return convertChinese2Pinyin(chinese, isFull).toLowerCase();
    }

    private static String convertChinese2Pinyin(String chinese, boolean isFull) {
        /***
         * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
         * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
         * ^[\u4E00-\u9FA5]+$ 匹配简体
         */
        String regExp = "^[\u4E00-\u9FFF]+$";
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isBlank(chinese)) {
            return "";
        }
        String pinyin = "";
        for (int i = 0; i < chinese.length(); i++) {
            char unit = chinese.charAt(i);
            //是汉字，则转拼音
            if (matchLanguage(String.valueOf(unit), regExp)) {
                pinyin = convertSingleChinese2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 将单个汉字转成拼音
     *
     * @param chinese 汉字字符
     * @return 拼音
     */
    private static String convertSingleChinese2Pinyin(char chinese) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuffer sb = new StringBuffer();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(chinese, outputFormat);
            sb.append(res[0]);//对于多音字，只用第一个拼音
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

    /***
     * 匹配
     * <P>
     * 根据字符和正则表达式进行匹配
     *
     * @param str 源字符串
     * @param regex 正则表达式
     *
     * @return true：匹配成功  false：匹配失败
     */
    private static boolean matchLanguage(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
