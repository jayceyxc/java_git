package com.linus.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author yuxuecheng
 * @version 1.0
 * @contact yuxuecheng@baicdata.com
 * @time 2018 May 08 12:09
 */
public class ThreadPrint {

    static class TestRunnable implements Runnable {

        private int b = 0;

        @Override
        public void run () {
            m2 ();
        }

        public int getB () {
            return b;
        }

        public synchronized void m1() throws InterruptedException {
            b = 1000;
            TimeUnit.MILLISECONDS.sleep (10);
            System.out.println ("in m1 b = " + b);
        }

        public synchronized void m2() {
            try {
                b = 2000;
                TimeUnit.MILLISECONDS.sleep (10);
                System.out.println ("in run b = " + b);
            } catch (InterruptedException ie) {
                ie.printStackTrace ();
            }
        }
    }

    public static void main (String[] args) throws InterruptedException {
        TestRunnable runnable = new TestRunnable ();
        Thread thread = new Thread(runnable);
        thread.start ();

        TimeUnit.MILLISECONDS.sleep (10);
        runnable.m1 ();

        System.out.println ("in main thread. b = " + runnable.getB ());
    }
}
