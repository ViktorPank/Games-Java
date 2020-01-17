package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.Random;

public class MoonLanderGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;
    private GameObject platformLie1;
    private GameObject platformLie2;
    private GameObject earth;
    private GameObject star;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isGameStopped;
    private boolean showPlatformLie1;
    private boolean showPlatformLie2;
    private boolean colisionPlatformLie1;
    private boolean colisionPlatformLie2;
    private int score;
    private int startSecondsMessage;

    @Override
    public void initialize() {

        //установка размера экрана
        setScreenSize(WIDTH, HEIGHT);
        //отключить сетку
        showGrid(false);

        //создание рандомных звезд на небе
       /* int[] starList = new int[20];
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            starList[i] = random.nextInt(WIDTH);
            starList[++i] = random.nextInt(HEIGHT);
        }
        star = new Star(1, 1);
        ((Star) star).setStarsXY(starList);
        */
        createGame();
    }

    private void createGame() {

        isGameStopped = false;
        score = 250;

        createGameObjects();

        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        startSecondsMessage = 0;
        colisionPlatformLie2 = false;
        colisionPlatformLie1 = false;

        setTurnTimer(50);
        drawScene();
    }

    @Override
    public void onTurn(int step) {


        //Если очки еще есть, нету стоклновения с лже-платформами и игра не остановлена. Ракета свободно летает
        if (score > 0 && !(colisionPlatformLie1 || colisionPlatformLie2) && !isGameStopped) {
            rocket.move(isUpPressed, isLeftPressed, isRightPressed);
            check();
            setScore(score);

            //drawScene();
            score--;
        }

        //Если очков нету, нету стоклновения с лже-платформами и игра не остановлена. Ракета отключает двигатели.
        if (score <= 0 && !(colisionPlatformLie1 || colisionPlatformLie2) && !isGameStopped) {
            rocket.move(false, false, false);
            check();
            setScore(score);
            //drawScene();
        }

        //отработка анимации падении ракеты в провал
        if (colisionPlatformLie1 && isGameStopped) {
            if (startSecondsMessage > 35) {
                showMessageDialog(Color.BLACK, "    У луны есть\n    свои тайны... ", Color.WHITE, 25);
                stopTurnTimer();
            } else startSecondsMessage++;
            rocket.move(false, false, false);
        }

        //отработка анимации падении ракеты и развала выступа
        if (colisionPlatformLie2 && isGameStopped) {
            if (startSecondsMessage > 50) {
                showMessageDialog(Color.BLACK, "    А выступ выглядел \n    таким надежным...", Color.WHITE, 25);
                stopTurnTimer();
            } else startSecondsMessage++;
            check_lie_platform_2();


            if (!rocket.isCollision(landscape)) rocket.move(false, false, false);
        }


        //есть ли коллизия ракеты с ложной платформой 1 и игра не остановлена
        if (rocket.isCollision(platformLie1) && !isGameStopped) {

            colisionPlatformLie1 = true;
            isGameStopped = true;
            //Проверка условий ракеты с лжеплатформой, отработка анимации и остановка игры
            check_lie_platform();
            //установка 0 баллов из за проигрыша
            setScore(0);
        }


        //есть ли коллизия ракеты с ложной платформой 2 и игра не остановлена
        if (rocket.isCollision(platformLie2) && !isGameStopped) {

            colisionPlatformLie2 = true;
            isGameStopped = true;
            //Проверка условий ракеты с лжеплатформой, отработка анимации и остановка игры
            check_lie_platform();
            //установка 0 баллов из за проигрыша
            setScore(0);

        }

        //отрисовка всех обьектов
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0) {
            super.setCellColor(x, y, color);
        }
    }

    private void drawScene() {

        showGrid(false);

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                setCellColor(j, i, Color.BLACK);
            }
        }
        //star.draw(this);
        landscape.draw(this);
        platform.draw(this);

        //при коллизии ракеты с лжеплатформой 1, лжеплатформа исчезает
        if (showPlatformLie1) platformLie1.draw(this);
        //при коллизии ракеты с лжеплатформой 2, лжеплатформа исчезает
        if (showPlatformLie2) platformLie2.draw(this);

        earth.draw(this);
        rocket.draw(this);
    }


    private void createGameObjects() {


        earth = new Earth(8, 7);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(12, MoonLanderGame.HEIGHT - 4, ShapeMatrix.PLATFORM);
        platformLie1 = new GameObject(31, MoonLanderGame.HEIGHT - 6, ShapeMatrix.LIE_PLATFORM_1);
        platformLie2 = new GameObject(45, MoonLanderGame.HEIGHT - 25, ShapeMatrix.LIE_PLATFORM_2);
        showPlatformLie1 = true;
        showPlatformLie2 = true;
        colisionPlatformLie1 = false;
        colisionPlatformLie2 = false;
        rocket = new Rocket(WIDTH / 2, 0);

    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.SPACE) && isGameStopped) createGame();
        else {
            if (key.equals(Key.UP)) {
                isUpPressed = true;
            }
            if (key.equals(Key.RIGHT)) {
                isRightPressed = true;
                isLeftPressed = false;
            }
            if (key.equals(Key.LEFT)) {
                isLeftPressed = true;
                isRightPressed = false;
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {

        if (key.equals(Key.UP)) {
            isUpPressed = false;
        }
        if (key.equals(Key.RIGHT)) {
            isRightPressed = false;
        }
        if (key.equals(Key.LEFT)) {
            isLeftPressed = false;
        }

    }

    //Проверка пересечения ракеты c ландшафтом и платформой
    private void check() {
        //Пересесение с ландшафтом
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform)) && !(rocket.isCollision(platformLie2)) && !(rocket.isCollision(platformLie1))) {
            rocket.switchFire(false, false, false);
            rocket.setSpeedX(0);
            rocket.setSpeedY(0);
            gameOver();

        }
        //Пересечение с платформой
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            rocket.switchFire(false, false, false);
            rocket.setSpeedX(0);
            rocket.setSpeedY(0);
            win();
        }
        //Пересечение платформы с большой скоростью
        if (rocket.isCollision(platform) && !rocket.isStopped()) {
            rocket.switchFire(false, false, false);
            rocket.setSpeedX(0);
            rocket.setSpeedY(0);
            gameOver();
        }


    }

    private void check_lie_platform() {

        //Если скорость ракеты большая отработать неудачное падение на луну
        if (!rocket.isStopped()) {

            rocket.switchFire(false, false, false);
            rocket.setSpeedY(0);
            rocket.setSpeedX(0);
            gameOver();


        } else {//иначе отработать сценарии лжеплаформ
            //Для отработки коллизии с лжеплатформой 1
            if (rocket.isCollision(platformLie1)) {
                showPlatformLie1 = false;//выключение отображения лжеплатформы
                colisionPlatformLie1 = true;//флаг коллизии с лжеплатформой 1

                //полная остановка ракеты
                rocket.setSpeedX(0);
                rocket.setSpeedY(0);

                //отработка анимации провала ракеты внутрь луны
                try {
                    failToHell();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Для отработки коллизии с лжеплатформой 2
            if (rocket.isCollision(platformLie2)) {
                showPlatformLie2 = false;//выключение отображения лжеплатформы
                colisionPlatformLie2 = true;//флаг коллизии с лжеплатформой 1

                //полная остановка ракеты
                rocket.setSpeedX(0);
                rocket.setSpeedY(0);

                //отработка анимации ломания скалы и падения ракеты
                try {
                    brokenRock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void check_lie_platform_2() {
        //Пересесение с ландшафтом
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform)) && !(rocket.isCollision(platformLie2)) && !(rocket.isCollision(platformLie1))) {
            rocket.switchFire(false, false, false);
            rocket.setSpeedX(0);
            rocket.setSpeedY(0);
            isGameStopped = true;
            rocket.crash();

        }
    }

    //Метод при выигрыше
    private void win() {

        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "      Молодец!\n  Это было мощно!", Color.WHITE, 25);
        stopTurnTimer();
    }

    //метод при проигрыше
    private void gameOver() {
        rocket.land();
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Аккуратнее космонавт!\n Ракеты не казенные!", Color.WHITE, 25);
        score = 0;
        stopTurnTimer();
    }

    private void failToHell() throws InterruptedException {
        rocket.switchFire(false, false, false);
        rocket.land();


        double failX = rocket.x;
        double failY = rocket.y + rocket.height - (HEIGHT - landscape.matrix.length);


        //создание провала под ракетой
        for (int j = (int) failY; j < landscape.matrix.length; j++) {
            for (int i = (int) failX; i < failX + rocket.width; i++) {
                landscape.matrix[j][i] = 0;
            }
        }
        rocket.move(false, false, false);
        isGameStopped = true;
    }

    private void brokenRock() throws InterruptedException {
        rocket.setSpeedY(0);
        rocket.switchFire(false, false, false);
        rocket.land();

        double failX = rocket.x - 2;
        double failY = rocket.y + rocket.height - (HEIGHT - landscape.matrix.length);


        for (int i = (int) failX; i < (rocket.width + rocket.x); i++) {
            for (int j = (int) failY; j < 17; j++) {
                landscape.matrix[j][i] = 0;
            }
        }

        isGameStopped = true;

    }


}
