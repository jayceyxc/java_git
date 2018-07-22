package com.linus.tools;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @Author: YuXuecheng
 * @Description
 * @Date: Created in 6:04 PM 2018/7/20
 * @Modified By:
 */

public class CharsetDetector {

    public Charset detectCharset(File f, String[] charsets) {

        Charset charset = null;

        for (String charsetName : charsets) {
            charset = detectCharset(f, Charset.forName(charsetName));
            if (charset != null) {
                System.out.println(charsetName);
            }
        }

        return charset;
    }

    private Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
//        File f = new File("/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/test.csv");
        File f = new File("/Users/yuxuecheng/Work/XLTech/documents/产品相关/病首信息/2017房颤住院病人首页信息_original.csv");
//        f = new File("conf/log4j.properties");
//        f = new File("conf/log4j.properties");

        String[] charsetsToBeTested = {"windows-1253", "ISO-8859-7", "UTF-8", "GBK"};

        CharsetDetector cd = new CharsetDetector();
        Charset charset = cd.detectCharset(f, charsetsToBeTested);
        System.out.println(charset.displayName());

        if (charset != null) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(f), Charset.forName(charset.displayName()));
                int c = 0;
                while ((c = reader.read()) != -1) {
//                    continue;
                    System.out.print((char)c);
                }
                reader.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }

        }else{
            System.out.println("Unrecognized charset.");
        }
    }
}