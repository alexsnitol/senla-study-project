import static java.lang.System.out;

public class Main {

    private static final Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread() {
            public void run() {
                try {
                    synchronized(obj) {
                        obj.notifyAll();
                        obj.wait();
                        obj.notifyAll();
                        obj.wait();
                        obj.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        out.println(t.getState()); // NEW


        synchronized(obj) {
            t.start();
            out.println(t.getState()); // RUNNABLE
            obj.wait();
            obj.notifyAll();
            out.println(t.getState()); // BLOCKED
            obj.wait();
            out.println(t.getState()); // WAITING
            obj.notifyAll();
        }

        t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        };

        t.start();
        Thread.sleep(1000);
        out.println(t.getState()); // TIME_WAITING

        t.join();
        out.println(t.getState()); // TERMINATED

    }

}
