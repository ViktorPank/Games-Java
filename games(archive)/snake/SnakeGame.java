package com.javarush.games.snake;
import com.javarush.engine.cell.*;

import com.javarush.engine.cell.Game;

public class SnakeGame extends Game {

    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Snake snake;
    private Apple apple;
    private int turnDelay;
    private boolean isGameStopped;
    private static final int GOAL = 28;
    private int score;

    public void initialize(){
        setScreenSize(WIDTH,HEIGHT);
        createGame();

    }

    private void createGame(){


        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        score = 0;
        setScore(score);
        isGameStopped = false;
        drawScene();
        turnDelay = 400;
        setTurnTimer(turnDelay);

    }

    private void drawScene(){
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(i,j, Color.DARKSEAGREEN,"");
            }
        }

        snake.draw(this);
        apple.draw(this);
    }

    public void onTurn(int turn){
        snake.move(apple);
        if (!apple.isAlive){
            createNewApple();
            score +=5;
            setScore(score);
            turnDelay -=10;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive)gameOver();
        if (snake.getLength() > GOAL) win();

        drawScene();
    }

    private void createNewApple(){

        while (true) {

             apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));

             if(!snake.checkCollision(apple)) break;
     }

    }



    private void gameOver(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.WHITE,"ЗМЕЯ САМОУБИЙЦА", Color.RED, 45);

    }

    private void win(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.WHITE,"\tЗМЕЯ НАЕЛАСЬ!!! \nИ НЕ МОЖЕТ ИДТИ ДАЛЬШЕ", Color.GREEN, 35);
    }

    @Override
    public void onKeyPress(Key key) {
        if(key.equals(Key.LEFT))snake.setDirection(Direction.LEFT);
        if(key.equals(Key.RIGHT))snake.setDirection(Direction.RIGHT);
        if(key.equals(Key.UP))snake.setDirection(Direction.UP);
        if(key.equals(Key.DOWN))snake.setDirection(Direction.DOWN);
        if(key.equals(Key.SPACE) && isGameStopped == true) createGame();
    }
}
