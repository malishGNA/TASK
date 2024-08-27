import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Необходимо передать аргументы <n> <m>");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        if (n <= 0 || m <= 0) {
            System.out.println("n и m должны быть положительными целыми числами.");
            return;
        }
        List<Integer> path = getPath(n, m);
        System.out.println("Путь: " + path);
    }

    private static List<Integer> getPath(int n, int m) {
        List<Integer> path = new ArrayList<>();
        int start = 0;

        for (int i = 0; i < n; i++) {
            path.add(start + 1);
            start = (start + m - 1) % n;
            if (start == 0) {
                break;
            }
        }
        return path;
    }
}
