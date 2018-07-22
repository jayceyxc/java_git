package com.linus.concurrent;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author yuxuecheng
 * @version 1.0
 * @contact yuxuecheng@baicdata.com
 * @time 2018 June 01 10:06
 */


class MyThreadFactory implements ThreadFactory {

    private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory ();
    private Thread.UncaughtExceptionHandler handler;

    public MyThreadFactory(Thread.UncaughtExceptionHandler handler) {
        this.handler = handler;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread (Runnable r) {
        System.out.println ("call newThread");
        if (r instanceof  MyTask) {
            System.out.println ("r is MyTask");
        } else {
            System.out.println (r.getClass ());
        }
        Thread thread = defaultFactory.newThread (r);
        thread.setUncaughtExceptionHandler (handler);
        return thread;
    }
}


class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param thread the thread
     * @param t the exception
     */
    @Override
    public void uncaughtException (Thread thread, Throwable t) {
        System.out.println (thread.getClass ());
        System.out.println (thread.getState ());
        System.err.println ("Uncaught exception is detected! " + thread
                + " st: " + Arrays.toString(thread.getStackTrace ()));
        System.err.println ("Uncaught exception is detected! " + t
                + " st: " + Arrays.toString(t.getStackTrace()));
    }
}

final class MyTask implements Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run () {
        System.out.println ("My task is started running...");
        throw new ArithmeticException ();
    }
}


public class UncaughtExecptionHandler {
    public static void main (String[] args) {
        ThreadFactory factory = new MyThreadFactory (new MyExceptionHandler ());
        ExecutorService executorService = Executors.newFixedThreadPool (10, factory);
        executorService.execute (new MyTask ());
    }
}
