public class Producer implements Runnable {

    private NumberFabric numberFabric;

    public Producer(NumberFabric numberFabric) {
        this.numberFabric = numberFabric;
    }

    @Override
    public void run() {
        while (true) {
            try {
                numberFabric.addRndNumber();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
