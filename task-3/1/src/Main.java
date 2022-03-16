import java.util.Random;

// program 3
public class Main {
    public static void main(String[] args) {
        int a = new Random().nextInt(899)+100;
        int b = new Random().nextInt(899)+100;
        int c = new Random().nextInt(899)+100;
        int d = a*1000 + b/100*100 + b%100/10*10 + b%10;

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d + " - " + c + " = " + (d - c));
    }
}