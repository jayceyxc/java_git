package com.linus.concurrent;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class VarianceShare {

    private static int a;
    private static boolean finished = false;

    private static Map<String, String> showDetails = new HashMap<> ();
    private static BlockingQueue<String> flushQueue = new LinkedBlockingQueue<> ();

    public static void main (String[] args) throws InterruptedException {
        a = 10;
        ExecutorService executorService = Executors.newCachedThreadPool ();
        executorService.submit (new TimerThread ("Timer Thread"));
        while (true) {
            System.out.println ("a: " + a);
            if (a > 20) {
                finished = true;
                break;
            }
            System.out.println ("print showDetails");
            for (Map.Entry<String, String> entry : showDetails.entrySet ()) {
                System.out.printf ("key: %s, value: %s\r\n", entry.getKey (), entry.getValue ());
            }
            TimeUnit.SECONDS.sleep (2);
        }
        executorService.awaitTermination (2, TimeUnit.SECONDS);
        executorService.shutdown ();
    }

    static class TimerThread implements Runnable {

        private String name;

        public TimerThread (String name) {
            this.name = name;
        }

        @Override
        public void run () {
            while (true) {
                a++;
                showDetails.put (String.valueOf (a), this.name + String.valueOf (a));
                try {
                    TimeUnit.SECONDS.sleep (1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace ();
                }
                if (finished) {
                    break;
                }
            }
        }
    }
}
