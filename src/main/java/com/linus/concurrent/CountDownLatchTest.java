package com.linus.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

abstract class BaseHealthChecker implements Runnable {
    private CountDownLatch latch;
    private String serviceName;
    private boolean serviceUp;

    //Get latch object in constructor so that after completing the task, thread can countDown() the latch
    BaseHealthChecker(String serviceName, CountDownLatch latch) {
        super();
        this.latch = latch;
        this.serviceName = serviceName;
        this.serviceUp = false;
    }

    @Override
    public void run() {
        try {
            verifyService ();
            serviceUp = true;
        } catch (Throwable e) {
            e.printStackTrace ();
            this.serviceUp = false;
        } finally {
            if (latch != null) {
                latch.countDown ();
            }
        }
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public boolean isServiceUp() {
        return this.serviceUp;
    }

    //This methods needs to be implemented by all specific service checker
    abstract void verifyService();
}

class NetworkHealthChecker extends BaseHealthChecker {
    public NetworkHealthChecker(CountDownLatch latch) {
        super("Network Service", latch);
    }

    @Override
    void verifyService () {
        System.out.println ("Checking " + this.getServiceName ());
        try {
            Thread.sleep (7000);
        } catch (InterruptedException ie) {
            ie.printStackTrace ();
        }
        System.out.println (this.getServiceName () + " is UP");
    }
}

class DatabaseHealthChecker extends BaseHealthChecker {
    public DatabaseHealthChecker(CountDownLatch latch) {
        super("Database Service", latch);
    }

    @Override
    void verifyService () {
        System.out.println ("Checking " + this.getServiceName ());
        try {
            Thread.sleep (5000);
        } catch (InterruptedException ie) {
            ie.printStackTrace ();
        }
        System.out.println (this.getServiceName () + " is UP");
    }
}

class CacheHealthChecker extends BaseHealthChecker {
    public CacheHealthChecker(CountDownLatch latch) {
        super("Cache Service", latch);
    }

    @Override
    void verifyService () {
        System.out.println ("Checking " + this.getServiceName ());
        try {
            Thread.sleep (3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace ();
        }
        System.out.println (this.getServiceName () + " is UP");
    }
}

class ApplicationStartupUtil {
    // List of service checkers
    private static List<BaseHealthChecker> services;

    // This latch will be userd to wait on
    private static CountDownLatch latch;

    private ApplicationStartupUtil() {

    }

    private final static ApplicationStartupUtil INSTANCE = new ApplicationStartupUtil ();

    public static ApplicationStartupUtil getInstance() {
        return INSTANCE;
    }

    public static boolean checkExternalServices() throws Exception {
        //Initialize the latch with number of service checkers
        latch = new CountDownLatch (3);

        //All add checker in lists
        services = new ArrayList<BaseHealthChecker> ();
        services.add (new NetworkHealthChecker (latch));
        services.add (new CacheHealthChecker (latch));
        services.add (new DatabaseHealthChecker (latch));

        // Start services checkers using executor framework
        Executor executor = Executors.newCachedThreadPool ();

        for (final BaseHealthChecker v : services) {
            executor.execute (v);
        }

        //Now wait till all services are checked
        latch.await ();

        //Services are file and now proceed startup
        for(final BaseHealthChecker v : services) {
            if (!v.isServiceUp ()) {
                return false;
            }
        }

        return true;
    }
}


/**
 * Main thread start
 * Create CountDownLatch for N threads
 * Create and start N threads
 * Main thread wait on latch
 * N threads completes there tasks are returns
 * Main thread resume execution
 */

public class CountDownLatchTest {

    public static void main (String[] args) {
        boolean result = false;
        try {
            result = ApplicationStartupUtil.checkExternalServices ();
        } catch (Exception e) {
            e.printStackTrace ();
        }

        System.out.println ("External services validation completed !! Result was :: "+ result);
    }
}
