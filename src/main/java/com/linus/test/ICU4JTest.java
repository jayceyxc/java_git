package com.linus.test;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author: YuXuecheng
 * @Description
 * @Date: Created in 6:31 PM 2018/7/20
 * @Modified By:
 */
public class ICU4JTest {
    public static String detectFileEncoding(String fileName) throws IOException {
//        File file = new File("/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2015房颤住院病人首页信息.csv");
        File file = new File(fileName);
        String charset = "ISO-8859-1"; //Default chartset, put whatever you want

        byte[] fileContent = null;
        FileInputStream fin = null;

        //create FileInputStream object
        fin = new FileInputStream(file.getPath());

        /*
         * Create byte array large enough to hold the content of the file.
         * Use File.length to determine size of the file in bytes.
         */
        fileContent = new byte[(int) file.length()];

        /*
         * To read content of the file in byte array, use
         * int read(byte[] byteArray) method of java FileInputStream class.
         *
         */
        fin.read(fileContent);

        byte[] data =  fileContent;

        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);

        CharsetMatch cm = detector.detect();

        if (cm != null) {
            int confidence = cm.getConfidence();
            System.out.println("Encoding: " + cm.getName() + " - Confidence: " + confidence + "%");
            //Here you have the encode name and the confidence
            //In my case if the confidence is > 50 I return the encode, else I return the default value
            if (confidence > 50) {
                charset = cm.getName();
            }
        }

        return charset;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2015房颤住院病人首页信息.csv";
//        String fileName = "/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2017房颤住院病人首页信息_original.csv";
        String charsetName = detectFileEncoding(fileName);
        System.out.println(fileName + " encoding: " + charsetName);
    }
}
