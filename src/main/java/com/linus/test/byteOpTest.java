package com.linus.test;

/**
 * test the bit opertion on byte
 *
 * @author yuxuecheng
 * @version 1.0
 * @contact yuxuecheng@baicdata.com
 * @time 2018 May 15 13:14
 */
public class byteOpTest {

    public static final String INIT = "\u0000";
    public static final byte CREATIVED = 0x01;
    public static final byte SHOWED = 0x02;
    public static final byte CLICKED = 0x04;

    static boolean isValidCreative(String state) {
        return (state.getBytes ()[0] & CREATIVED) == 0;
    }

    static boolean isValidShow(String state) {
        return (state.getBytes ()[0] & SHOWED) == 0;
    }

    static boolean isValidClick(String state) {
        return (state.getBytes ()[0] & CLICKED) == 0;
    }

    static boolean hasCreative(String state) {
        return (state.getBytes ()[0] & CREATIVED) != 0;
    }

    static boolean hasShow(String state) {
        return (state.getBytes ()[0] & SHOWED) != 0;
    }

    static boolean hasClick(String state) {
        return (state.getBytes ()[0] & CLICKED) != 0;
    }

    public static void main (String[] args) {
        String a = INIT;
        System.out.println ("a is valid creative: " + isValidCreative (a));
        System.out.println ("a is valid show: " + isValidShow (a));
        System.out.println ("a is valid click: " + isValidClick(a));
        System.out.println ("a has creative: " + hasCreative (a));
        System.out.println ("a has show: " + hasShow (a));
        System.out.println ("a has click: " + hasClick (a));

        String creatived = new String(new byte[]{(byte)(a.getBytes ()[0] | CREATIVED)});
        System.out.println ("creatived is valid creative: " + isValidCreative (creatived));
        System.out.println ("creatived is valid show: " + isValidShow (creatived));
        System.out.println ("creatived is valid click: " + isValidClick(creatived));
        System.out.println ("creatived has creative: " + hasCreative (creatived));
        System.out.println ("creatived has show: " + hasShow (creatived));
        System.out.println ("creatived has click: " + hasClick (creatived));

        String showed = new String(new byte[]{(byte)(creatived.getBytes ()[0] | SHOWED)});
        System.out.println ("showed is valid creative: " + isValidCreative (showed));
        System.out.println ("showed is valid show: " + isValidShow (showed));
        System.out.println ("showed is valid click: " + isValidClick(showed));
        System.out.println ("showed has creative: " + hasCreative (showed));
        System.out.println ("showed has show: " + hasShow (showed));
        System.out.println ("showed has click: " + hasClick (showed));

        String clicked = new String(new byte[]{(byte)(showed.getBytes ()[0] | CLICKED)});
        System.out.println ("clicked is valid creative: " + isValidCreative (clicked));
        System.out.println ("clicked is valid show: " + isValidShow (clicked));
        System.out.println ("clicked is valid click: " + isValidClick (clicked));
        System.out.println ("showed has creative: " + hasCreative (clicked));
        System.out.println ("showed has show: " + hasShow (clicked));
        System.out.println ("showed has click: " + hasClick (clicked));
    }
}
