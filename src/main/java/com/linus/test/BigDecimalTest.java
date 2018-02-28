package com.linus.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalTest {
    public static void main (String[] args) {
        BigDecimal show = new BigDecimal (938);
        BigDecimal unit_price = new BigDecimal ("0.938").divide (show);
        BigInteger show_value = show.multiply (new BigDecimal (0.8)).toBigInteger ();
        show = BigDecimal.valueOf (show_value.longValue ());
        System.out.println (show);
        BigDecimal cost = unit_price.multiply (show);
        System.out.println (cost);
    }
}
