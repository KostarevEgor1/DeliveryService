package console;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    public String readText(String message) {
        while (true) {
            System.out.print(message);
            String text = scanner.nextLine();

            if (!text.isBlank()) {
                return text;
            }

            System.out.println("Значение не должно быть пустым.");
        }
    }

    public int readInt(String message, int min, int max) {
        while (true) {
            System.out.print(message);

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Введите целое число.");
                continue;
            }

            int value = scanner.nextInt();
            scanner.nextLine();

            if (value < min || value > max) {
                System.out.println("Введите число от " + min + " до " + max + ".");
                continue;
            }

            return value;
        }
    }
}