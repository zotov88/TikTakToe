import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class TicTacToe {
    public static void main(String[] args) {
        String[][] field = new String[3][3];
        showFieldBeforeGame();
        Scanner sc = new Scanner(System.in);
        int countToWin = setCountGames();
        int levelOfGame = setLevelGame();
        boolean yourStepNow = whoStartsGame();
        boolean isEndGame = false;
        int playerWin = 0;
        int compWin = 0;
        int usedSectors = 0;

        while (playerWin < countToWin && compWin < countToWin) {
            initializationField(field);
            while (!isEndGame) {
                if (yourStepNow) {
                    System.out.println("Твой ход");
                    // ввод числа игрока, проверка на корректность диапазона и отметка в игровом поле
                    String yourNumber = sc.nextLine();
                    while (!isPlayerStepCorrect(yourNumber, field)) {
                        yourNumber = sc.nextLine();
                    }
                    usedSectors++;
                    // проверка победителя и отображение игрового поля
                    isEndGame = isWinner(field, "XXX");
                    if (isEndGame) {
                        showField(field);
                        playerWin++;
                        yourStepNow = false;
                        break;
                    }
                    showField(field);
                    yourStepNow = false;
                } else {
                    System.out.print("Ход компьютера");
                    computerStep(field, levelOfGame);
                    usedSectors++;
                    isEndGame = isWinner(field, "OOO");
                    if (isEndGame) {
                        compWin++;
                        showField(field);
                        yourStepNow = true;
                        break;
                    }
                    showField(field);
                    yourStepNow = true;
                }
                if (usedSectors == 9) {
                    isEndGame = true;
                }
            }
            isEndGame = false;
            usedSectors = 0;
            System.out.println("    Счет\n" + "Ты " + playerWin + " - " + compWin + " Комп\n");
        }
        System.out.println(compWin > playerWin ? "*** You loose ***" : (playerWin > compWin ? "*** You win ***" : "*** It's a draw ***"));
        System.out.println("GAME OVER");
    }

    private static int setLevelGame() {
        System.out.println("Выберете уровень сложности:\nEasy - 1\nNormal - 2\nHard - 3");
        Scanner sc = new Scanner(System.in);
        int level;
        try {
            level = Integer.parseInt(sc.nextLine());
            if (level < 1 || level > 4) {
                System.out.println("Уровень игры определяется числами от 1 до 3");
                return setLevelGame();
            }
            return level;
        } catch (Exception e) {
            System.out.println("Введите целое число");
        }
        return setLevelGame();
    }

    private static int setCountGames() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nДо скольки побед будем играть?");
        int count;
        try {
            count = Integer.parseInt(sc.nextLine());
            return count;
        } catch (Exception e) {
            System.out.println("Введите целое число");
        }
        return setCountGames();
    }

    private static void computerStep(String[][] field, int levelOfGame) {
        if (field[1][1].isEmpty()) {
            field[1][1] = "O";
            return;
        }
        TreeSet<Integer> treeSet = uniqueNumbersFrom1to3(levelOfGame + 1);
        for (Integer n : treeSet) {
            switch (n) {
                case 0:
                    if (n == 0 && checkLinesThatThereAreTwoInRow(field, "OO")) {
                        return;
                    }
                case 1:
                    if (n == 1 && checkAcrossOneSector(field, "OO")) {
                        return;
                    }
                case 2:
                    if (n == 2 && checkLinesThatThereAreTwoInRow(field, "XX")) {
                        return;
                    }
                case 3:
                    if (n == 3 && checkAcrossOneSector(field, "XX")) {
                        return;
                    }
            }
        }
        Random random = new Random();
        while (true) {
            int i = random.nextInt(3);
            int j = random.nextInt(3);
            if (field[i][j].isEmpty()) {
                field[i][j] = "O";
                return;
            }
        }
    }

    private static TreeSet<Integer> uniqueNumbersFrom1to3(int levelOfGame) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        Random random = new Random();
        while (treeSet.size() < levelOfGame) {
            treeSet.add(random.nextInt(4));
        }
        return treeSet;
    }

    private static boolean checkLinesThatThereAreTwoInRow(String[][] field, String coincidence) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 1; j < field.length - 1; j++) {
                if ((field[i][j - 1] + field[i][j]).equals(coincidence) && field[i][j + 1].isEmpty()) {
                    field[i][j + 1] = "O";
                    return true;
                }
                if ((field[i][j] + field[i][j + 1]).equals(coincidence) && field[i][j - 1].isEmpty()) {
                    field[i][j - 1] = "O";
                    return true;
                }
            }
        }
        for (int i = 1; i < field.length - 1; i++) {
            for (int j = 0; j < field.length; j++) {
                if ((field[i - 1][j] + field[i][j]).equals(coincidence) && field[i + 1][j].isEmpty()) {
                    field[i + 1][j] = "O";
                    return true;
                }
                if ((field[i][j] + field[i + 1][j]).equals(coincidence) && field[i - 1][j].isEmpty()) {
                    field[i - 1][j] = "O";
                    return true;
                }
            }
        }
        if ((field[0][0] + field[1][1]).equals(coincidence) && field[2][2].isEmpty()) {
            field[2][2] = "O";
            return true;
        }
        if ((field[1][1] + field[2][2]).equals(coincidence) && field[0][0].isEmpty()) {
            field[0][0] = "O";
            return true;
        }
        if ((field[0][2] + field[1][1]).equals(coincidence) && field[2][0].isEmpty()) {
            field[2][0] = "O";
            return true;
        }
        if ((field[2][0] + field[1][1]).equals(coincidence) && field[0][2].isEmpty()) {
            field[0][2] = "O";
            return true;
        }
        return false;
    }

    private static boolean checkAcrossOneSector(String[][] field, String coincidence) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if ((field[i][0] + field[i][2]).equals(coincidence) && field[i][1].isEmpty()) {
                    field[i][1] = "O";
                    return true;
                }
                if ((field[0][j] + field[2][j]).equals(coincidence) && field[1][j].isEmpty()) {
                    field[1][j] = "O";
                    return true;
                }
            }
        }
        if ((field[0][0] + field[2][2]).equals(coincidence) && field[1][1].isEmpty()) {
            field[1][1] = "O";
            return true;
        }
        if ((field[0][2] + field[2][0]).equals(coincidence) && field[1][1].isEmpty()) {
            field[1][1] = "O";
            return true;
        }
        return false;
    }

    private static boolean whoStartsGame() {
        boolean result = new Random().nextBoolean();
        System.out.println("Подбрасываем монетку и... " + (result ? "Вы начинаете" : "Компьютер начинает"));
        return result;
    }

    private static boolean isWinner(String[][] field, String row) {
        for (int i = 0; i < 3; i++) {
            if ((field[i][0] + field[i][1] + field[i][2]).equals(row)) return true;
        }
        for (int i = 0; i < 3; i++) {
            if ((field[0][i] + field[1][i] + field[2][i]).equals(row)) return true;
        }
        if ((field[0][0] + field[1][1] + field[2][2]).equals(row)) return true;
        return (field[0][2] + field[1][1] + field[2][0]).equals(row);
    }

    private static boolean isCellNotEmpty(int[] step, String[][] field) {
        return !field[step[0]][step[1]].isEmpty();
    }

    private static boolean isPlayerStepCorrect(String number, String[][] field) {
        int n;
        try {
            n = Integer.parseInt(number);
            if (n > 0 && n < 10) {
                int[] step = parseIntToCoordinate(n);
                if (!isCellNotEmpty(step, field)) {
                    field[step[0]][step[1]] = "X";
                    return true;
                } else {
                    System.out.println("Клетка занята. Попробуйте еще");
                    return false;
                }
            } else {
                System.out.println("Вы ввели число не из указанного диапазона");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Вы ввели не число");
        }
        return false;
    }

    private static int[] parseIntToCoordinate(int num) {
        if (num == 1) return new int[]{2, 0};
        if (num == 2) return new int[]{2, 1};
        if (num == 3) return new int[]{2, 2};
        if (num == 4) return new int[]{1, 0};
        if (num == 5) return new int[]{1, 1};
        if (num == 6) return new int[]{1, 2};
        if (num == 7) return new int[]{0, 0};
        if (num == 8) return new int[]{0, 1};
        if (num == 9) return new int[]{0, 2};
        return null;
    }

    private static void showFieldBeforeGame() {
        String[][] field = new String[3][3];
        int init = 1;
        for (int i = field.length - 1; i >= 0; i--) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = String.valueOf(init++);
            }
        }
        showField(field);
        System.out.print("Вы ходите цифрами от 1 до 9");
    }

    public static void showField(String[][] field) {
        for (String[] strings : field) {
            System.out.println("\n_________________________");
            System.out.print("|");
            for (String string : strings) {
                System.out.print("\t" + string + "\t|");
            }
        }
        System.out.println("\n_________________________");
    }

    private static void initializationField(String[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = "";
            }
        }
    }
}