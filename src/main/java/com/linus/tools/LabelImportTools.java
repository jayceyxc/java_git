package com.linus.tools;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LabelImportTools {

    private static final Logger log = Logger.getLogger (LabelImportTools.class);

    public static void main (String[] args) {
        PropertyConfigurator.configure ("conf/log4j.properties");
        Options options = new Options ();
        options.addOption ("f", "file", true, "The file name of user tags, required");
        options.addOption (null, "host", true, "The host of the user tag redis, required");
        options.addOption (null, "port", true, "The port of the user tag redis, required");
        options.addOption (null, "db", true, "The db index of the user tag redis, required");
        options.addOption (null, "password", true, "The password of the user tag redis");
        CommandLine cl = null;
        try {
            cl = new DefaultParser ().parse (options, args);
        } catch (ParseException e) {
            log.error (e);
        }

        if (cl == null || !cl.hasOption ("f") || !cl.hasOption ("file") || !cl.hasOption ("host") || !cl.hasOption ("port") || !cl.hasOption ("db")) {
            new HelpFormatter ().printHelp ("java [options] -f [user_tag_file]", options);
            return;
        }

        String fileName = cl.getOptionValue ("f");
        String redisHost = cl.getOptionValue ("host");
        int redisPort = Integer.valueOf (cl.getOptionValue ("port"));
        int redisDb = Integer.valueOf (cl.getOptionValue ("db"));
        String password = "";
        if (cl.hasOption ("password")) {
            password = cl.getOptionValue ("password");
        }
        log.info ("user tag file name: " + fileName);
        log.info ("redis host: " + redisHost + ", port: " + redisPort + ", db: " + redisDb + ", password" + password);

        Jedis jedis = new Jedis (redisHost, redisPort);
        if (!password.isEmpty ()) {
            jedis.auth (password);
        }
        jedis.select (redisDb);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader (new FileReader (fileName));
            String line;
            while ((line = reader.readLine ()) != null) {
                line = line.trim ();
                String segs[] = line.split ("\t");
                String adsl = segs[0].trim();
                String tagValueString = segs[1].trim ();
                for (String tagValue : tagValueString.split (",")) {
                    String tagSegs[] = tagValue.split (":");
                    if (tagSegs.length == 2) {
                        String tag = tagSegs[0].trim ();
                        jedis.sadd (tag, adsl);
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            log.error ("file " + fileName + " not exist");
        } catch (IOException ioe) {
            log.error ("read file failed");
        } finally {
            if (reader != null) {
                try {
                    reader.close ();
                } catch (IOException ioe) {
                    log.error ("close reader failed");
                }
            }
        }
    }
}
