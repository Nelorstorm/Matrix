package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            // Игнорируем
        }

        int ordinat = 5;
        int abscis = 6;
        int castleX;
        int castleY;

        int rows = ordinat * 2 + 3;
        int cols = abscis * 2 + 3;

        String[][] field = new String[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    field[i][j] = "+";
                } else if (i % 2 == 1) {
                    field[i][j] = "-";
                } else if (j % 2 == 1) {
                    field[i][j] = "|";
                } else {
                    field[i][j] = " ";
                }
            }
        }

        for (int i = 0; i < cols; i++) {
            field[0][i] = "~";
            field[rows - 1][i] = "~";
        }
        for (int i = 0; i < rows; i++) {
            field[i][0] = "~";
            field[i][cols - 1] = "~";
        }

        Random random = new Random();

        castleX = random.nextInt(abscis);
        castleY = ordinat - 1;
        field[castleY * 2 + 2][castleX * 2 + 2] = "^";

        int startHiroX = random.nextInt(abscis);
        int startHiroY = 0;
        Person person = new Person(startHiroX, startHiroY);
        field[person.getY() * 2 + 2][person.getX() * 2 + 2] = "*";

        int totalFreeCells = (abscis * ordinat) - 2;
        int monstersCount = (int) Math.round(totalFreeCells * 0.30);
        int spawnedMonsters = 0;

        while (spawnedMonsters < monstersCount) {
            int mX = random.nextInt(abscis);
            int mY = random.nextInt(ordinat);

            if (field[mY * 2 + 2][mX * 2 + 2].equals(" ")) {
                field[mY * 2 + 2][mX * 2 + 2] = "M";
                spawnedMonsters++;
            }
        }

        Scanner scanner = new Scanner(System.in, "UTF-8");

        while (true) {
            drawMap(field);

            System.out.print("Здоровье персонажа: ");
            for (int i = 0; i < person.getHealth(); i++) {
                System.out.print("❤️ ");
            }
            System.out.println(" (" + person.getHealth() + ")");

            System.out.print("Введите команду WASD : ");
            String input = scanner.nextLine().toUpperCase().trim();

            int nextX = person.getX();
            int nextY = person.getY();

            if (input.equals("W")) nextY--;
            else if (input.equals("S")) nextY++;
            else if (input.equals("A")) nextX--;
            else if (input.equals("D")) nextX++;
            else {
                System.out.println("Неверная команда! Шагайте с помощью W, A, S, D");
                continue;
            }

            int targetFieldX = nextX * 2 + 2;
            int targetFieldY = nextY * 2 + 2;

            if (targetFieldY >= 0 && targetFieldY < rows && targetFieldX >= 0 && targetFieldX < cols) {

                if (field[targetFieldY][targetFieldX].equals("M")) {
                    clearConsole();
                    System.out.println("⚔️ На вас напал монстр! Чтобы защититься, решите пример:");

                    int num1 = random.nextInt(1000) + 1;
                    int num2 = random.nextInt(1000) + 1;
                    int operation = random.nextInt(2);

                    int correctAnswer = 0;
                    if (operation == 0) {
                        correctAnswer = num1 + num2;
                        System.out.print("Сколько будет: " + num1 + " + " + num2 + " = ");
                    } else {
                        if (num1 < num2) {
                            int temp = num1;
                            num1 = num2;
                            num2 = temp;
                        }
                        correctAnswer = num1 - num2;
                        System.out.print("Сколько будет: " + num1 + " - " + num2 + " = ");
                    }

                    int playerAnswer = -9999;
                    if (scanner.hasNextInt()) {
                        playerAnswer = scanner.nextInt();
                    }
                    scanner.nextLine();

                    if (playerAnswer == correctAnswer) {
                        System.out.println("Правильно! Вы победили монстра без потери здоровья!");
                        System.out.println("Нажмите Enter, чтобы продолжить...");
                        scanner.nextLine();
                    } else {
                        person.decreaseHealth();
                        System.out.println("Неверно! Правильный ответ был: " + correctAnswer);
                        System.out.println("Нажмите Enter, чтобы продолжить...");
                        scanner.nextLine();
                    }

                    field[targetFieldY][targetFieldX] = " ";
                }
            }


            if (person.getHealth() <= 0) {
                clearConsole();
                System.out.println("Игра окончена! Вас съели монстры! 👾");
                break;
            }

            field[person.getY() * 2 + 2][person.getX() * 2 + 2] = " ";

            person.move(input);

            int currentPX = person.getX() * 2 + 2;
            int currentPY = person.getY() * 2 + 2;

            if (currentPY <= 0 || currentPY >= rows - 1 || currentPX <= 0 || currentPX >= cols - 1) {
                clearConsole();
                System.out.println("Игра окончена! Вы упали в воду");
                break;
            }

            if (currentPY == castleY * 2 + 2 && currentPX == castleX * 2 + 2) {
                clearConsole();
                System.out.println("Победа! Вы добрались до замка!");
                break;
            }

            field[currentPY][currentPX] = "*";
        }
    }

    public static void drawMap(String[][] field) {
        clearConsole();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void clearConsole() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }
}