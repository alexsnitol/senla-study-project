import java.time.LocalTime;

import static java.lang.System.out;

public class MyDeamon implements Runnable {

    private int second;

    public MyDeamon(int second) {
        this.second = second;
    }

    @Override
    public void run() {
        while (true) {
            try {
                out.println(LocalTime.now());
                Thread.sleep(second * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
