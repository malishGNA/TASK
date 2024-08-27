import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Требуется указать <circle_file> <points_file>");
            return;
        }
        String circleFilePath = args[0];
        String pointsFilePath = args[1];

        try {
            // Считывание данных окружности
            Circle circle = readCircleData(circleFilePath);

            // Считывание данных точек и проверка их положения
            readAndCheckPoints(pointsFilePath, circle);

        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтение файлов " + e.getMessage());
        }
    }

    private static Circle readCircleData(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] centerLine = reader.readLine().split(" ");
            double centerX = Double.parseDouble(centerLine[0]);
            double centerY = Double.parseDouble(centerLine[1]);
            double radius = Double.parseDouble(reader.readLine().trim());

            return new Circle(centerX, centerY, radius);
        }
    }

    private static void readAndCheckPoints(String filePath, Circle circle) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pointLine = line.split(" ");
                double x = Double.parseDouble(pointLine[0]);
                double y = Double.parseDouble(pointLine[1]);

                int position = checkPointPosition(x, y, circle);
                System.out.println(position);
            }
        }
    }

    private static int checkPointPosition(double x, double y, Circle circle) {
        double dx = x - circle.centerX;
        double dy = y - circle.centerY;
        double distanceSquared = dx * dx + dy * dy;
        double radiusSquared = circle.radius * circle.radius;

        if (distanceSquared == radiusSquared) {
            return 0; // точка лежит на окружности
        } else if (distanceSquared < radiusSquared) {
            return 1; // // точка внутри
        } else {
            return 2; // точка снаружи.
        }
    }

    private static class Circle {
        double centerX;
        double centerY;
        double radius;

        Circle(double centerX, double centerY, double radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
    }
}