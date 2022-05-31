public class Main {

    public static void main(String[] args) {

        NumberFabric numberFabric = new NumberFabric();

        Contumer contumer = new Contumer(numberFabric);
        Producer producer = new Producer(numberFabric);

        Thread t1 = new Thread(contumer);
        Thread t2 = new Thread(producer);

        t1.start();
        t2.start();

    }

}
