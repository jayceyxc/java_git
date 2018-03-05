package com.linus.concurrent.safe;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CopyOnWriteArrayListTest {
    private static int threadIndex = 0;
    private synchronized static int getThreadIndex() {
        threadIndex++;
        return threadIndex;
    }
    public static void main (String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();

        ExecutorService exec = Executors.newCachedThreadPool ();
        exec.submit (new Runnable () {
            @Override
            public void run () {
                for (int i = 0; i < 1000; i++) {
//                    System.out.println ("Add value: " + i);
                    list.add (String.valueOf (i));
                }
            }
        });
        for (int i = 0; i < 10; i++) {
            exec.submit (new Runnable () {

                int threadIndex = getThreadIndex ();

                @Override
                public void run () {
                    Iterator<String> iter = list.iterator ();
                    String e = null;
                    while (iter.hasNext ()) {
                        e = iter.next ();
//                        System.out.println ("thread " + this.threadIndex + " get " + e);
                    }
                    System.out.println ("thread " + this.threadIndex + " finished with " + e);
                }
            });
        }
    }
}
