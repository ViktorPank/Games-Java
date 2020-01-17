package com.javarush.games.moonlander;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;

public class Star extends GameObject {

    private int[] starsXY;


    public Star(double x, double y) {
        super(x, y, ShapeMatrix.WHITE_STAR);
    }

    public void setStarsXY(int[] starsXY) {
        this.starsXY = starsXY;

    }

    @Override
    public void draw(Game game) {
        for (int i = 0; i < starsXY.length ; i++) {
            game.setCellColor(starsXY[i],starsXY[++i], Color.WHITE);
        }
    }
}
