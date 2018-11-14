package com.linus.tools;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomString {
    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
        String s = RandomStringUtils.randomAlphanumeric(24);
        System.out.println(s);
    }
}
