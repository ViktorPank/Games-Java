package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class GameObject {
    public double x;
    public double y;
    public int[][] matrix;
    public int width;
    public int height;


    public GameObject(double x, double y, int[][] matrix) {
        this.x = x;
        this.y = y;

        this.width = matrix[0].length;
        this.height = matrix.length;
        this.matrix = new int[this.height][this.width];

        //создание копии матрицы
        for (int i = 0; i < this.height ; i++) {
            for (int j = 0; j < this.width ; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public void draw(Game game) {
        if(matrix == null) {
            return;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int colorIndex = matrix[j][i];
                game.setCellColor((int) x + i, (int) y + j, Color.values()[colorIndex]);
            }
        }
    }
}
