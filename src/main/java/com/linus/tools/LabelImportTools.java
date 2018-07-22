package com.linus.tools;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

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
        long addedCount = 0;
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
                        tag = StringUtils.stripStart (tag, "0");
                        if (1 == jedis.sadd (tag, adsl)) {
                            addedCount++;
                        }
                    }
                }
                if (addedCount % 1000 == 0) {
                    log.info ("Finished inserted " + addedCount + " records");
                }
            }
        } catch (FileNotFoundException fnfe) {
            log.error ("file " + fileName + " not exist");
        } catch (IOException ioe) {
            log.error ("read file failed");
        } catch (JedisConnectionException jce) {
            log.error ("redis connection error", jce);
        } finally {
            if (reader != null) {
                try {
                    reader.close ();
                } catch (IOException ioe) {
                    log.error ("close reader failed");
                }
            }
            if (jedis != null) {
                try {
                    jedis.close ();
                } catch (JedisConnectionException jce) {
                    log.error ("close redis connection error", jce);
                }
            }
        }
    }
}
