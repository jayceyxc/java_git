package com.linus.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuxuecheng
 * @Title: NameMatcher
 * @ProjectName java_git
 * @Description: TODO
 * @date 2018/11/13 16:41
 */


public class NameMatcher {

    private static List<String> names = new ArrayList<>();

    private static void init() {
        names.add("张33");
        names.add("张bb");
        names.add("张aa");
    }

    public static boolean matchName(String name) {
        if (name.contains("*")) {
            name = name.substring(0, 1) + ".*";
        }
        Pattern namePattern = Pattern.compile(name);
        for (String originalName : names) {
            Matcher matcher = namePattern.matcher(originalName);
            System.out.println(matcher.matches());
            if (!matcher.matches()) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        init();
        matchName("张33");
        matchName("张**");
    }
}
