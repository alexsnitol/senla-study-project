import java.util.*;

import static java.lang.System.err;
import static java.lang.System.out;

public class NumberFabric {

    private Queue<Integer> buffer = new LinkedList<>();

    private static int getRandomNumber(int from, int to) {
        if (to >= from) {
            return new Random().nextInt(to - from) + from;
        }
        else {
            return -1;
        }
    }

    public synchronized void addRndNumber() throws InterruptedException {
        if (buffer.size() == 5) {
            out.println();

            Thread.sleep(500);

            notifyAll();
            wait();
        }
        Integer rndNum = getRandomNumber(10, 99);
        out.print(rndNum + " ");

        Thread.sleep(300);

        buffer.add(rndNum);
    }

    public synchronized void poolNumber() throws InterruptedException {
        if (buffer.size() == 0) {
            Thread.sleep(500);

            out.println();

            notifyAll();
            wait();
        }
        Thread.sleep(300);

        err.print(buffer.poll() + " ");
    }

}
