package com.linus.tools;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.*;

public class HankcsAhoCorasickTest {

    private static final Logger logger = Logger.getLogger (HankcsAhoCorasickTest.class);

    private static final String DATA_FILE_PATH = "data";
    private static final String URL_TAGS_FILE_NAME = "url_tags.txt";
    public static final String URL_TAGS_FILE = System.getProperty("user.dir") + System.getProperty("file.separator")
            + DATA_FILE_PATH + System.getProperty("file.separator") + URL_TAGS_FILE_NAME;

    public static final String URL_FILE = System.getProperty("user.dir") + System.getProperty("file.separator")
            + "data" + System.getProperty("file.separator") + "urls.txt";

    private static String urlFormat(String url) {
        if (url.startsWith ("http://")) {
//            System.out.println (url + " startsWith http://");
            url = url.substring (7);
        } else if (url.startsWith ("https://")) {
//            System.out.println (url + " startsWith https://");
            url = url.substring (8);
        }

        if (url.startsWith ("www.")) {
//            System.out.println (url + " startsWith www.");
            url = url.substring (4);
        }

        return url;
    }

    public static AhoCorasickDoubleArrayTrie<List<String>> buildACMachine() {
        try {

            TreeMap<String, List<String>> map = new TreeMap<String, List<String>>();
            BufferedReader reader = new BufferedReader (new FileReader (URL_TAGS_FILE));
            String line = null;
            while ((line = reader.readLine ()) != null) {
                line = line.trim ();
//                System.out.println (line);
                String[] segs = line.split (":", 2);
                if (segs.length == 2) {
                    String urlPattern = urlFormat (segs[0].trim ());
                    String[] tags = segs[1].trim ().split (",");
//                    System.out.println ("put url: " + urlPattern);
                    map.put (urlPattern, Arrays.asList (tags));
                }
            }
            AhoCorasickDoubleArrayTrie<List<String>> acdat = new AhoCorasickDoubleArrayTrie<List<String>> ();
            acdat.build (map);

            return acdat;
        } catch (FileNotFoundException fnfe) {
            logger.error ("File not found");
        } catch (IOException ioe) {
            logger.error ("Read file failed");
        }

        return null;
    }

    public static void main (String[] args) throws Exception {
        PropertyConfigurator.configure ("conf/log4j.properties");
        // Collect test data set
        TreeMap<String, String> map = new TreeMap<String, String>();
        String[] keyArray = new String[]
                {
                        "hers",
                        "his",
                        "she",
                        "he"
                };
        for (String key : keyArray)
        {
            map.put(key, key);
        }
        // Build an AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
        acdat.build(map);
        // Test it
        final String text = "uhers";
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = acdat.parseText(text);
        for (AhoCorasickDoubleArrayTrie<String>.Hit<String> hit : wordList) {
            System.out.println ("begin: " +hit.begin + ", end: " + hit.end + ", value: " + hit.value);
        }

        String url = "http://www.baidu.com";
        System.out.println (urlFormat (url));
        url = "https://mail.163.com";
        System.out.println (urlFormat (url));

        logger.info ("begin buildACMachine");
        AhoCorasickDoubleArrayTrie<List<String>> ac = buildACMachine();
        logger.info ("finish buildACMachine");

        url = "abtest.goforandroid.com/abtestcenter/ab?sid=206&gzip=0&utm_source=null&isupgrade=2&aid=13bbeed18c9a66af&local=ID&cversion=321&pkgname=com.jb.emoji.gokeyboard&cid=4&cdays=17563&entrance=1";
//        url = "mobilesystemservice.com/api_endpoint";
//        url = "abtest.goforandroid.com";
        List<AhoCorasickDoubleArrayTrie<List<String>>.Hit<List<String>>> urlTagList = ac.parseText (url);
        if (urlTagList != null) {
            for (AhoCorasickDoubleArrayTrie<List<String>>.Hit<List<String>> hit : urlTagList) {
                for (String tag : hit.value) {
                    logger.info("\tmatched tag: " + tag + ", begin: " + hit.begin +", end: " + hit.end + ", key: " + url.substring (hit.begin, hit.end));
                }
            }
        }


/*        BufferedReader reader = new BufferedReader (new FileReader (URL_FILE));
        int count = 0;
        while ((url = reader.readLine ()) != null) {
            url = url.trim ();
            List<AhoCorasickDoubleArrayTrie<List<String>>.Hit<List<String>>> tagList = ac.parseText (url);
            if (tagList != null) {
                logger.debug ("url: " + url);
                count++;
                if(count % 1000 == 0) {
                    logger.info ("parsed " + count + " url");
                }
                for (AhoCorasickDoubleArrayTrie<List<String>>.Hit<List<String>> hit : tagList) {
                    for (String tag : hit.value) {
                        logger.debug("\tmatched tag: " + tag);
                    }
                }
            }
        }
        reader.close ();*/
    }
}
