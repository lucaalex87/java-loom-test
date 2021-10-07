import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Test {


public static void main(String[] args) throws InterruptedException {

    final boolean USE_VIRTUAL_THREADS = true;
    final int CARRIER_THREAD_COUNT = 2;
    final int TASK_COUNT = 6;

    // plain old thread factory and thread pool using the new builder
    ThreadFactory carrierTF = Thread.ofPlatform().name("carrier#", 0).daemon(true).factory();
    ExecutorService carrierPool = Executors.newFixedThreadPool( CARRIER_THREAD_COUNT, carrierTF);
    ExecutorService executor;

    if(USE_VIRTUAL_THREADS) {

        // factory for virtual threads scheduled on the carrier pool
        ThreadFactory virtualTF = Thread.ofVirtual()
                .scheduler(carrierPool)
                .name("virtual#", 0).factory();

        // thread executor will spawn a new virtual thread for each task
        executor = Executors.newThreadExecutor(virtualTF);

    } else {
        executor = carrierPool;
    }

    for (int i = 0; i < TASK_COUNT; i++)
        executor.submit(new WaitAndHurry(i));

    executor.shutdown();
    executor.awaitTermination(20, TimeUnit.SECONDS);
}

private final static class WaitAndHurry implements Runnable {

    private final static long START_TIME = System.nanoTime();
	
	private Integer index = 0;
	
	WaitAndHurry(Integer index) {
		this.index = index;
	}

    @Override
    public void run() {
        doIO();    // block for 2s
        doWork();  // compute something for ~2s
        print("done");
    }

    private void doIO() {
        print("io");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void doWork() {
        print("work");
        long number = 479001599;
        boolean prime = true;
        for(long i = 2; i <= number/2; ++i) {
            if(number % i == 0)  {
                prime = false;
                break;
            }
        }
        if (!prime) {throw new RuntimeException("wrong result");} // to prevent the JIT to optimize everything away
    }

    private void print(String msg) {
        double elapsed = (System.nanoTime()-START_TIME)/1_000_000_000.0d;
        String timestamp = String.format("%.2fs", elapsed);
        System.out.println("Task "+index+": " + timestamp + " " + Thread.currentThread() + " " + msg);
    }

}


    
}
