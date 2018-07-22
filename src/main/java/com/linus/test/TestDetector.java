package com.linus.test;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.IOException;

/**
 * @Author: YuXuecheng
 * @Description
 * @Date: Created in 6:25 PM 2018/7/20
 * @Modified By:
 */

public class TestDetector
{
    public static void main(String[] args) throws IOException {
        byte[] buf = new byte[4096];
        File f = new File("/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2015房颤住院病人首页信息.csv");
//        File f = new File("/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2017房颤住院病人首页信息_original.csv");
        System.out.println(f.getAbsolutePath());
        java.io.InputStream fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(f.getAbsolutePath()));

        // (1)
        UniversalDetector detector = new UniversalDetector();

        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();

        // (4)
        String encoding = detector.getDetectedCharset();
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
        } else {
            System.out.println("No encoding detected.");
        }

        // (5)
        detector.reset();
    }
}