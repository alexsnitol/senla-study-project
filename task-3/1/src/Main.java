import java.util.Random;

import static java.lang.System.out;

// program 3
public class Main {
    public static final int MAX_INT = 999;
    public static final int SHIFT = 100;

    public static int concatenateNumbers(Integer num1, Integer num2) {
        String concatenateNumbers = num1.toString() + num2.toString();
        return Integer.parseInt(concatenateNumbers);
    }

    public static int getRandomNumber(int from, int to) {
        if (to >= from)
            return new Random().nextInt(to - from) + from;
        else
            return -1;
    }

    public static void main(String[] args) {
        int num1 = getRandomNumber(SHIFT, MAX_INT);
        int num2 = getRandomNumber(SHIFT, MAX_INT);
        int num3 = getRandomNumber(SHIFT, MAX_INT);
        int concatenatedNum1Num2 = concatenateNumbers(num1, num2);

        out.println(num1);
        out.println(num2);
        out.println(num3);
        out.println(concatenatedNum1Num2 + " - " + num3 + " = " + (concatenatedNum1Num2 - num3));
    }
}