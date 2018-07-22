package com.linus.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloWorld {

    private static SimpleDateFormat secondDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static synchronized Date getDateFromStringSecond(String dateString) {
        try {
            return secondDateFormat.parse(dateString);
        } catch (Exception e) {
            System.err.println(e.getMessage() + "日期转换异常！");
        }
        return null;
    }

    private static Map<String, Double> analysisUltraphonicString(String yxms) {
        Map<String, Double> result = new HashMap<>();
        String[] tokens = yxms.split("\\s");
        String regex = "(\\d*)(\\p{Alpha}*)";
        Pattern valuePattern = Pattern.compile(regex);
        for (String token : tokens) {
            if (token.contains("=")) {
                String[] strs = token.split("=");
                if (strs.length == 2) {
                    try {
                        String key = strs[0].trim();
                        Matcher m = Pattern.compile(regex).matcher(strs[1].trim());
                        if (m.find()) {
                            if (m.group(1).length() > 0) {
                                Double value = Double.valueOf(m.group(1));
//                                System.out.println("key: " + key + ", value: " + value);
                                result.put(key, value);
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("the value is not double: " + token);
                        nfe.printStackTrace();
                    }
                }
            }
        }

        return result;
    }


    public static void main (String[] args) {
        System.out.println ("Hello world");
        String startTime = "2018-06-26 00:00:00";
        String endTime = "2018-06-19 00:00:00";

        Date startDate = getDateFromStringSecond(startTime);
        Date endDate = getDateFromStringSecond(endTime);

        System.out.println(System.getProperty("user.dir"));
        String curPath = System.getProperty("user.dir");
        String fileSeprator = System.getProperty("file.separator");
        String tempPath = curPath + fileSeprator + "temp";
        String compressPath = curPath + fileSeprator + "compress";
        System.out.println(tempPath);
        System.out.println(compressPath);
        File tempPathDir = new File(tempPath);
        File compressPathDir = new File(compressPath);
        System.out.println(tempPathDir.exists());
        System.out.println(compressPathDir.exists());
        if (!tempPathDir.exists()) {
            if (!tempPathDir.mkdirs())
            {
                System.err.println("create temp directory failed;");
                System.exit(-1);
            }
        }
        if (!compressPathDir.exists()) {
            if (!compressPathDir.mkdirs()) {
                System.err.println("create compress directory failed;");
                System.exit(-1);
            }
        }

        String yxms = "超声测量：\n" +
                "  AAOD=33mm(23-32mm)    MPAD=20mm(16-22mm)   LAD=41mm(25-35mm) \n" +
                "  LVDD=44mm(36-52mm)     RAD=38mm(26-42mm)   RVD=19mm(16-22mm)\n" +
                "  IVSD=10mm(6-10mm)    LVPWD=10mm(6-10mm)   LVEF=54% (45-80%)\n" +
                "   AVSV=101cm/s            MVE=116cm/s           \n" +
                "   PVSV=69cm/s\n" +
                "超声所见：\n" +
                "\n" +
                "   升主动脉稍宽，肺动脉不宽，左房增大，余房室内径在正常范围；\n" +
                "\n" +
                "   房室间隔连续性完好，室间隔与左室壁不厚，室壁运动欠协调；\n" +
                "\n" +
                "   二尖瓣形态开放尚可，关闭欠佳；余瓣膜形态启闭未见明显异常。\n" +
                "\n" +
                "   CDFI：二尖瓣可见轻-中度反流；三尖瓣可见微量反流。,";
        Map<String, Double> result =  analysisUltraphonicString(yxms);
        for (Map.Entry<String, Double> entry : result.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
    }
}
