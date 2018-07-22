package com.linus.test;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseTest {
    public static void main(String[] args) {
        String s = new String("我是中国人");
        System.out.println(checkChinese(s));

        String s2 = new String("我是中国人new");
        System.out.println(checkChinese(s2));

//        String s3 = "averg fgf王可可fdbsb额的办公室df12rbs23bh";
        String s3 = "hello英文 world中文 test";
        String []tokens = StringUtils.split(s3);

        String regex = "(\\p{Alpha}*)([\\u4e00-\\u9fa5]*)(\\d*)";
        Pattern chinesePattern = Pattern.compile("[\u3007\u4E00-\u9FCB\uE815-\uE864]");
        for (String token : tokens) {
            Matcher m = Pattern.compile(regex).matcher(token);
            while (m.find()) {
                if (m.group(1).length() > 0)
                    System.out.println(m.group(1));
                if (m.group(2).length() > 0) {
//                    System.out.println(m.group(2));
                    Matcher cm = chinesePattern.matcher(m.group(2));
                    while (cm.find()) {
                        System.out.println(cm.group());
                    }
                }
                if (m.group(3).length() > 0)
                    System.out.println(m.group(3));
            }
        }
    }



    public static String checkChinese(String str){
        String sb = new String();
        Pattern pattern = Pattern.compile("[\u3007\u4E00-\u9FCB\uE815-\uE864]");//只匹配一个中文字符
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            sb += matcher.group()+";";
        }
        return sb.toString();
    }
}
