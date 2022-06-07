public class Main {

    public static void main(String[] args) {
        MyDeamon myDeamon = new MyDeamon(5);

        Thread thread = new Thread(myDeamon);

        thread.setDaemon(true);

        thread.start();
    }

}
