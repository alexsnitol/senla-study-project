public class Contumer implements Runnable {

    private NumberFabric numberFabric;

    public Contumer(NumberFabric numberFabric) {
        this.numberFabric = numberFabric;
    }

    @Override
    public void run() {
        while (true) {
            try {
                numberFabric.poolNumber();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
