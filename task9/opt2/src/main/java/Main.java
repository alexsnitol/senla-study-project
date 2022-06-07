import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        MyClass obj = new MyClass();

        Thread t1 = new Thread(obj);
        Thread t2 = new Thread(obj);

        t1.start();
        t2.start();
    }

}
