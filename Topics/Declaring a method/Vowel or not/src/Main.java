import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static boolean isVowel(char ch) {
        Pattern pattern = Pattern.compile("[aeoui]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(String.valueOf(ch));
        return matcher.find();
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char letter = scanner.nextLine().charAt(0);
        System.out.println(isVowel(letter) ? "YES" : "NO");
    }
}