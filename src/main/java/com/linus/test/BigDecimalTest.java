package com.linus.test;

import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class BigDecimalTest {

    private static void normalization (Map<String, BigDecimal> tagValueMap) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal weight : tagValueMap.values ()) {
            sum = sum.add (BigDecimal.valueOf(Math.sqrt (weight.doubleValue ())));
        }
        System.err.println (String.format ("map size: %d, sum: %f", tagValueMap.size (), sum.doubleValue ()));

        if (sum.compareTo (BigDecimal.ZERO) > 0) {
            //这里将map.entrySet()转换成list
            List<Map.Entry<String, BigDecimal>> list = new ArrayList<Map.Entry<String, BigDecimal>> (tagValueMap.entrySet ());

            //然后通过比较器来实现排序
            list.sort (new Comparator<Map.Entry<String, BigDecimal>> () {
                // 按value值降序排序，如果要升序，将o2和o1位置互换
                @Override
                public int compare (Map.Entry<String, BigDecimal> o1, Map.Entry<String, BigDecimal> o2) {
                    return o2.getValue ().compareTo (o1.getValue ());
                }
            });

            for (Map.Entry<String, BigDecimal> mapping : list) {
//                System.err.println (String.format ("normalization. %s:%f", mapping.getKey (), BigDecimal.valueOf (Math.sqrt (mapping.getValue ().doubleValue ())).divide (sum, 2, BigDecimal.ROUND_HALF_UP).doubleValue ()));
                tagValueMap.put (mapping.getKey (), BigDecimal.valueOf (Math.sqrt (mapping.getValue ().doubleValue ())).divide (sum, 2, BigDecimal.ROUND_HALF_UP));
            }
        }
    }

    public static void main (String[] args) {
        BigDecimal show = new BigDecimal (938);
        BigDecimal unit_price = new BigDecimal ("0.938").divide (show);
        BigInteger show_value = show.multiply (new BigDecimal (0.8)).toBigInteger ();
        show = BigDecimal.valueOf (show_value.longValue ());
        System.out.println (show);
        BigDecimal cost = unit_price.multiply (show);
        System.out.println (cost);

        Map<String, BigDecimal> map = new HashMap<String, BigDecimal> ();
        map.put ("hello", BigDecimal.ONE);
        map.put ("world", BigDecimal.valueOf (5));
        map.put ("welcome", BigDecimal.valueOf (8));
        map.put ("linux", BigDecimal.valueOf (3));
        map.put ("windows", BigDecimal.valueOf (11));

        /*
        //这里将map.entrySet()转换成list
        List<Map.Entry<String,BigDecimal>> list = new ArrayList<Map.Entry<String,BigDecimal>> (map.entrySet());

        //然后通过比较器来实现排序
        list.sort (new Comparator<Map.Entry<String,BigDecimal>>() {
            //升序排序
            public int compare(Map.Entry<String, BigDecimal> o1,
                               Map.Entry<String, BigDecimal> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for(Map.Entry<String,BigDecimal> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }
        */

        normalization (map);
        for (Map.Entry<String, BigDecimal> mapping: map.entrySet ()) {
            System.out.println (mapping.getKey()+":"+mapping.getValue());
        }
    }
}
