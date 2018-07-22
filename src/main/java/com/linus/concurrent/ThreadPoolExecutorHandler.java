package com.linus.concurrent;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author yuxuecheng
 * @version 1.0
 * @contact yuxuecheng@baicdata.com
 * @time 2018 June 01 11:30
 */


class MyThreadPoolExecutor extends ThreadPoolExecutor {

    public MyThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super (corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute (Runnable r, Throwable t) {
        super.afterExecute (r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                Object result = ((Future<?>) r).get ();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause ();
            } catch (InterruptedException ie) {
                Thread.currentThread ().interrupt (); // ignore/reset
            }
        }
        if (t != null) {
            // Exception occurred
            System.err.println("Uncaught exception is detected! " + t
                    + " st: " + Arrays.toString(t.getStackTrace()));
            // ... Handle the exception
            // Restart the runnable again
            execute(r);
        }
    }
}

public class ThreadPoolExecutorHandler {
    public static void main (String[] args) {
        ExecutorService executorService = new MyThreadPoolExecutor (10, 10, 0L, TimeUnit.MILLISECONDS.MILLISECONDS, new LinkedBlockingQueue<> ());
        executorService.execute (new MyTask ());
    }
}
