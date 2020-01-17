package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

import java.util.Arrays;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override

    //инициализация игры
    public void initialize() {

        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();

        //int test[] = {8, 8, 4, 4}; //test data for test move row

        //System.out.println(mergeRow(test));//test move row
    }

    //создание игры
    private void createGame() {

        clearFields();
        createNewNumber();
        createNewNumber();
    }

    //Отрисовка сцены
    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(j, i, gameField[i][j]);
            }
        }
    }

    //случайный разброс чисел
    private void createNewNumber() {
        int x, y;
        int freeCells = 0;

        //count free cells
        if (getMaxTileValue() < 2048) {
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (gameField[i][j] == 0) freeCells++;
                }
            }

            //random get cells
            if (freeCells > 0) {
                do {
                    x = getRandomNumber(SIDE);
                    y = getRandomNumber(SIDE);

                } while (gameField[y][x] != 0);

                //random set number select of cells

                if (getRandomNumber(10) == 9) {
                    gameField[y][x] = 4;

                } else {
                    gameField[y][x] = 2;
                }
            }
        } else {
            win();
        }

    }

    private void clearFields() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                gameField[i][j] = 0;
            }
        }
    }

    //получение цвета по цифре
    private Color getColorByValue(int value) {
        switch (value) {
            case (0):
                return Color.WHITE;
            case (2):
                return Color.RED;
            case (4):
                return Color.ALICEBLUE;
            case (8):
                return Color.GREEN;
            case (16):
                return Color.GRAY;
            case (32):
                return Color.AQUAMARINE;
            case (64):
                return Color.DEEPSKYBLUE;
            case (128):
                return Color.DARKOLIVEGREEN;
            case (256):
                return Color.YELLOW;
            case (512):
                return Color.DARKRED;
            case (1024):
                return Color.BROWN;
            case (2048):
                return Color.GOLD;
            default:
                return Color.WHITE;
        }

    }

    //Установка цвета и значения ячейки
    private void setCellColoredNumber(int x, int y, int value) {

        Color colorCell = getColorByValue(value);
        if (value == 0) {
            setCellValueEx(x, y, colorCell, "");
        } else {
            setCellValueEx(x, y, colorCell, String.valueOf(value));
        }

    }

    //проверка имеет ли место быть перемещению или нет, если да переставить
    private boolean compressRow(int[] row) {
        boolean flagNeedSort = false; //флаг показывает перемещался ли массив и нужна ли сортировка

        //цикл для проверки нужности перемещения элементов влево
        for (int i = SIDE - 1; i >= 0; i--) {
            if (row[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (row[j] == 0) {
                        flagNeedSort = true;
                        break;
                    }
                }
                if (flagNeedSort) break;
            }
        }

        //цикл для сортировки массива по убыванию
        if (flagNeedSort) row = invertArraySortSelect(row);
        return flagNeedSort;
    }

    //уборка нулей  в сторону
    private int[] invertArraySortSelect(int[] arraySrc) {

        for (int i = 0; i < SIDE; i++) {

            if (arraySrc[i] == 0) {
                for (int j = i + 1; j < SIDE; j++) {
                    if (arraySrc[j] != 0) {
                        arraySrc[j - 1] = arraySrc[j];
                        arraySrc[j] = 0;
                        //2340
                    }

                }
            }
        }
        return arraySrc;
    }

    //Проверка соседних элементов ячейки для слияния, если слияние нужно то возвращается true и происходит само слияние
    private boolean mergeRow(int[] row) {
        boolean flagNeedMerge = false;

        for (int i = 0; i < SIDE; i++) {

            for (int j = i + 1; j < SIDE; j++) {

                if (row[i] == row[j] && row[i] != 0 && row[j] != 0) {
                    row[i] = row[i] * 2;
                    row[j] = 0;
                    score += row [i];
                    i++;
                    flagNeedMerge = true;
                    setScore(score);
                    break;
                }
                i++;
            }
        }

        return flagNeedMerge;
    }

    @Override
    public void onKeyPress(Key key) {

        if (key == Key.SPACE && isGameStopped) { //restart game

            createGame();
            score = 0;
            setScore(score);
            isGameStopped = false;
            drawScene();
        } else if (!isGameStopped){

            if (!canUserMove()) gameOver(); // завершение игры

            else {
                if (key == Key.LEFT) {
                    moveLeft();

                }
                if (key == Key.RIGHT) {

                    moveRight();
                }
                if (key == Key.UP) {

                    moveUp();
                }
                if (key == Key.DOWN) {

                    moveDown();
                }
                drawScene();
            }
        }
    }

    private void moveLeft() {

        boolean flagChangeDraw = false;

        for (int i = 0; i < SIDE; i++) {

            if (compressRow(gameField[i])) flagChangeDraw = true; //сдвигаем влево числа

            if (mergeRow(gameField[i])) flagChangeDraw = true; // слияние чисел

            if (compressRow(gameField[i])) flagChangeDraw = true; //
        }

        if (flagChangeDraw) createNewNumber();//разрешение на добавку нового числа


    }

    private void moveRight() {

        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {

        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();


    }

    private void rotateClockwise() {
        int[][] rotMat = new int[SIDE][SIDE];

        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++) {
                rotMat[i][SIDE - 1 - j] = gameField[j][i];
            }
        gameField = rotMat;
    }

    private int getMaxTileValue() {
        int maxValueCell = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (maxValueCell < gameField[i][j]) maxValueCell = gameField[i][j];
            }
        }
        return maxValueCell;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Вы выиграли!!!", Color.BLACK, 45);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Вы проиграли!!!", Color.BLACK, 45);
    }

    //выдает разрешение если есть пустые клетки и есть возможность ходить даже если нет пустых клеток
    private boolean canUserMove() {
        boolean isUserMove = false;

        //Вложенный цикл для проверки остались ли нули, если остались то выдается разрешение на ход
        for (int i = 0; i < SIDE; i++) {

            for (int j = 0; j < SIDE; j++) {

                //если нулевой элемент есть, то разрешение выдается
                if (gameField[i][j] == 0) {
                    isUserMove = true;
                    break;
                }


            }

            if (isUserMove) break; //сокращает лишнии итерации если разрешение выдано

        }

        //Вложенный цикл проверяет есть ли еще возможность хода, если нулей не осталось
        if (!isUserMove) {

            for (int i = 0; i < SIDE; i++) {

                for (int j = 0; j < SIDE; j++) {

                    //проверка соседа сверху
                    if (i != 0) { //проверка существования соседа

                        if (gameField[i][j] == gameField[i - 1][j]) {//проверка совпадения с соседом для хода

                            isUserMove = true;
                            break;
                        }

                    }
                    //проверка соседа снизу
                    if (i != SIDE - 1) {//проверка существования соседа

                        if (gameField[i][j] == gameField[i + 1][j]) {//проверка совпадения с соседом для хода

                            isUserMove = true;
                            break;

                        }
                    }
                    //проверка соседа справа
                    if (j != SIDE - 1) {//проверка существования соседа

                        if (gameField[i][j] == gameField[i][j + 1]) {//проверка совпадения с соседом для хода

                            isUserMove = true;
                            break;

                        }

                    }
                    //проверка соседа слева
                    if (j != 0) {//проверка существования соседа

                        if (gameField[i][j] == gameField[i][j - 1]) {//проверка совпадения с соседом для хода

                            isUserMove = true;
                            break;

                        }
                    }
                }

                if (isUserMove) break; //сокращает лишнии итерации если разрешение выдано
            }
        }

        return isUserMove;
    }
}


