package com.javarush.games.moonlander;

public class Earth extends GameObject {
    private double x;
    private double y;

    public Earth(double x,double y) {
        super(x, y, ShapeMatrix.EARTH);

    }
}
