import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MinMovesToEqualArray <inputFile>");
            return;
        }

        String inputFilePath = args[0];

        try {
            int[] nums = readNumbersFromFile(inputFilePath);
            int minMoves = calculateMinMoves(nums);
            System.out.println(minMoves);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    private static int[] readNumbersFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().mapToInt(Integer::parseInt).toArray();
        }
    }

    private static int calculateMinMoves(int[] nums) {
        if (nums.length == 0) return 0;

        Arrays.sort(nums);
        int median = nums[nums.length / 2]; // медиана отсортированного массива

        int minMoves = 0;
        for (int num : nums) {
            minMoves += Math.abs(num - median);
        }
        return minMoves;
    }
}