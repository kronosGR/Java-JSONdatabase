import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        String a = scanner.next();
        String b = scanner.next();

        String tmp = a;
        a = b;
        b = tmp;
        System.out.println(a + " " + b);

    }
}