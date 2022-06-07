import static java.lang.System.out;

public class MyClass implements Runnable {

    @Override
    public synchronized void run() {
        out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notifyAll();
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        run();
    }

}
