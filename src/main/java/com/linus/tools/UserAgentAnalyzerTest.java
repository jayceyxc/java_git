package com.linus.tools;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserAgentAnalyzerTest {
    public static void main (String[] args) {
        List<String> needFields = new ArrayList<> (Arrays.asList (UserAgent.DEVICE_CLASS, UserAgent.DEVICE_BRAND, UserAgent.DEVICE_NAME));
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder ().hideMatcherLoadStats ().withCache (25000).withFields (needFields).build ();
        UserAgent agent = userAgentAnalyzer.parse("Mozilla/5.0 (Linux; Android 6.0.1; SAMSUNG SM-G532G Build/MMB29T) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.2 Chrome/44.0.2403.133 Mobile Safari/537.36");
//        for (String fieldName: agent.getAvailableFieldNamesSorted()) {
//            System.out.println(fieldName + " = " + agent.getValue(fieldName));
//        }
        System.out.printf ("class: %s, brand: %s, name: %s\r\n", agent.getValue (UserAgent.DEVICE_CLASS), agent.getValue (UserAgent.DEVICE_BRAND), agent.getValue (UserAgent.DEVICE_NAME));

        String fileName = "only_user_agent.txt";
        String fullFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "data" + System.getProperty("file.separator") + fileName;
        try {
            BufferedReader reader = new BufferedReader (new FileReader (new File (fullFileName)));
            String line;
            while ((line = reader.readLine ()) != null){
                line = line.trim ();
                agent = userAgentAnalyzer.parse (line);
                System.out.println ("full user agent: " + line);
                System.out.printf ("class: %s, brand: %s, name: %s\r\n", agent.getValue (UserAgent.DEVICE_CLASS), agent.getValue (UserAgent.DEVICE_BRAND), agent.getValue (UserAgent.DEVICE_NAME));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace ();
        } catch (IOException ioe) {
            ioe.printStackTrace ();
        }
    }
}
