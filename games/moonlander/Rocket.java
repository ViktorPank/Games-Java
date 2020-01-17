package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.ArrayList;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);

        ArrayList listAnimationDownFire = new ArrayList();
        listAnimationDownFire.add(ShapeMatrix.FIRE_DOWN_1);
        listAnimationDownFire.add(ShapeMatrix.FIRE_DOWN_2);
        listAnimationDownFire.add(ShapeMatrix.FIRE_DOWN_3);
        downFire = new RocketFire(listAnimationDownFire);

        ArrayList listAnimationLeftFire = new ArrayList();
        ArrayList listAnimationRightFire = new ArrayList();
        listAnimationLeftFire.add(ShapeMatrix.FIRE_SIDE_1);
        listAnimationLeftFire.add(ShapeMatrix.FIRE_SIDE_2);
        listAnimationRightFire.add(ShapeMatrix.FIRE_SIDE_3);
        listAnimationRightFire.add(ShapeMatrix.FIRE_SIDE_4);
        rightFire = new RocketFire(listAnimationLeftFire);
        leftFire = new RocketFire(listAnimationRightFire);
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed,isLeftPressed,isRightPressed);
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void land() {
        this.y--;
    }

    public void crash() {

        matrix = ShapeMatrix.ROCKET_CRASH;
    }

    public void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            downFire.x = (this.x + (width / 2))-1;
            downFire.y = this.y + height;
            downFire.show();
        }else downFire.hide();
        if (isLeftPressed) {
            leftFire.y = this.y + this.height;
            leftFire.x = this.x + this.width;
            leftFire.show();
        }else leftFire.hide();
        if (isRightPressed) {
            rightFire.y = this.y + this.height;
            rightFire.x = this.x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.show();

        }else rightFire.hide();
    }
}
