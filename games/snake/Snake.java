package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN =  "\u26AB";
    private List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake (int x, int y){
        snakeParts.add(new GameObject(x,y));
        snakeParts.add(new GameObject(x + 1,y));
        snakeParts.add(new GameObject(x + 2,y));

    }

    public void setDirection(Direction newDirection){

        if(newDirection != Direction.LEFT  && (snakeParts.get(0).x != snakeParts.get(1).x)&& newDirection != Direction.RIGHT && this.direction == Direction.RIGHT) this.direction = newDirection;
        else if(newDirection != Direction.RIGHT && (snakeParts.get(0).x != snakeParts.get(1).x) && newDirection != Direction.LEFT && this.direction == Direction.LEFT) this.direction = newDirection;
        else if(newDirection != Direction.DOWN  && (snakeParts.get(0).y != snakeParts.get(1).y) && newDirection != Direction.UP && this.direction == Direction.UP) this.direction = newDirection;
        else if(newDirection != Direction.UP  && (snakeParts.get(0).y != snakeParts.get(1).y) && newDirection != Direction.DOWN && this.direction == Direction.DOWN) this.direction = newDirection;


    }

    public void draw (Game game){
        for (int i = 0; i < snakeParts.size() ; i++) {
            if (isAlive) {
                if (i == 0)
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.BLACK, 75);
                else
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.BLACK, 75);
            }
            else {
                if (i == 0)
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
                else
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.RED, 75);
            }
        }
    }

    public void move(Apple apple){
        GameObject gameObject = createNewHead();
        int headX = gameObject.x;
        int headY = gameObject.y;
        if (checkCollision(gameObject)){  //проверка что змея не напоролась на саму себя
            isAlive = false;
        }
        else {

            if (headX < 0 || headX == SnakeGame.HEIGHT || headY < 0 || headY == SnakeGame.WIDTH) {//проверка выхода за пределы поля

                isAlive = false;

            } else {
                if (apple.x == headX && apple.y == headY) {//если сожрал яблоко
                    apple.isAlive = false;
                    snakeParts.add(0, gameObject);
                } else {                                  //если видимых событий не произошло, то движение продолжается
                    snakeParts.add(0, gameObject);
                    removeTail();
                }

            }
        }


    }

    public GameObject createNewHead(){

        int headX = snakeParts.get(0).x;
        int headY = snakeParts.get(0).y;
        GameObject gameObject = null;

        if (direction == Direction.LEFT){ gameObject = new GameObject(headX - 1,headY); }

        if (direction == Direction.RIGHT){gameObject = new GameObject(headX + 1,headY);  }

        if (direction == Direction.UP){ gameObject = new GameObject(headX,headY - 1);  }

        if (direction == Direction.DOWN){gameObject = new GameObject(headX,headY + 1);  }

        return gameObject;
    }

    public void removeTail(){

        snakeParts.remove(snakeParts.size() - 1);

    }

    public boolean checkCollision(GameObject gameObject){
        boolean collision = false;
        int x = gameObject.x;
        int y = gameObject.y;

        for (GameObject buffer : snakeParts ) {
            if (buffer.y == y && buffer.x == x){
                collision = true;
                break;
            }

        }

        return collision;
    }

    public int getLength(){

        return this.snakeParts.size();
    }

}
